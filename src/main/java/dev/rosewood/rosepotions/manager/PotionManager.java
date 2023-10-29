package dev.rosewood.rosepotions.manager;

import com.google.gson.Gson;
import com.jeff_media.morepersistentdatatypes.DataType;
import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.manager.Manager;
import dev.rosewood.rosegarden.utils.HexUtils;
import dev.rosewood.rosepotions.potion.DataKeys;
import dev.rosewood.rosepotions.potion.PotionEffect;
import dev.rosewood.rosepotions.potion.PotionStatus;
import dev.rosewood.rosepotions.potion.impl.BurningAuraEffect;
import dev.rosewood.rosepotions.potion.impl.FreezeAuraEffect;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.persistence.PersistentDataContainer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class PotionManager extends Manager {

    private final Gson gson = new Gson();
    private final List<PotionEffect> loaded = new ArrayList<>();

    public PotionManager(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    @Override
    public void reload() {
        this.loaded.clear();

        // TODO: Improve the system for loading potion effect types.
        this.loaded.addAll(List.of(
                new BurningAuraEffect(this.rosePlugin),
                new FreezeAuraEffect(this.rosePlugin)
        ));

        // TODO: Async Ticker?
        Bukkit.getScheduler().runTaskTimer(this.rosePlugin, () -> {
            for (final Player player : Bukkit.getOnlinePlayers()) {
                Map<String, PotionStatus> effects = this.getEffects(player);
                if (effects.isEmpty())
                    continue;

                this.loaded.stream()
                        .filter(effect -> effects.containsKey(effect.getName()))
                        .forEach(effect -> effect.tick(player));
            }
        }, 0, 5);
    }

    @Override
    public void disable() {
        Bukkit.getScheduler().cancelTasks(this.rosePlugin);
    }

    /**
     * Gets the active potion effects for a player
     *
     * @param player The player to get the potion effects for
     * @return The potion effects for the player
     */
    public Map<String, PotionStatus> getEffects(Player player) {
        final PersistentDataContainer container = player.getPersistentDataContainer();

        Map<String, PotionStatus> results = container.getOrDefault(
                        DataKeys.EFFECTS_KEY,
                        DataType.asList(DataType.STRING),
                        new ArrayList<>()
                )
                .stream()
                .map(s -> this.gson.fromJson(s, PotionStatus.class))
                .collect(Collectors.toMap(PotionStatus::getName, status -> status));

        if (results.isEmpty())
            return new HashMap<>();

        List<String> toRemove = new ArrayList<>();
        results.values().forEach(status -> {
            if (status.isExpired()) {
                toRemove.add(status.getName());
            }
        });

        // Remove the expired effects from the player's data container
        if (!toRemove.isEmpty()) {
            toRemove.forEach(results::remove);

            container.set(
                    DataKeys.EFFECTS_KEY,
                    DataType.asList(DataType.STRING),
                    results.values()
                            .stream()
                            .map(this.gson::toJson)
                            .toList()
            );
        }

        return results;
    }

    /**
     * Applies a potion effect to a player
     *
     * @param player The player to apply the potion effect to
     * @param effect The potion effect to apply
     */
    public void applyEffect(Player player, PotionEffect effect, PotionStatus status) {
        final Map<String, PotionStatus> effects = this.getEffects(player);
        effects.put(effect.getName(), status);

        player.getPersistentDataContainer().set(
                DataKeys.EFFECTS_KEY,
                DataType.asList(DataType.STRING),
                effects.values()
                        .stream()
                        .map(this.gson::toJson)
                        .toList()
        );
    }

    /**
     * Removes a potion effect from a player
     *
     * @param player The player to remove the potion effect from
     * @param effect The potion effect to remove
     */
    public void removeEffect(Player player, String effect) {
        Map<String, PotionStatus> effects = this.getEffects(player);
        effects.remove(effect);

        player.getPersistentDataContainer().set(
                DataKeys.EFFECTS_KEY,
                DataType.asList(DataType.STRING),
                effects.values()
                        .stream()
                        .map(this.gson::toJson)
                        .toList()
        );
    }

    /**
     * Check if a player has a potion effect active
     *
     * @param player The player to check
     * @param effect The potion effect to check for
     * @return True if the player has the potion effect active, otherwise false
     */
    public boolean hasEffect(Player player, PotionEffect effect) {
        return this.getEffects(player).containsKey(effect.getName());
    }

    /**
     * Creates a potion item stack
     *
     * @param effect   The potion effect to create the item stack for
     * @param duration The duration of the potion effect
     * @param unit     The unit of time for the duration
     * @return The created potion item stack
     */
    public ItemStack createPotion(PotionEffect effect, int duration, TimeUnit unit) {
        PotionStatus status = new PotionStatus(
                effect.getName(),
                -1,
                unit.toMillis(duration),
                0
        );

        ItemStack itemStack = new ItemStack(Material.POTION);
        PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
        if (meta == null) return itemStack;

        meta.setDisplayName(HexUtils.colorify("&b" + effect.getName() + " Potion"));
        meta.setLore(Arrays.asList(
                "",
                HexUtils.colorify("&7Duration: &f" + unit.toSeconds(duration) + " seconds"),
                HexUtils.colorify("&7Effect: &f" + effect.getName())
        ));

        PersistentDataContainer container = meta.getPersistentDataContainer();
        container.set(
                DataKeys.EFFECTS_KEY,
                DataType.asList(DataType.STRING), List.of(
                        this.gson.toJson(status)
                ));
        itemStack.setItemMeta(meta);
        return itemStack;
    }

    /**
     * Gets the potion effects from an item stack
     *
     * @param itemStack The item stack to get the potion effects from
     * @return The potion effects from the item stack
     */
    public Map<String, PotionStatus> getEffects(ItemStack itemStack) {
        final PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
        if (meta == null)
            return new HashMap<>();

        return meta.getPersistentDataContainer()
                .getOrDefault(
                        DataKeys.EFFECTS_KEY,
                        DataType.asList(DataType.STRING),
                        new ArrayList<>()
                )
                .stream()
                .map(s -> this.gson.fromJson(s, PotionStatus.class))
                .collect(Collectors.toMap(PotionStatus::getName, status -> status));
    }

    /**
     * Wipes all potion effects from a player
     *
     * @param player The player to wipe the potion effects from
     */
    public void wipeEffects(Player player) {
        player.getPersistentDataContainer().remove(DataKeys.EFFECTS_KEY);
        player.getPersistentDataContainer().remove(DataKeys.EFFECTS_DURATION);
        player.getPersistentDataContainer().remove(DataKeys.EFFECTS_AMPLIFIER);
    }

    public List<PotionEffect> getLoaded() {
        return loaded;
    }

}

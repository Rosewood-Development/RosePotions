package dev.rosewood.rosepotions.listener;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosepotions.manager.PotionManager;
import dev.rosewood.rosepotions.potion.PotionEffect;
import dev.rosewood.rosepotions.potion.PotionStatus;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

public class PotionListeners implements Listener {

    private final RosePlugin rosePlugin;

    public PotionListeners(RosePlugin rosePlugin) {
        this.rosePlugin = rosePlugin;
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onConsume(PlayerItemConsumeEvent event) {
        final ItemStack consumed = event.getItem();
        final PotionManager manager = this.rosePlugin.getManager(PotionManager.class);
        final Map<String, PotionStatus> potionEffects = manager.getEffects(consumed);

        if (potionEffects.isEmpty())
            return;

        for (final PotionStatus status : potionEffects.values()) {
            PotionEffect effect = manager.getLoaded()
                    .stream()
                    .filter(e -> e.getName().equals(status.getName()))
                    .findFirst()
                    .orElse(null);

            if (effect == null)
                continue;

            status.setActivationTime(System.currentTimeMillis());
            effect.apply(event.getPlayer(), status);
        }
    }

}

package dev.rosewood.rosepotions.potion;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosepotions.manager.PotionManager;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerAnimationType;

import java.util.concurrent.TimeUnit;

public abstract class PotionEffect {

    protected final RosePlugin rosePlugin;

    public PotionEffect(RosePlugin rosePlugin) {
        this.rosePlugin = rosePlugin;
    }

    /**
     * Gets the name of this potion effect
     *
     * @return The name of this potion effect
     */
    public abstract String getName();

    /**
     * Called when this potion effect is applied to a player
     *
     * @param player The player
     * @param status The potion status
     */
    public void apply(Player player, PotionStatus status) {
        status.setActivationTime(System.currentTimeMillis());

        this.rosePlugin.getManager(PotionManager.class).applyEffect(player, this, status);
    }

    /**
     * Called when this potion effect is applied to a player
     *
     * @param player The player
     */
    public void apply(Player player) {
        final PotionStatus status = new PotionStatus(
                this.getName(),
                System.currentTimeMillis(),
                TimeUnit.SECONDS.toMillis(30),
                0
        );

        this.apply(player, status);
    }

    /**
     * Called when this potion effect is removed from a player
     *
     * @param player The player
     */
    public void remove(Player player) {
        this.rosePlugin.getManager(PotionManager.class).removeEffect(player, this.getName());
    }

    /**
     * Checks if the given player has this potion effect
     *
     * @param player The player
     * @return True if the player has this potion effect
     */
    public boolean has(Player player) {
        return this.rosePlugin.getManager(PotionManager.class).hasEffect(player, this);
    }

    /**
     * Tick the potion effect for the given player
     *
     * @param player The player
     */
    public void tick(Player player) {
        // Unused by default
    }

}

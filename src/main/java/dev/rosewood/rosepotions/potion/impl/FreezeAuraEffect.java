package dev.rosewood.rosepotions.potion.impl;

import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.utils.HexUtils;
import dev.rosewood.rosepotions.potion.PotionEffect;
import dev.rosewood.rosepotions.util.MathL;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class FreezeAuraEffect extends PotionEffect {

    private final long cooldown = TimeUnit.SECONDS.toMillis(7);
    private long lastTrigger = System.currentTimeMillis();

    public FreezeAuraEffect(RosePlugin rosePlugin) {
        super(rosePlugin);
    }

    @Override
    public String getName() {
        return "freeze_aura";
    }

    @Override
    public void tick(Player player) {
        if (System.currentTimeMillis() - this.lastTrigger < this.cooldown)
            return;

        this.lastTrigger = System.currentTimeMillis();

        player.getWorld().getNearbyEntities(player.getLocation(), 5, 5, 5).stream()
                .filter(entity -> !entity.getUniqueId().equals(player.getUniqueId()))
                .filter(entity -> entity instanceof LivingEntity)
                .map(entity -> (LivingEntity) entity)
                .forEach(entity -> entity.addPotionEffect(new org.bukkit.potion.PotionEffect(org.bukkit.potion.PotionEffectType.SLOW, 20 * 4, 2)));

        for (Location loc : this.createCircle(player.getLocation(), 5)) {
            player.getWorld().spawnParticle(Particle.FALLING_DUST, loc, 1, 0, 0, 0, 0, Material.ICE.createBlockData());
        }

    }

    /**
     * Create a circle from the center of a location with a specified radius
     *
     * @param center The center location
     * @param range  The radius of the circle
     * @return The list of particle locations
     */
    private List<Location> createCircle(Location center, double range) {
        final List<Location> locs = new ArrayList<>();
        for (int i = 0; i < 120; i++) {
            double dx = MathL.cos(Math.PI * 2 * ((double) i / 120)) * range;
            double dz = MathL.sin(Math.PI * 2 * ((double) i / 120)) * range;
            locs.add(center.clone().add(dx, 0.0, dz));
        }

        return locs;
    }

}

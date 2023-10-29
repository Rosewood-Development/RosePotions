package dev.rosewood.rosepotions.potion;

import dev.rosewood.rosepotions.RosePotions;
import org.bukkit.NamespacedKey;

public final class DataKeys {

    public static final NamespacedKey EFFECTS_KEY = new NamespacedKey(RosePotions.getInstance(), "effects");
    public static final NamespacedKey EFFECTS_DURATION = new NamespacedKey(RosePotions.getInstance(), "potion_duration");
    public static final NamespacedKey EFFECTS_AMPLIFIER = new NamespacedKey(RosePotions.getInstance(), "potion_amplifier");

}

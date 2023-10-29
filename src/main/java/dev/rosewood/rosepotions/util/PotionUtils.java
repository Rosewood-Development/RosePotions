package dev.rosewood.rosepotions.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class PotionUtils {

    public static <T extends Enum<T>> T getEnum(@NotNull Class<T> enumClass, @Nullable String name) {
        if (name == null)
            return null;

        try {
            return Enum.valueOf(enumClass, name.toUpperCase());
        } catch (IllegalArgumentException | NullPointerException e) {
            return null;
        }
    }


}

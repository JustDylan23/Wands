package me.dylan.wands.utils;

import org.bukkit.Location;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public final class LocationUtil {
    private LocationUtil() {
    }

    @Contract("_ -> new")
    public static @NotNull Location toCenterLocation(@NotNull Location loc) {
        return new Location(
                loc.getWorld(),
                Math.floor(loc.getX()) + 0.5,
                Math.floor(loc.getY()) + 0.5,
                Math.floor(loc.getZ()) + 0.5
        );
    }
}

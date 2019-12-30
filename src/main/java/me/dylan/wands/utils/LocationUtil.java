package me.dylan.wands.utils;

import org.bukkit.Location;
import org.bukkit.block.Block;
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

    @Contract("_ -> new")
    public static @NotNull Location toCenterBlock(@NotNull Block block) {
        return new Location(
                block.getWorld(),
                block.getX() + 0.5,
                block.getY() + 0.5,
                block.getZ() + 0.5
        );
    }
}

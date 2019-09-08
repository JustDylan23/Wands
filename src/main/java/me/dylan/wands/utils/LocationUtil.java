package me.dylan.wands.utils;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class LocationUtil {
    @Contract(value = " -> fail", pure = true)
    private LocationUtil() {
        throw new UnsupportedOperationException("Instantiating util class");
    }

    public static Location toCenterLocation(@NotNull Location loc) {
        return new Location(
                loc.getWorld(),
                Math.floor(loc.getX()) + 0.5,
                Math.floor(loc.getY()) + 0.5,
                Math.floor(loc.getZ()) + 0.5
        );
    }

    public static Location toCenterBlock(@NotNull Block block) {
        return new Location(
                block.getWorld(),
                block.getX() + 0.5,
                block.getY() + 0.5,
                block.getZ() + 0.5
        );
    }
}

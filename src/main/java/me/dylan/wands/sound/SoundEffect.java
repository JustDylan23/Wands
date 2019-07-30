package me.dylan.wands.sound;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

@FunctionalInterface
public interface SoundEffect {
    SoundEffect EMPTY = location -> {
    };

    default void play(Entity entity) {
        play(entity.getLocation());
    }

    void play(Location location);
}

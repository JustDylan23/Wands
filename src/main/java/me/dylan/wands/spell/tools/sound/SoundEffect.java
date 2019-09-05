package me.dylan.wands.spell.tools.sound;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

@FunctionalInterface
public interface SoundEffect {
    SoundEffect NONE = location -> {
    };

    default void play(Entity entity) {
        play(entity.getLocation());
    }

    void play(Location location);
}

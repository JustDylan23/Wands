package me.dylan.wands.spell.spelleffect.sound;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import javax.annotation.Nonnull;

@FunctionalInterface
public interface SoundEffect {
    SoundEffect EMPTY = location -> {
    };

    default void play(@Nonnull Entity entity) {
        play(entity.getLocation());
    }

    void play(@Nonnull Location location);
}

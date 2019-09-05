package me.dylan.wands.spell.tools.sound;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public final class CompoundSound implements SoundEffect {
    private final List<SoundEffect> sounds = new ArrayList<>();

    private CompoundSound() {
    }

    public static CompoundSound chain() {
        return new CompoundSound();
    }

    public CompoundSound add(Sound sound) {
        sounds.add(SingularSound.from(sound, 1));
        return this;
    }

    public CompoundSound add(Sound sound, float pitch) {
        sounds.add(SingularSound.from(sound, pitch));
        return this;
    }

    public CompoundSound addAll(Sound sound, float pitch, int... repeat) {
        sounds.add(RepeatableSound.from(sound, pitch, repeat));
        return this;
    }

    @Override
    public void play(Location location) {
        for (SoundEffect sound : sounds) {
            sound.play(location);
        }
    }

    @Override
    public void play(Entity entity) {
        for (SoundEffect sound : sounds) {
            sound.play(entity);
        }
    }
}

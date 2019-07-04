package me.dylan.wands.spell.spelleffect.sound;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class CompoundSound implements SoundEffect {
    private final List<SoundEffect> sounds = new ArrayList<>();

    private CompoundSound() {
    }

    public CompoundSound add(@Nonnull Sound sound) {
        sounds.add(SingularSound.from(sound, 1));
        return this;
    }

    public CompoundSound add(@Nonnull Sound sound, float pitch) {
        sounds.add(SingularSound.from(sound, pitch));
        return this;
    }

    public CompoundSound add(@Nonnull Sound sound, float pitch, int... repeat) {
        sounds.add(me.dylan.wands.spell.spelleffect.sound.RepeatableSound.from(sound, pitch, repeat));
        return this;
    }

    public static CompoundSound chain() {
        return new CompoundSound();
    }

    @Override
    public void play(@Nonnull Location location) {
        for (SoundEffect sound : sounds) {
            sound.play(location);
        }
    }

    @Override
    public void play(@Nonnull Entity entity) {
        for (SoundEffect sound : sounds) {
            sound.play(entity);
        }
    }
}

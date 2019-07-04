package me.dylan.wands.spell.spelleffect.sound;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;

import javax.annotation.Nonnull;

public class SingularSound implements SoundEffect {
    private final Sound sound;
    private final float pitch;

    private SingularSound(@Nonnull Sound sound, float pitch) {
        this.sound = sound;
        this.pitch = pitch;
    }

    public static SingularSound from(@Nonnull Sound sound, float pitch) {
        return new SingularSound(sound, pitch);
    }

    @Override
    public void play(@Nonnull Location location) {
        location.getWorld().playSound(location, sound, SoundCategory.MASTER, 4, pitch);
    }
}

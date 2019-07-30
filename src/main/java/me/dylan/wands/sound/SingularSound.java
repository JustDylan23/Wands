package me.dylan.wands.sound;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;

public class SingularSound implements SoundEffect {
    private final Sound sound;
    private final float pitch;

    private SingularSound(Sound sound, float pitch) {
        this.sound = sound;
        this.pitch = pitch;
    }

    public static SingularSound from(Sound sound, float pitch) {
        return new SingularSound(sound, pitch);
    }

    @Override
    public void play(Location location) {
        location.getWorld().playSound(location, sound, SoundCategory.MASTER, 4, pitch);
    }
}

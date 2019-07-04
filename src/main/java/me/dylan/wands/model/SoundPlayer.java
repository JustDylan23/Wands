package me.dylan.wands.model;

import me.dylan.wands.util.EffectUtil;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SoundPlayer {
    public static final SoundPlayer EMPTY = new SoundPlayer();
    private final List<SoundWrapper> sounds = new ArrayList<>();

    private SoundPlayer() {
    }

    public SoundPlayer add(Sound sound, float volume, float pitch, int... repeat) {
        sounds.add(new SoundWrapper(sound, volume, pitch, repeat));
        return this;
    }

    public static SoundPlayer chain() {
        return new SoundPlayer();
    }

    public void play(Player player) {
        Location loc = player.getLocation();
        for (SoundWrapper sound : sounds) {
            sound.play(loc);
        }
    }

    private static class SoundWrapper {
        final Sound sound;
        final float volume, pitch;
        final int[] repeat;

        SoundWrapper(Sound sound, float volume, float pitch, int... repeat) {
            this.sound = sound;
            this.volume = volume;
            this.pitch = pitch;
            this.repeat = repeat;
        }

        void play(Location loc) {
            loc.getWorld().playSound(loc, sound, volume, pitch);
            if (repeat.length != 0) {
                EffectUtil.runTaskLater(() -> {
                    loc.getWorld().playSound(loc, sound, SoundCategory.MASTER, volume, pitch);
                }, repeat);
            }
        }
    }
}

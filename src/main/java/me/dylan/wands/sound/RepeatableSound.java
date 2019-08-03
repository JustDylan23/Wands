package me.dylan.wands.sound;

import me.dylan.wands.spell.SpellEffectUtil;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class RepeatableSound implements SoundEffect {
    private final Sound sound;
    private final float pitch;
    private final int[] repeat;

    private RepeatableSound(Sound sound, float pitch, int[] repeat) {
        this.sound = sound;
        this.pitch = pitch;
        this.repeat = repeat;
    }

    @NotNull
    public static RepeatableSound from(Sound sound, float pitch, int... repeat) {
        return new RepeatableSound(sound, pitch, repeat);
    }

    @Override
    public void play(@NotNull Location location) {
        World world = location.getWorld();
        SpellEffectUtil.runTaskLater(() -> world.playSound(location, sound, SoundCategory.MASTER, 4, pitch), repeat);
    }

    @Override
    public void play(@NotNull Entity entity) {
        World world = entity.getLocation().getWorld();
        SpellEffectUtil.runTaskLater(() -> world.playSound(entity.getLocation(), sound, SoundCategory.MASTER, 4, pitch), repeat);
    }
}

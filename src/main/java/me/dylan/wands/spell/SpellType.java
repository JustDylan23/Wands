package me.dylan.wands.spell;

import me.dylan.wands.spell.implementations.*;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public enum SpellType {
    BLOOD_BLOCK(BloodBlock.INSTANCE),
    BLOOD_EXPLODE(BloodExplode.INSTANCE),
    BLOOD_SPARK(BloodSpark.INSTANCE),
    BLOOD_STUN(BloodStun.INSTANCE),
    BLOOD_WAVE(BloodWave.INSTANCE),
    COMET(Comet.INSTANCE),
    CONFUSE(Confuse.INSTANCE),
    CORRUPTED_LAUNCH(CorruptedLaunch.INSTANCE),
    CORRUPTED_RAIN(CorruptedRain.INSTANCE),
    CORRUPTED_SHOCK_WAVE(CorruptedShockWave.INSTANCE),
    CORRUPTED_SPARK(CorruptedSpark.INSTANCE),
    CORRUPTED_WAVE(CorruptedWave.INSTANCE),
    CORRUPTED_WOLFS(CorruptedWolves.INSTANCE),
    DARK_AURA(DarkAura.INSTANCE),
    DARK_BLOCK(DarkBlock.INSTANCE),
    DARK_CIRCLE(DarkCircle.INSTANCE),
    DARK_PULSE(DarkPulse.INSTANCE),
    DARK_PUSH(DarkPush.INSTANCE),
    DARK_SPARK(DarkSpark.INSTANCE),
    ESCAPE(Escape.INSTANCE),
    FIRE_COMET(FireComet.INSTANCE),
    FIRE_SPARK(FireSpark.INSTANCE),
    FIRE_TWISTER(FireTwister.INSTANCE),
    FLAME_SHOCK_WAVE(FlameShockWave.INSTANCE),
    FLAME_THROWER(FlameThrower.INSTANCE),
    FLAME_WAVE(FlameWave.INSTANCE),
    ICE_AURA(IceAura.INSTANCE),
    ICE_FREEZE(Freeze.INSTANCE),
    LAUNCH(Launch.INSTANCE),
    POISON_WAVE(PoisonWave.INSTANCE),
    SPARK(Spark.INSTANCE),
    THUNDER_ARROW(LightningArrow.INSTANCE),
    THUNDER_RAGE(ThunderRage.INSTANCE),
    THUNDER_STORM(ThunderStorm.INSTANCE),
    THUNDER_STRIKE(ThunderStrike.INSTANCE);

    public final Castable castable;

    @Contract(pure = true)
    SpellType(Castable castable) {
        this.castable = castable;
    }

    @Nullable
    public static Castable getSpell(@NotNull String name) {
        try {
            return SpellType.valueOf(name.toUpperCase()).castable;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Nullable
    public static SpellType getSpellType(@NotNull String name) {
        try {
            return SpellType.valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public String getName() {
        return castable.getDisplayName();
    }
}
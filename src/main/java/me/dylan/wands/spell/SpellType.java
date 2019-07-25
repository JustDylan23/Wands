package me.dylan.wands.spell;

import me.dylan.wands.spell.handler.MySomething;
import me.dylan.wands.spell.implementations.*;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public enum SpellType {
    COMET(Comet.INSTANCE),
    CONFUSE(Confuse.INSTANCE),
    SPARK(Spark.INSTANCE),
    LAUNCH(Launch.INSTANCE),
    POISON_WAVE(PoisonWave.INSTANCE),

    BLOOD_BLOCK(BloodBlock.INSTANCE),
    BLOOD_EXPLODE(BloodExplode.INSTANCE),
    BLOOD_SPARK(BloodSpark.INSTANCE),
    BLOOD_STUN(BloodStun.INSTANCE),
    BLOOD_WAVE(BloodWave.INSTANCE),

    ICE_AURA(IceAura.INSTANCE),
    ICE_FREEZE(Freeze.INSTANCE),
    THUNDER_ARROW(LightningArrow.INSTANCE),
    THUNDER_RAGE(ThunderRage.INSTANCE),
    THUNDER_STORM(ThunderStorm.INSTANCE),
    THUNDER_STRIKE(ThunderStrike.INSTANCE),

    FIRE_COMET(FireComet.INSTANCE),
    FIRE_SPARK(FireSpark.INSTANCE),
    FIRE_TWISTER(FireTwister.INSTANCE),
    FLAME_SHOCK_WAVE(FlameShockWave.INSTANCE),
    FLAME_THROWER(FlameThrower.INSTANCE),
    FLAME_WAVE(FlameWave.INSTANCE),

    DARK_AURA(DarkAura.INSTANCE),
    DARK_BLOCK(DarkBlock.INSTANCE),
    DARK_CIRCLE(DarkCircle.INSTANCE),
    DARK_PULSE(DarkPulse.INSTANCE),
    DARK_PUSH(DarkPush.INSTANCE),
    DARK_SPARK(DarkSpark.INSTANCE),

    CORRUPTED_WAVE(CorruptedWave.INSTANCE),
    CORRUPTED_WOLFS(CorruptedWolves.INSTANCE),
    CORRUPTED_RAIN(CorruptedRain.INSTANCE),
    CORRUPTED_SHOCK_WAVE(CorruptedShockWave.INSTANCE),
    CORRUPTED_LAUNCH(CorruptedLaunch.INSTANCE),
    CORRUPTED_SPARK(CorruptedSpark.INSTANCE),

    JETP(new MySomething());

    public final Castable castable;

    SpellType(Castable castable) {
        this.castable = castable;
    }

    @Nullable
    public static Castable getSpell(@Nonnull String name) {
        try {
            return SpellType.valueOf(name.toUpperCase()).castable;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Nullable
    public static SpellType getSpellType(@Nonnull String name) {
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
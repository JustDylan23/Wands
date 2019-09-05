package me.dylan.wands.spell;

import me.dylan.wands.spell.spells.*;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public enum SpellType {
    BLOOD_BLOCK(new BloodBlock()),
    BLOOD_EXPLODE(new BloodExplode()),
    BLOOD_SPARK(new BloodSpark()),
    BLOOD_STUN(new BloodStun()),
    BLOOD_WAVE(new BloodWave()),
    COMET(new Comet()),
    CONFUSE(new Confuse()),
    CORRUPTED_LAUNCH(new CorruptedLaunch()),
    CORRUPTED_RAIN(new CorruptedRain()),
    CORRUPTED_SHOCK_WAVE(new CorruptedShockWave()),
    CORRUPTED_SPARK(new CorruptedSpark()),
    CORRUPTED_WAVE(new CorruptedWave()),
    CORRUPTED_WOLFS(new CorruptedWolves()),
    DARK_AURA(new DarkAura()),
    DARK_BLOCK(new DarkBlock()),
    DARK_CIRCLE(new DarkCircle()),
    DARK_PULSE(new DarkPulse()),
    DARK_PUSH(new DarkPush()),
    DARK_SPARK(new DarkSpark()),
    ESCAPE(new Escape()),
    FIRE_COMET(new FireComet()),
    FIRE_SPARK(new FireSpark()),
    FIRE_TWISTER(new FireTwister()),
    FLAME_SHOCK_WAVE(new FlameShockWave()),
    FLAME_THROWER(new FlameThrower()),
    FLAME_WAVE(new FlameWave()),
    ICE_AURA(new IceAura()),
    ICE_FREEZE(new Freeze()),
    LAUNCH(new Launch()),
    MEPHI_AURA(new MephiAura()),
    MEPHI_AWAY(new MephiAway()),
    MEPHI_CHOKE(new MephiChoke()),
    MEPHI_GRAB_WAVE(new MephiGrabWave()),
    MEPHI_SPARK(new MephiSpark()),
    POISON_WAVE(new PoisonWave()),
    SPARK(new MagicSpark()),
    THUNDER_ARROW(new LightningArrow()),
    THUNDER_RAGE(new ThunderRage()),
    THUNDER_STORM(new ThunderStorm()),
    THUNDER_STRIKE(new ThunderStrike()),

    SPIRIT_THRUST(new SpiritThrust()),
    SPIRIT_FURY(new SpiritFury()),

    ONE_MIND(new OneMind()),
    DUAL_DRAW(new DualDraw()),
    WHIRLWIND_SLASH(new WhirlwindSlash()),
    FLOATING_PASSAGE(new FloatingPassage()),
    SPIRAL_CLOUD_PASSAGE(new SpiralCloudPassage());

    public final SpellData spellData;

    SpellType(@NotNull SpellData spellData) {
        this.spellData = spellData;
    }

    @Nullable
    public static SpellType getSpellType(@NotNull String name) {
        try {
            return valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public String getName() {
        return spellData.getDisplayName();
    }
}
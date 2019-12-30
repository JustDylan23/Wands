package me.dylan.wands.spell;

import me.dylan.wands.spell.spellbuilders.Behavior;
import me.dylan.wands.spell.spells.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public enum SpellType {
    BLOOD_BLOCK(1, new BloodBlock()),
    BLOOD_EXPLODE(2, new BloodExplode()),
    BLOOD_SPARK(3, new BloodSpark()),
    BLOOD_STUN(4, new BloodStun()),
    BLOOD_WAVE(5, new BloodWave()),
    COMET(6, new Comet()),
    CONFUSE(7, new Confuse()),
    CORRUPTED_LAUNCH(8, new CorruptedLaunch()),
    CORRUPTED_RAIN(9, new CorruptedRain()),
    CORRUPTED_SHOCK_WAVE(10, new CorruptedShockWave()),
    CORRUPTED_SPARK(11, new CorruptedSpark()),
    CORRUPTED_WAVE(12, new CorruptedWave()),
    CORRUPTED_WOLFS(13, new CorruptedWolves()),
    DARK_AURA(14, new DarkAura()),
    DARK_BLOCK(15, new DarkBlock()),
    DARK_CIRCLE(16, new DarkCircle()),
    DARK_PULSE(17, new DarkPulse()),
    DARK_PUSH(18, new DarkPush()),
    DARK_SPARK(19, new DarkSpark()),
    ESCAPE(20, new Escape()),
    FIRE_COMET(21, new FireComet()),
    FIRE_SPARK(22, new FireSpark()),
    FIRE_TWISTER(23, new FireTwister()),
    FLAME_SHOCK_WAVE(24, new FlameShockWave()),
    FLAME_THROWER(25, new FlameThrower()),
    FLAME_WAVE(26, new FlameWave()),
    ICE_AURA(27, new IceAura()),
    ICE_FREEZE(28, new Freeze()),
    LAUNCH(29, new Launch()),
    MEPHI_AURA(30, new MephiAura()),
    MEPHI_AWAY(31, new MephiAway()),
    MEPHI_CHOKE(32, new MephiChoke()),
    MEPHI_GRAB_WAVE(33, new MephiGrabWave()),
    MEPHI_SPARK(34, new MephiSpark()),
    POISON_WAVE(35, new PoisonWave()),
    SPARK(36, new MagicSpark()),
    THUNDER_ARROW(37, new LightningArrow()),
    THUNDER_RAGE(38, new ThunderRage()),
    THUNDER_STORM(39, new ThunderStorm()),
    THUNDER_STRIKE(40, new ThunderStrike()),

    SPIRIT_THRUST(41, new SpiritThrust()),
    SPIRIT_FURY(42, new SpiritFury()),

    ONE_MIND(43, new OneMind()),
    DUAL_DRAW(44, new DualDraw()),
    WHIRLWIND_SLASH(45, new WhirlwindSlash()),
    FLOATING_PASSAGE(46, new FloatingPassage()),
    SPIRAL_CLOUD_PASSAGE(47, new SpiralCloudPassage());

    private static final Map<Integer, SpellType> SPELL_TYPE_MAP = new HashMap<>();

    static {
        for (SpellType value : values()) {
            SPELL_TYPE_MAP.put(value.id, value);
        }
    }

    public final Behavior behavior;
    public final String name;
    public final int id;

    SpellType(int id, @NotNull Castable castable) {
        this.id = id;
        this.behavior = castable.createBehaviour();
        this.name = castable.getDisplayName();
    }

    @Nullable
    public static SpellType getSpellById(int id) {
        return SPELL_TYPE_MAP.get(id);
    }

    @Nullable
    public static SpellType fromString(@NotNull String name) {
        try {
            return valueOf(name.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public String getDisplayName() {
        return name;
    }
}
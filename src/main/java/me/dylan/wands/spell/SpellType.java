package me.dylan.wands.spell;

import me.dylan.wands.spell.implementations.bloodmagic.*;
import me.dylan.wands.spell.implementations.commonmagic.*;
import me.dylan.wands.spell.implementations.darkmagic.*;
import me.dylan.wands.spell.implementations.firemagic.*;
import me.dylan.wands.spell.implementations.icemagic.*;

import java.util.NoSuchElementException;
import java.util.Optional;

public enum SpellType {
    COMET(Comet.INSTANCE),
    SPARK(Spark.INSTANCE),
    LAUNCH(Launch.INSTANCE),
    CONFUSE(Confuse.INSTANCE),
    POISON_WAVE(PoisonWave.INSTANCE),

    BLOOD_BLOCK(BloodBlock.INSTANCE),
    BLOOD_SPARK(BloodSpark.INSTANCE),
    BLOOD_WAVE(BloodWave.INSTANCE),
    BLOOD_EXPLODE(BloodExplode.INSTANCE),
    BLOOD_STUN(BloodStun.INSTANCE),

    ICE_FREEZE(Freeze.INSTANCE),
    ICE_AURA(IceAura.INSTANCE),

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
    DARK_SPARK(DarkSpark.INSTANCE);

    public final Castable castable;

    SpellType(Castable castable) {
        this.castable = castable;
    }

    public static Optional<Castable> getSpell(String name) {
        if (name == null) return Optional.empty();
        try {
            return Optional.of(SpellType.valueOf(name).castable);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public static Castable[] getSpells(String unparsedArray) throws NoSuchElementException {
        String[] parsedKeys = unparsedArray.split(";");
        Castable[] castables = new Castable[parsedKeys.length];
        for (int i = 0; i < parsedKeys.length; i++) {
            String spellName = parsedKeys[i];
            castables[i] = getSpell(spellName).orElseThrow(() ->
                    new NoSuchElementException("Castable with identifier " + spellName + " is not declared!"));
        }
        return castables;
    }

    public String getName() {
        return castable.getDisplayName();
    }
}
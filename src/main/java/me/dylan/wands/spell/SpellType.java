package me.dylan.wands.spell;

import me.dylan.wands.spell.implementations.bloodmagic.*;
import me.dylan.wands.spell.implementations.commonmagic.*;
import me.dylan.wands.spell.implementations.icemagic.*;

import java.util.NoSuchElementException;
import java.util.Optional;

public enum SpellType {
    COMET(Comet.INSTANCE),
    SPARK(new Spark()),
    LAUNCH(new Launch()),
    CONFUSE(new Confuse()),
    POISON_WAVE(new PoisonWave()),
    BLOOD_BLOCK(new BloodBlock()),
    BLOOD_SPARK(new BloodSpark()),
    BLOOD_WAVE(new BloodWave()),
    BLOOD_EXPLODE(new BloodExplode()),
    BLOOD_STUN(new BloodStun()),
    ICE_FREEZE(new Freeze()),
    ICE_AURA(new IceAura()),
    THUNDER_ARROW(new LightningArrow()),
    THUMDER_RAGE(new ThunderRage()),
    THUNDER_STORM(new ThunderStorm()),
    THUNDER_STRIKE(new ThunderStrike());


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
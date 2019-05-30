package me.dylan.wands.spell;

import me.dylan.wands.spell.implementation.*;

import java.util.NoSuchElementException;
import java.util.Optional;

public enum Spell {
    COMET(new Comet()),
    SPARK(new Spark()),
    LAUNCH(new Launch()),
    CONFUSE(new Confuse()),
    POISON_WAVE(new PoisonWave()),
    BLOOD_SPARK(new BloodSpark()),
    BLOOD_WAVE(new BloodWave()),
    BLOOD_EXPLODE(new BloodExplode()),
    BLOOD_STUN(new BloodStun());

    public final BaseSpell spell;

    Spell(BaseSpell spell) {
        this.spell = spell;
    }

    public String getName() {
        return spell.getName();
    }

    public static Optional<BaseSpell> getSpell(String name) {
        if (name == null) return Optional.empty();
        try {
            return Optional.of(Spell.valueOf(name).spell);
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }

    public static BaseSpell[] getSpells(String unparsedArray) throws NoSuchElementException {
        String[] parsedKeys = unparsedArray.split(",");
        BaseSpell[] spells = new BaseSpell[parsedKeys.length];
        for (int i = 0; i < parsedKeys.length; i++) {
            String spellName = parsedKeys[i];
            spells[i] = getSpell(spellName).orElseThrow(() ->
                    new NoSuchElementException("BaseSpell with identifier " + spellName + " is not registered!"));
        }
        return spells;
    }
}
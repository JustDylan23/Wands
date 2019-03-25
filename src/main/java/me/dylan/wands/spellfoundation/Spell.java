package me.dylan.wands.spellfoundation;

import me.dylan.wands.spells.*;

public enum Spell {
    COMET(new Comet()),
    SPARK(new Spark()),
    LAUNCH(new Launch()),
    CONFUSE(new Confuse()),
    POISON_WAVE(new PoisonWave()),
    BLOOD_SPARK(new BloodSpark()),
    BLOOD_WAVE(new BloodWave());

    private final CastableSpell spell;

    Spell(CastableSpell spell) {
        this.spell = spell;
    }

    public int getId() {
        return spell.getId();
    }

    public CastableSpell getInstance() {
        return spell;
    }

    @Override
    public String toString() {
        return spell.getName();
    }
}
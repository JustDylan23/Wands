package me.dylan.wands;

import me.dylan.wands.spells.*;

public enum Spell {
    COMET(new Comet()),
    SPARK(new Spark()),
    LAUNCH(new Launch()),
    CONFUSE(new Confuse()),
    POISON_WAVE(new PoisonWave()),
    SPARK_BLOOD(new BloodSpark());

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
}

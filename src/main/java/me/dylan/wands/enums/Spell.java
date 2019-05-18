package me.dylan.wands.enums;

import me.dylan.wands.spellfoundation.CastableSpell;
import me.dylan.wands.spells.*;

import java.lang.reflect.InvocationTargetException;

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

    public static CastableSpell spell(String className) {
        Class<?> clazz;
        try {
            clazz = Class.forName("me.dylan.wands.spells." + className);
        } catch (ClassNotFoundException e) {
            throw new NoSuchSpellException("Spell does not exist");
        }
        try {
            return (CastableSpell) clazz.getConstructor((Class<?>) null).newInstance((Object[]) null);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        throw new NoSuchSpellException("Spell does not exist");
    }

    public static class NoSuchSpellException extends RuntimeException {
        public NoSuchSpellException(String message) {
            super(message);
        }
    }
}
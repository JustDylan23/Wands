package me.dylan.wands.spell.implementations.corruptedmagic;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.handler.Behaviour;

public enum CorruptedSpark implements Castable {
    INSTANCE;

    private final Behaviour behaviour;

    CorruptedSpark() {
        this.behaviour = null;
    }

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }
}
package me.dylan.wands.spell.implementations.corruptedmagic;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.handler.Behaviour;

public enum CorruptedRain implements Castable {
    INSTANCE;

    private final Behaviour behaviour;

    CorruptedRain() {
        this.behaviour = null;
    }

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }
}
package me.dylan.wands.spell.implementations;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.handler.Behaviour;

public enum CorruptedWolfs implements Castable {
    INSTANCE;

    private final Behaviour behaviour;

    CorruptedWolfs() {
        this.behaviour = null;
    }

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }
}

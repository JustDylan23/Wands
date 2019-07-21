package me.dylan.wands.spell.implementations;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.handler.Behaviour;

public enum CorruptedLaunch implements Castable {
    INSTANCE;

    private final Behaviour behaviour;

    CorruptedLaunch() {
        this.behaviour = null;
    }

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }
}
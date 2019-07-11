package me.dylan.wands.spell.implementations.corruptedmagic;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.handler.Behaviour;

public enum CorruptedShockWave implements Castable {
    INSTANCE;

    private final Behaviour behaviour;

    CorruptedShockWave() {
        this.behaviour = null;
    }

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }
}

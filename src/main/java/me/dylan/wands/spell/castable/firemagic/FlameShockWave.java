package me.dylan.wands.spell.castable.firemagic;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.handler.Behaviour;

public class FlameShockWave implements Castable {

    private static Behaviour behaviour;

    static {
        behaviour = null;
    }

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }
}
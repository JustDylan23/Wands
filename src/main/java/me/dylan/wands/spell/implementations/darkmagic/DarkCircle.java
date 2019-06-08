package me.dylan.wands.spell.implementations.darkmagic;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.handler.Behaviour;

public class DarkCircle implements Castable {

    private static Behaviour behaviour;

    static {
        behaviour = null;
    }

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }
}
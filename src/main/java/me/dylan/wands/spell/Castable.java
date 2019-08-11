package me.dylan.wands.spell;

import me.dylan.wands.spell.types.Behaviour;

@FunctionalInterface
public interface Castable {
    default String getDisplayName() {
        return getClass().getSimpleName();
    }


    Behaviour getBehaviour();
}
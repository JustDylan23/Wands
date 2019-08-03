package me.dylan.wands.spell;

import me.dylan.wands.spell.types.Base;

@FunctionalInterface
public interface Castable {
    default String getDisplayName() {
        return getClass().getSimpleName();
    }


    Base getBaseType();
}
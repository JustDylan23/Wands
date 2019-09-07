package me.dylan.wands.spell;

import me.dylan.wands.spell.types.Behavior;

@FunctionalInterface
public interface Castable {
    default String getDisplayName() {
        return getClass().getSimpleName();
    }

    Behavior createBehaviour();
}
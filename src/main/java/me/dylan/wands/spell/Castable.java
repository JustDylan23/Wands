package me.dylan.wands.spell;

import me.dylan.wands.spell.handler.Behaviour;

import javax.annotation.Nullable;

@FunctionalInterface
public interface Castable {
    default String getDisplayName() {
        return getClass().getSimpleName();
    }

    @Nullable
    Behaviour getBehaviour();
}
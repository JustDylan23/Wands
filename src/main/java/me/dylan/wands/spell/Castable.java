package me.dylan.wands.spell;

import me.dylan.wands.spell.spellbuilders.Behavior;
import me.dylan.wands.utils.Common;

@FunctionalInterface
public interface Castable {
    default String getDisplayName() {
        return Common.pascalCaseToWords(getClass().getSimpleName());
    }

    Behavior createBehaviour();
}
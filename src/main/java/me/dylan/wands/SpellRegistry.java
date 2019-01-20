package me.dylan.wands;

import java.util.HashMap;
import java.util.Map;

public final class SpellRegistry {

    private final Map<Integer, Spell> spellRegister = new HashMap<>();

    public void registerSpell(int index, Spell spell) {
        if (spellRegister.containsKey(index)) {
            throw new IllegalArgumentException("A spell with index: " + index + " has already been registered");
        }
        spellRegister.put(index, spell);
    }

    public Spell getSpell(int index) {
        return spellRegister.get(index);
    }
}
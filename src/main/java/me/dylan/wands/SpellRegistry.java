package me.dylan.wands;

import java.util.HashMap;
import java.util.Map;

public final class SpellRegistry {

    private final Map<Integer, Spell> spellRegister = new HashMap<>();

    public void registerSpell(int index, Spell spell) {
        if (spellRegister.containsKey(index)) {
            throw new IllegalArgumentException("Spell with index: " + index + " has already been registered!");
        }
        spellRegister.put(index, spell);
    }

    public Spell getSpell(int index) {
        Spell spell = spellRegister.get(index);
        if (spell == null) throw new NullPointerException("Spell with is index " + index + " not registered!");
        return spell;
    }
}
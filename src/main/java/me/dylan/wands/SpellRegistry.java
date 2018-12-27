package me.dylan.wands;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class SpellRegistry {

    private final Map<Integer, Spell> spellRegister = new HashMap<>();

    public void registerSpell(int index, Spell spell) {
        if (spellRegister.containsKey(index)) {
            throw new IllegalArgumentException("A spell with index: " + index + " has already been registered");
        }
        spellRegister.put(index, spell);
        Wands.getInstance().registerListener(spell);
    }

    public Spell getSpell(int index) {
        return spellRegister.get(index);
    }

    int size() {
        return spellRegister.size();
    }

    public final Iterable<Spell> values() {
        return Collections.unmodifiableCollection(spellRegister.values());
    }
}
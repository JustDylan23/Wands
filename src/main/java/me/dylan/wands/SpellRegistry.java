package me.dylan.wands;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SpellRegistry {

    private Map<Integer, Spell> spellRegister = new HashMap<>();

    void registerSpell(int index, Spell spell) {
        if (spellRegister.containsKey(spell)) {
            throw new IllegalArgumentException("A spell with index: " + index + " has already been registered");
        }
        spellRegister.put(index, spell);
        Wands.getInstance().registerListener(spell);
    }

    Spell getSpell(int index) {
        return spellRegister.get(index);
    }

    int size() {
        return spellRegister.size();
    }

    public final Iterable<Spell> values() {
        return Collections.unmodifiableCollection(spellRegister.values());
    }
}
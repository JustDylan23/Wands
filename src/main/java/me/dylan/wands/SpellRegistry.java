package me.dylan.wands;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SpellRegistry {

    private Map<Integer, Spell> spellRegister = new HashMap();

    public void registerSpell(int index, Spell spell) {
        spellRegister.put(index, spell);
    }

    public Spell getSpell(int index) {
        return spellRegister.get(index);
    }

    public int size() {
        return spellRegister.size();
    }

    public final Iterable<Spell> values() {
        return Collections.unmodifiableCollection(spellRegister.values());
    }
}
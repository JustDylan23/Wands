package me.dylan.wands;

import java.util.HashMap;
import java.util.Map;

public final class SpellRegistry {

    private final Map<Integer, CastableSpell> spellRegister = new HashMap<>();

    public void registerSpells(Spell... spells) {
        for (Spell spell : spells) {
            spellRegister.put(spell.getId(), spell.getInstance());
        }
    }

    public CastableSpell getSpell(int index) {
        CastableSpell castableSpell = spellRegister.get(index);
        if (castableSpell == null) {
            throw new NullPointerException("CastableSpell with is index " + index + " not registered!");
        }
        return castableSpell;
    }
}
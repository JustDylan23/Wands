package me.dylan.wands.spell.meta;

import me.dylan.wands.spell.model.CastableSpell;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class SpellRegistry {

    private final Map<String, CastableSpell> spellRegister = new HashMap<>();

    public void loadSpells() {
        Spell[] spells = Spell.values();
        for (Spell spell : spells) {
            spellRegister.put(spell.toString(), spell.getInstance());
        }
    }

    public CastableSpell getSpell(String identifier) throws NoSuchElementException {
        CastableSpell castableSpell = spellRegister.get(identifier);
        if (castableSpell == null) {
            throw new NoSuchElementException("CastableSpell with identifier " + identifier + " is not registered!");
        }
        return castableSpell;
    }

    public CastableSpell[] getSpells(String rawArray) {
        String[] spellKeys = rawArray.split(",");
        CastableSpell[] spells = new CastableSpell[spellKeys.length];
        for (int i = 0; i < spellKeys.length; i++) {
            CastableSpell spell = spellRegister.get(spellKeys[i]);
            if (spell == null) {
                throw new NoSuchElementException("CastableSpell with identifier " + spellKeys[i] + " is not registered!");
            }
            spells[i] = spell;
        }
        return spells;
    }
}
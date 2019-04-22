package me.dylan.wands.spellfoundation;

import me.dylan.wands.Wands;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public class SpellRegistry {

    private final Map<Integer, CastableSpell> spellRegister = new HashMap<>();

    public SpellRegistry() {
        Wands.getPlugin().sendConsole("trying to create instance of registry");
    }

    public void loadSpells() {
        if (!spellRegister.isEmpty()) {
            throw new IllegalStateException("The plugin executed loadSpells twice");
        }
        Spell[] spells = Spell.values();
        for (Spell spell : spells) {
            spellRegister.put(spell.getId(), spell.getInstance());
        }
    }

    public CastableSpell getSpell(int index) throws NoSuchElementException {
        CastableSpell castableSpell = spellRegister.get(index);
        if (castableSpell == null) {
            throw new NoSuchElementException("CastableSpell with is index " + index + " not registered!");
        }
        return castableSpell;
    }
}
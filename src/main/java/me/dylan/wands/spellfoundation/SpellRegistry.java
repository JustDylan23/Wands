package me.dylan.wands.spellfoundation;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public final class SpellRegistry {

    private final Map<Integer, CastableSpell> spellRegister = new HashMap<>();

    public void loadSpells() {
        if (!spellRegister.isEmpty()) {
            throw new AlreadyLoadedException();
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

    private static class AlreadyLoadedException extends RuntimeException {
        private AlreadyLoadedException() {
            super("The plugin executed loadSpells twice");
        }
    }
}
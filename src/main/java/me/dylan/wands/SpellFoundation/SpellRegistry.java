package me.dylan.wands.SpellFoundation;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

public final class SpellRegistry {

    private static boolean isLoaded = false;
    private final Map<Integer, CastableSpell> spellRegister = new HashMap<>();

    public void loadSpells() {
        if (isLoaded) {
            throw new AlreadyLoadedException();
        }
        isLoaded = true;
        Spell[] spells = Spell.values();
        for (Spell spell : spells) {
            spellRegister.put(spell.getId(), spell.getInstance());
        }
    }

    public CastableSpell getSpell(int index) {
        CastableSpell castableSpell = spellRegister.get(index);
        if (castableSpell == null) {
            throw new NoSuchElementException("CastableSpell with is index " + index + " not registered!");
        }
        return castableSpell;
    }

    @SuppressWarnings("WeakerAccess")
    public static class AlreadyLoadedException extends RuntimeException {
        private AlreadyLoadedException() {
            super("The plugin executed loadSpells twice");
        }
    }
}
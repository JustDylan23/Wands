package me.dylan.wands.spell.model;

import me.dylan.wands.Wands;
import me.dylan.wands.spell.spelltemplates.SpellBehaviour;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * @author Dylan
 * <p>
 * The CastableSpell class is the base class for every spell that can be casted.
 * The class has a couple of important features.
 * First off, it's primarily feature is to store an object with SpellBehaviour.
 * This object should contain what the CastableSpell does when casted.
 * Second is making sure the spell has an unique id
 */

public abstract class CastableSpell {

    private static int idCount;
    protected final Plugin plugin = Wands.getPlugin();
    private final String displayName;
    private final int id;
    private final SpellBehaviour spellBehaviour;

    protected CastableSpell() {
        this.spellBehaviour = getSpellBehaviour();
        this.displayName = getClass().getSimpleName();
        this.id = ++idCount;
    }

    public String getName() {
        return displayName;
    }

    public int getId() {
        return id;
    }

    protected abstract SpellBehaviour getSpellBehaviour();

    public final void cast(Player player) {
        spellBehaviour.executeSpellFrom(player);
    }
}
package me.dylan.wands.spell.model;

import me.dylan.wands.Wands;
import me.dylan.wands.spell.spellhandler.SpellBehaviour;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * @author Dylan
 * The CastableSpell class is the base class for every spell that can be casted.
 * It's primarily feature is to store an instance of an implementation SpellBehaviour.
 * This object should contain what the CastableSpell does when casted.
 */

public abstract class CastableSpell {

    protected final Plugin plugin = Wands.getPlugin();
    private final String displayName;
    private final SpellBehaviour spellBehaviour;

    protected CastableSpell() {
        this.spellBehaviour = getSpellBehaviour();
        this.displayName = getClass().getSimpleName();
    }

    public String getName() {
        return displayName;
    }

    protected abstract SpellBehaviour getSpellBehaviour();

    public final void cast(Player player) {
        spellBehaviour.executeSpellFrom(player);
    }
}
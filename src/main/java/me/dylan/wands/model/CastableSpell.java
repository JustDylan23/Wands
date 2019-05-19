package me.dylan.wands.model;

import me.dylan.wands.Wands;
import me.dylan.wands.spellbehaviour.SpellBehaviour;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

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
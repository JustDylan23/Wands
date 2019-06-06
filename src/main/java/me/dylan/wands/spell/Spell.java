package me.dylan.wands.spell;

import me.dylan.wands.Main;
import me.dylan.wands.spell.handler.Behaviour;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * @author Dylan
 * The Spell class is the base class for every spell that can be casted.
 * It's features are:
 * > Storing the name of the spell.
 * > Storing spell behaviour
 */

public abstract class Spell {

    protected static final Plugin plugin = Main.getPlugin();
    private final String displayName;
    private final Behaviour behaviour;

    protected Spell() {
        this.behaviour = getBehaviour();
        this.displayName = getClass().getSimpleName();
    }

    public String getName() {
        return displayName;
    }

    protected abstract Behaviour getBehaviour();

    public final boolean cast(Player player) {
        return behaviour.cast(player);
    }
}
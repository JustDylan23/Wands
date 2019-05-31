package me.dylan.wands.spell;

import me.dylan.wands.Main;
import me.dylan.wands.spell.behaviourhandler.BaseBehaviour;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * @author Dylan
 * The BaseSpell class is the base class for every baseSpell that can be casted.
 * It's primarily feature is to store an instance of an implementation BaseBehaviour.
 * This object should contain what the BaseSpell does when casted.
 */

public abstract class BaseSpell {

    protected final Plugin plugin = Main.getPlugin();
    private final String displayName;
    private final BaseBehaviour baseBehaviour;

    protected BaseSpell() {
        this.baseBehaviour = getBaseBehaviour();
        this.displayName = getClass().getSimpleName();
    }

    public String getName() {
        return displayName;
    }

    protected abstract BaseBehaviour getBaseBehaviour();

    public final void cast(Player player) {
        baseBehaviour.cast(player);
    }
}
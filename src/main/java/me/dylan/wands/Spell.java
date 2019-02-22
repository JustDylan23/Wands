package me.dylan.wands;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public abstract class Spell implements Listener {

    protected final Plugin plugin;

    private final String displayName;

    protected Spell(String displayName) {
        this.displayName = displayName;
        this.plugin = Wands.getInstance();
        Wands.getInstance().registerListener(this);
    }

    String getName() {
        return displayName;
    }

    protected abstract void cast(Player player);
}
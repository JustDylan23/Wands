package me.dylan.wands;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public abstract class CastableSpell implements Listener {

    protected final Plugin plugin = Wands.getInstance();
    private final String displayName;
    private final int id;
    private static int idCount;

    protected CastableSpell(String displayName) {
        this.displayName = displayName;
        this.id = ++idCount;
    }

    String getName() {
        return displayName;
    }

    public int getId() {
        return id;
    }

    protected abstract void cast(Player player);
}
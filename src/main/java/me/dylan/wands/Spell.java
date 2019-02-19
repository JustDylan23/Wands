package me.dylan.wands;

import org.bukkit.Location;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.stream.Collectors;

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

    protected final Iterable<Damageable> getNearbyDamageables(Location loc, double radius) {
        return loc.getWorld()
                .getNearbyEntities(loc, radius, radius, radius).stream()
                .filter(Damageable.class::isInstance)
                .map(Damageable.class::cast)
                .collect(Collectors.toList());
    }
}
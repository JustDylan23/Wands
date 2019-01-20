package me.dylan.wands;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
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

    protected final Location getSpellLocation(Player player, int distance) {
        Entity entity = player.getTargetEntity(distance);
        if (entity != null) {
            return entity.getLocation();
        }
        Block block = player.getTargetBlock(30);
        if (block != null) {
            return block.getLocation().toCenterLocation();
        }
        return player.getLocation().getDirection().normalize().multiply(distance).toLocation(player.getWorld()).add(player.getLocation());

    }

    protected final Iterable<Damageable> getNearbyDamageables(Location loc, double radius) {
        return loc.getWorld()
                .getNearbyEntities(loc, radius, radius, radius).stream()
                .filter(Damageable.class::isInstance)
                .map(Damageable.class::cast)
                .collect(Collectors.toList());
    }
}
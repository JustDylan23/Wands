package me.dylan.wands;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

public abstract class Spell implements Listener {

    protected final Wands plugin = Wands.getInstance();

    private final String displayName;

    protected Spell(String displayName) {
        this.displayName = displayName;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    String getName() {
        return displayName;
    }

    protected abstract void cast(Player player);

    public static void damageEntity(Entity attacker, Damageable victim, int damage) {
        if (victim.isInvulnerable()) {
            attacker.sendMessage("not vulnerable target");
            return;
        }

        if (victim instanceof Player) {
            GameMode gm = ((Player)victim).getGameMode();
            if (gm == GameMode.CREATIVE || gm == GameMode.SPECTATOR) return;
        }

        if (!victim.isDead()) {
            victim.setHealth(Math.max(victim.getHealth() - (damage - 1), 0.0));
            victim.damage(1.0, attacker);
            victim.setVelocity(new Vector(0, 0, 0));
        }
    }

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

    protected final Location getLocationInFrontOf(Entity entity, double meters) {
        Location loc = entity.getLocation();
        return loc.getDirection().normalize().multiply(meters).toLocation(loc.getWorld()).add(loc);
    }
}
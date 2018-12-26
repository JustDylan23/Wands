package me.dylan.wands;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

public abstract class Spell implements Listener {
    private final String displayName;

    public Spell(String displayName) {
        this.displayName = displayName;
    }

    protected String getName() {
        return displayName;
    }

    protected abstract void cast(Player player);

    public void damageEntity(Entity attacker, Damageable victim, int damage) {
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

    public Location getSpellLocation(Player player, int distance) {
        Entity entity = player.getTargetEntity(distance);
        if (entity != null) {
            return player.getTargetEntity(distance).getLocation();
        }
        Block block = player.getTargetBlock(30);
        if (block != null) {
            return block.getLocation().toCenterLocation();
        }
        return player.getLocation().getDirection().normalize().multiply(distance).toLocation(player.getWorld()).add(player.getLocation());

    }

    public Location getLocationInfrontOf(Entity entity, double meters) {
        Location loc = entity.getLocation();
        return loc.getDirection().normalize().multiply(meters).toLocation(loc.getWorld()).add(loc);
    }
}
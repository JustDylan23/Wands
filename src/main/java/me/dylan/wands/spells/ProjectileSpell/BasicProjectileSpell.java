package me.dylan.wands.spells.ProjectileSpell;

import me.dylan.wands.Spell;
import me.dylan.wands.Wands;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.Consumer;

public abstract class BasicProjectileSpell extends Spell {

    private final int ticksAlive;
    final String metadataTag;

    BasicProjectileSpell(String spellName, int ticksAlive, String metadataTag) {
        super(spellName);
        this.ticksAlive = ticksAlive;
        this.metadataTag = metadataTag;
    }

    public abstract void hit(Player player, Location location);

    final void trail(Projectile projectile, Consumer<Location> consumer) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (projectile.isValid()) {
                    consumer.accept(projectile.getLocation());
                } else cancel();
            }
        }.runTaskTimer(Wands.getInstance(), 1, 1);
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile entity = event.getEntity();
        if (entity.hasMetadata(metadataTag)) {
            hit((Player) entity.getShooter(), entity.getLocation());
            entity.remove();
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE && event.getDamager().hasMetadata(metadataTag)) {
            event.setCancelled(true);
        }
    }

    final void activateLifeTimer(Projectile projectile) {
        Bukkit.getScheduler().runTaskLater(Wands.getInstance(), () -> {
            if (projectile.isValid()) projectile.remove();
        }, ticksAlive);
    }
}

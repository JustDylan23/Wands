package me.dylan.wands.spells.ProjectileSpell;

import me.dylan.wands.Spell;
import me.dylan.wands.Wands;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.function.Consumer;

public abstract class BasicProjectileSpell extends Spell {

    private final int ticksAlive;
    private final String metadataTag;
    private final double speed;

    BasicProjectileSpell(String spellName, String metadataTag, int ticksAlive, double speed) {
        super(spellName);
        this.ticksAlive = ticksAlive;
        this.metadataTag = metadataTag;
        this.speed = speed;
    }

    public abstract void hit(Player player, Location location);

    final Fireball launchFireball(Player player, boolean small) {
        Vector velocity = player.getLocation().getDirection().multiply(speed);
        Fireball fireball;
        if (small) {
            fireball = player.launchProjectile(SmallFireball.class, velocity);
        } else {
            fireball = player.launchProjectile(Fireball.class, velocity);
        }
        fireball.setMetadata(metadataTag, new FixedMetadataValue(this.plugin, true));
        fireball.setIsIncendiary(false);
        activateLifeTimer(fireball);
        return fireball;
    }

    final Arrow launchArrow(Player player, boolean includeLifeTimer) {
        Vector velocity = player.getLocation().getDirection().multiply(speed);
        Arrow arrow = player.launchProjectile(Arrow.class, velocity);
        arrow.setMetadata(metadataTag, new FixedMetadataValue(this.plugin, true));
        if (includeLifeTimer) {
            activateLifeTimer(arrow);
        }
        return arrow;
    }

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

    private void activateLifeTimer(Projectile projectile) {
        Bukkit.getScheduler().runTaskLater(Wands.getInstance(), () -> {
            if (projectile.isValid()) {
                hit((Player) projectile.getShooter(), projectile.getLocation());
                projectile.remove();
            }
        }, ticksAlive);
    }
}

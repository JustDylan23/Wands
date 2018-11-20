package me.dylan.wands.spells;

import java.util.Collection;

import me.dylan.wands.Spell;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;

public final class Comet extends Spell implements Listener {
    private final Plugin plugin;

    public Comet(Plugin plugin) {
        super("Comet");
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void cast(Player player) {
        Vector velocity = player.getLocation().getDirection().multiply(2.5);
        SmallFireball fireball = player.launchProjectile(SmallFireball.class, velocity);
        fireball.setMetadata("CometFireball", new FixedMetadataValue(this.plugin, true));
        fireball.setIsIncendiary(false);
        cometParticles(fireball);

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (fireball.isValid() && !fireball.isDead()) {
                this.explodeComet(fireball, player);
            }

        }, 20L);

        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, SoundCategory.MASTER, 4.0F, 1.0F);
    }

    private void cometParticles(Fireball fireball) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (fireball.isValid() && !fireball.isDead()) {
                Location loc = fireball.getLocation();
                loc.getWorld().spawnParticle(Particle.SPELL_WITCH, loc, 40, 0.8, 0.8, 0.8, 0.15, null, true);
                loc.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 10, 0.6, 0.6, 0.6, 0.1, null, true);
                loc.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 10, 1.0, 1.0, 1.0, 0.1, null, true);
                cometParticles(fireball);
            }

        }, 1L);
    }

    @EventHandler
    private void onDamage(EntityDamageByEntityEvent event) {
        if (event.getCause() == DamageCause.PROJECTILE && event.getDamager().hasMetadata("CometFireball")) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntity().hasMetadata("CometFireball")) {
            explodeComet(event.getEntity(), (Entity)event.getEntity().getShooter());
        }
    }

    private void explodeComet(Entity projectile, Entity shooter) {
        Location loc = projectile.getLocation();
        projectile.remove();

        loc.getWorld().playSound(loc, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, SoundCategory.MASTER, 4.0F, 1.0F);
        loc.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, loc, 0, 0.0, 0.0, 0.0, 0.0, null, true);

        Collection<Entity> nearby = loc.getWorld().getNearbyEntities(loc, 4, 4, 4);
        for (Entity entity : nearby) {
            if (entity != shooter && entity instanceof Damageable) {
                this.damageEntity(shooter, (Damageable)entity, 6);
                entity.setFireTicks(60);
                Location point = loc.subtract(0.0, 1.2, 0.0);
                Location entityPos = entity.getLocation();
                Vector direction = entityPos.subtract(point).toVector().normalize();
                direction.multiply(1);
                entity.setVelocity(direction);
            }
        }

        for(int i = 0; i < 3; ++i) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                loc.getWorld().playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, SoundCategory.MASTER, 4.0F, 1.0F);
            }, (i * 5));
        }
    }
}

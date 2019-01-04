package me.dylan.wands.spells.ProjectileSpell;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.Collection;

public class Comet extends BasicProjectileSpell {
    public Comet() {
        super("Comet", 20, "CometFireball");
    }

    public void cast(Player player) {
        Vector velocity = player.getLocation().getDirection().multiply(2.5);
        Fireball fireball = player.launchProjectile(SmallFireball.class, velocity);
        activateLifeTimer(fireball);
        fireball.setMetadata(metadataTag, new FixedMetadataValue(this.plugin, true));
        fireball.setIsIncendiary(false);
        trail(fireball, loc -> {
            loc.getWorld().spawnParticle(Particle.SPELL_WITCH, loc, 40, 0.8, 0.8, 0.8, 0.15, null, true);
            loc.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 10, 0.6, 0.6, 0.6, 0.1, null, true);
            loc.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 10, 1.0, 1.0, 1.0, 0.1, null, true);
        });
    }

    public void hit(Player player, Location loc) {
        loc.getWorld().playSound(loc, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, SoundCategory.MASTER, 4.0F, 1.0F);
        loc.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, loc, 0, 0.0, 0.0, 0.0, 0.0, null, true);

        Collection<Entity> nearby = loc.getWorld().getNearbyEntities(loc, 4, 4, 4);
        for (Entity entity : nearby) {
            if (entity != player && entity instanceof Damageable) {
                player.damage(10D, entity);
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

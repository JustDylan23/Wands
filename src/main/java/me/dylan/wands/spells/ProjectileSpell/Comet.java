package me.dylan.wands.spells.ProjectileSpell;

import org.bukkit.*;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Comet extends BasicProjectileSpell {
    public Comet() {
        super("Comet", "CometFireball", 20, 2.5);
    }

    @Override
    public void cast(Player player) {
        Fireball fireball = launchFireball(player, true);
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_FIREWORK_ROCKET_BLAST, SoundCategory.MASTER, 4.0F, 1.0F);

        trail(fireball, loc -> {
            World world = loc.getWorld();
            world.spawnParticle(Particle.SPELL_WITCH, loc, 40, 0.8, 0.8, 0.8, 0.15, null, true);
            world.spawnParticle(Particle.SMOKE_LARGE, loc, 10, 0.6, 0.6, 0.6, 0.1, null, true);
            world.spawnParticle(Particle.SMOKE_LARGE, loc, 10, 1.0, 1.0, 1.0, 0.1, null, true);
        });
    }

    @Override
    public void hit(Player player, Location loc) {
        loc.getWorld().playSound(loc, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, SoundCategory.MASTER, 4.0F, 1.0F);
        loc.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, loc, 0, 0.0, 0.0, 0.0, 0.0, null, true);

        getNearbyDamageables(loc, 4).forEach(entity -> {
            player.damage(10, entity);
            entity.setFireTicks(60);
            Location point = loc.subtract(0.0, 1.2, 0.0);
            Location entityPos = entity.getLocation();
            Vector direction = entityPos.subtract(point).toVector().normalize();
            direction.multiply(1);
            entity.setVelocity(direction);
        });

        for (int i = 0; i < 3; ++i) {
            Bukkit.getScheduler().runTaskLater(plugin, () ->
                    loc.getWorld().playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, SoundCategory.MASTER, 4.0F, 1.0F), (i * 5));
        }
    }
}

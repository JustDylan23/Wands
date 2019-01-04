package me.dylan.wands.spells.SparkSpell;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.Collection;

public class Spark extends BasicSparkSpell {

    public Spark() {
        super("Spark", 30);
    }

    public void cast(Player player) {
        cast(player, loc -> {
            loc.add(0, 1, 0);

            loc.getWorld().spawnParticle(Particle.SPELL_WITCH, loc, 30, 0.6, 0.7, 0.6, 0.2, null, true);
            loc.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 50, 0.2, 0.2, 0.2, 0.08, null, true);

            loc.getWorld().playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_BLAST, SoundCategory.MASTER, 4.0F, 1.0F);
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                loc.getWorld().playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, SoundCategory.MASTER, 4.0F, 1.0F);
            }, 10L);

            Collection<Entity> nearby = loc.getWorld().getNearbyEntities(loc, 3.2, 3.2, 3.2);
            for (Entity entity : nearby) {
                if (entity instanceof Damageable && !entity.equals(player)) {
                    player.damage(10, entity);
                    entity.setVelocity(new Vector(0, 0, 0));
                }
            }
        });
    }
}

package me.dylan.wands.spells;

import me.dylan.wands.Spell;
import me.dylan.wands.Wands;
import org.bukkit.*;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import java.util.Collection;

public final class Spark extends Spell {

    public Spark() {
        super("Spark");
    }

    @Override
    public void cast(Player player) {
        Location loc = getSpellLocation(player, 30);

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
                damageEntity(player, (Damageable) entity, 8);
            }
        }
    }
}
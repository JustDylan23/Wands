package me.dylan.wands.spells.WaveSpell;

import org.bukkit.Particle;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collection;

public final class PoisonWave extends BasicWaveSpell {

    public PoisonWave() {
        super("PoisonWave", 30);
    }

    public void cast(Player player) {
        Collection<Entity> nearby = new ArrayList<>();
        nearby.add(player); //makes player immune

        cast(player, loc -> {
            loc.getWorld().spawnParticle(Particle.SPELL_MOB, loc, 15, 1, 1, 1, 0, null, true);
            loc.getWorld().spawnParticle(Particle.SMOKE_NORMAL, loc, 5, 1, 1, 1, 0.05, null, true);
            for (Entity entity : loc.getWorld().getNearbyEntities(loc, 2.2, 2.2, 2.2)) {
                if (!nearby.contains(entity) && entity instanceof Damageable) {
                    nearby.add(entity);
                    player.damage(2, entity);
                    ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.POISON, 60, 4, false));
                }
            }
        });
    }
}

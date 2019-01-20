package me.dylan.wands.spells.WaveSpell;

import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class PoisonWave extends BasicWaveSpell {

    public PoisonWave() {
        super("PoisonWave", 30);
    }

    @Override
    public void cast(Player player) {
        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.MASTER, 1, 1);
        trigger(player, loc -> {
            loc.getWorld().spawnParticle(Particle.SPELL_MOB, loc, 15, 1, 1, 1, 0, null, true);
            loc.getWorld().spawnParticle(Particle.SMOKE_NORMAL, loc, 5, 1, 1, 1, 0.05, null, true);

            getNearbyDamageables(loc, 2.2).forEach(entity -> {
                if (!entity.equals(player)) {
                    entity.damage(2, player);
                    ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.POISON, 60, 4, false));
                }
            });
        });
    }
}

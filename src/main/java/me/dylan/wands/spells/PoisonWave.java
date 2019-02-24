package me.dylan.wands.spells;

import me.dylan.wands.CastableSpell;
import me.dylan.wands.spellbehaviour.WaveSpell;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PoisonWave extends CastableSpell {

    private final WaveSpell spellBehaviour;

    @Override
    protected void cast(Player player) {
        spellBehaviour.executeFrom(player);
    }

    public PoisonWave() {
        super("PoisonWave");
        spellBehaviour = new WaveSpell.Builder()
                .setEffectDistance(30)
                .setEffectAreaRange(2.2F)
                .setEntityDamage(1)
                .setCastEffects(location -> location.getWorld().playSound(location, Sound.ENTITY_EVOKER_CAST_SPELL, 3, 1))
                .setEntityEffects(entity -> ((LivingEntity) entity).addPotionEffect(
                        new PotionEffect(PotionEffectType.POISON, 60, 4, false), true))
                .setVisualEffects(loc -> {
                    loc.getWorld().spawnParticle(Particle.SPELL_MOB, loc, 15, 1, 1, 1, 0, null, true);
                    loc.getWorld().spawnParticle(Particle.SMOKE_NORMAL, loc, 5, 1, 1, 1, 0.05, null, true);
                })
                .build();
    }
}

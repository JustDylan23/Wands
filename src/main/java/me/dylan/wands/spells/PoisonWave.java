package me.dylan.wands.spells;

import me.dylan.wands.ParticleUtil;
import me.dylan.wands.SpellFoundation.CastableSpell;
import me.dylan.wands.SpellFoundation.SpellBehaviour;
import me.dylan.wands.SpellFoundation.SpellBehaviour.BaseProperties;
import me.dylan.wands.SpellFoundation.SpellBehaviour.WaveSpell.Builder;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PoisonWave extends CastableSpell {

    public PoisonWave() {
        super("PoisonWave");
    }

    @Override
    public SpellBehaviour getSpellBehaviour() {
        BaseProperties baseProperties = SpellBehaviour.createEmptyBaseProperties()
                .setEffectAreaRange(2.2F)
                .setEntityDamage(1)
                .setCastEffects(location -> location.getWorld().playSound(location, Sound.ENTITY_EVOKER_CAST_SPELL, 3, 1))
                .setEntityEffects(entity -> ((LivingEntity) entity).addPotionEffect(
                        new PotionEffect(PotionEffectType.POISON, 60, 4, false), true))
                .setVisualEffects(loc -> {
                    ParticleUtil.spawnColoredParticle(Particle.SPELL_MOB, loc, 18, 1, 1.2, 1.2, 1.2, 75, 140, 50, false);
                    loc.getWorld().spawnParticle(Particle.SMOKE_NORMAL, loc, 5, 1, 1, 1, 0.05, null, true);
                });
        return new Builder(baseProperties).setEffectDistance(30).build();
    }
}

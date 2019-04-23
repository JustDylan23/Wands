package me.dylan.wands.spells;

import me.dylan.wands.ParticleUtil;
import me.dylan.wands.spellbehaviour.SpellBehaviour;
import me.dylan.wands.spellbehaviour.WaveSpell;
import me.dylan.wands.spellfoundation.CastableSpell;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BloodStun extends CastableSpell {
    @Override
    public SpellBehaviour getSpellBehaviour() {
        SpellBehaviour.BaseProperties baseProperties = SpellBehaviour.createEmptyBaseProperties()
                .setEffectRadius(2.2F)
                .setEntityDamage(1)
                .setCastEffects(location -> location.getWorld().playSound(location, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 4, 1))
                .setEntityEffects(entity -> ((LivingEntity) entity).addPotionEffect(
                        new PotionEffect(PotionEffectType.POISON, 60, 4, false), true))
                .setVisualEffects(loc -> {
                    ParticleUtil.spawnColoredParticle(Particle.SPELL_MOB, loc, 18, 1, 1.2, 1.2, 1.2, 75, 140, 50, false);
                    loc.getWorld().spawnParticle(Particle.SMOKE_NORMAL, loc, 5, 1, 1, 1, 0.05, null, true);
                });
        return WaveSpell.getBuilder(baseProperties).setEffectDistance(30).stopAtEntity(true).build();
    }
}

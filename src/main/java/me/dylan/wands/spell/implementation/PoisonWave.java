package me.dylan.wands.spell.implementation;

import me.dylan.wands.spell.BaseSpell;
import me.dylan.wands.spell.behaviourhandler.BaseBehaviour;
import me.dylan.wands.spell.behaviourhandler.WaveSpell;
import me.dylan.wands.util.EffectUtil;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PoisonWave extends BaseSpell {
    @Override
    public BaseBehaviour getBaseBehaviour() {
        return WaveSpell.newBuilder()
                .setEffectRadius(2.2F)
                .setEntityDamage(1)
                .setCastEffects(location -> location.getWorld().playSound(location, Sound.ENTITY_EVOKER_CAST_SPELL, 3, 1))
                .setEntityEffects(entity -> ((LivingEntity) entity).addPotionEffect(
                        new PotionEffect(PotionEffectType.POISON, 60, 4, false), true))
                .setRelativeEffects(loc -> {
                    EffectUtil.spawnColoredParticle(Particle.SPELL_MOB, loc, 18, 1.2, 1.2, 1.2, 75, 140, 50, false);
                    loc.getWorld().spawnParticle(Particle.SMOKE_NORMAL, loc, 5, 1, 1, 1, 0.05, null, true);
                }).setEffectDistance(30)
                .build();
    }
}

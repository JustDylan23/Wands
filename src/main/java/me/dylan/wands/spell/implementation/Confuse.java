package me.dylan.wands.spell.implementation;

import me.dylan.wands.spell.BaseSpell;
import me.dylan.wands.spell.behaviourhandler.BaseBehaviour;
import me.dylan.wands.spell.behaviourhandler.SparkSpell;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Confuse extends BaseSpell {
    @Override
    public BaseBehaviour getBaseBehaviour() {
        return SparkSpell.newBuilder()
                .setEffectRadius(4F)
                .setEntityDamage(6)
                .setEntityEffects(entity -> ((LivingEntity) entity).addPotionEffect(
                        new PotionEffect(PotionEffectType.CONFUSION, 240, 4, false), true))
                .setRelativeEffects(loc -> {
                    loc.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 30, 1, 1, 1, 0.08, null, true);
                    loc.getWorld().spawnParticle(Particle.SMOKE_NORMAL, loc, 30, 1, 1, 1, 0.08, null, true);
                    loc.getWorld().spawnParticle(Particle.SPELL_WITCH, loc, 30, 1, 1, 1, 0.15, null, true);
                    Bukkit.getScheduler().runTaskLater(plugin, () ->
                            loc.getWorld().playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, SoundCategory.MASTER, 4.0F, 1.0F), 10L);
                })
                .setCastEffects(loc -> loc.getWorld().playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_BLAST, SoundCategory.MASTER, 4.0F, 1.0F))
                .setEffectDistance(30)
                .build();
    }
}

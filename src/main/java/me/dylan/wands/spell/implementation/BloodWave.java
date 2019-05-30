package me.dylan.wands.spell.implementation;

import me.dylan.wands.spell.model.CastableSpell;
import me.dylan.wands.spell.spellhandler.SpellBehaviour;
import me.dylan.wands.spell.spellhandler.WaveSpell;
import me.dylan.wands.util.EffectUtil;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BloodWave extends CastableSpell {
    @Override
    public SpellBehaviour getSpellBehaviour() {
        return WaveSpell.newBuilder()
                .setEffectRadius(1.8F)
                .setEntityDamage(4)
                .setCastEffects(loc -> {
                    loc.getWorld().playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 4.0F, 1.0F);
                    EffectUtil.runTaskLater(() -> loc.getWorld().playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, 4.0F, 1.0F), 20, 5, 3);
                })
                .setEntityEffects(entity -> ((LivingEntity) entity).addPotionEffect(
                        new PotionEffect(PotionEffectType.WITHER, 60, 4, false), true))
                .setVisualEffects(loc -> {
                    loc.getWorld().spawnParticle(Particle.SMOKE_NORMAL, loc, 10, 0.2, 0.2, 0.2, 0.1, null, true);
                    loc.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 10, 1, 1, 1, 0.1, null, true);
                    loc.getWorld().spawnParticle(Particle.BLOCK_CRACK, loc, 12, 0.6, 0.6, 0.6, 0.15, Material.REDSTONE_BLOCK.createBlockData(), true);
                }).setEffectDistance(30)
                .build();
    }
}
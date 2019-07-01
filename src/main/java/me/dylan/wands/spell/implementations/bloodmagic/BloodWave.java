package me.dylan.wands.spell.implementations.bloodmagic;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.handler.Behaviour;
import me.dylan.wands.spell.handler.WaveSpell;
import me.dylan.wands.util.EffectUtil;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public enum BloodWave implements Castable {
    INSTANCE;
    private final Behaviour behaviour;

    BloodWave() {
        this.behaviour = WaveSpell.newBuilder()
                .setEffectRadius(1.8F)
                .setEntityDamage(4)
                .setCastEffects(loc -> {
                    loc.getWorld().playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_BLAST, 4.0F, 1.0F);
                    EffectUtil.runTaskLater(() -> loc.getWorld().playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, 4.0F, 1.0F), 20, 5, 3);
                })
                .setEntityEffects(entity -> entity.addPotionEffect(
                        new PotionEffect(PotionEffectType.WITHER, 60, 4, false), true))
                .setRelativeEffects(loc -> {
                    loc.getWorld().spawnParticle(Particle.SMOKE_NORMAL, loc, 10, 0.2, 0.2, 0.2, 0.1, null, true);
                    loc.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 10, 1, 1, 1, 0.1, null, true);
                    loc.getWorld().spawnParticle(Particle.BLOCK_CRACK, loc, 12, 0.6, 0.6, 0.6, 0.15, Material.REDSTONE_BLOCK.createBlockData(), true);
                }).setEffectDistance(30)
                .build();
    }

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }
}
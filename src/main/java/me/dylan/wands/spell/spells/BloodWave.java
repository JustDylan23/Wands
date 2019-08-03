package me.dylan.wands.spell.spells;

import me.dylan.wands.sound.CompoundSound;
import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.types.Base;
import me.dylan.wands.spell.types.Wave;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public enum BloodWave implements Castable {
    INSTANCE;
    private final Base baseType;
    private final PotionEffect wither = new PotionEffect(PotionEffectType.WITHER, 80, 4, false);

    BloodWave() {
        this.baseType = Wave.newBuilder()
                .setEffectDistance(30)
                .setSpellEffectRadius(2F)
                .setEntityDamage(5)
                .setCastSound(CompoundSound.chain()
                        .add(Sound.ENTITY_FIREWORK_ROCKET_BLAST)
                        .add(Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, 1, 20, 5, 3)
                )
                .setEntityEffects(entity -> entity.addPotionEffect(wither, true))
                .setSpellRelativeEffects((loc, world) -> {
                    world.spawnParticle(Particle.SMOKE_NORMAL, loc, 10, 0.2, 0.2, 0.2, 0.1, null, true);
                    world.spawnParticle(Particle.SMOKE_LARGE, loc, 10, 1, 1, 1, 0.1, null, true);
                    world.spawnParticle(Particle.BLOCK_CRACK, loc, 12, 0.6, 0.6, 0.6, 0.15, Material.REDSTONE_BLOCK.createBlockData(), true);
                })
                .build();
    }

    @Override
    public Base getBaseType() {
        return baseType;
    }
}
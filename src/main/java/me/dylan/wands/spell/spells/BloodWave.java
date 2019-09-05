package me.dylan.wands.spell.spells;

import me.dylan.wands.spell.SpellData;
import me.dylan.wands.spell.tools.sound.CompoundSound;
import me.dylan.wands.spell.types.Behavior;
import me.dylan.wands.spell.types.Wave;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BloodWave implements SpellData {
    private final Behavior behavior;
    private final PotionEffect wither = new PotionEffect(PotionEffectType.WITHER, 80, 4, false);

    public BloodWave() {
        this.behavior = Wave.newBuilder()
                .setEffectDistance(30)
                .setSpellEffectRadius(2.0F)
                .setEntityDamage(5)
                .setCastSound(CompoundSound.chain()
                        .add(Sound.ENTITY_FIREWORK_ROCKET_BLAST)
                        .addAll(Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, 1, 20, 5, 3)
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
    public Behavior getBehavior() {
        return behavior;
    }
}
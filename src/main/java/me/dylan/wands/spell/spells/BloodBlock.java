package me.dylan.wands.spell.spells;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.accessories.KnockBack;
import me.dylan.wands.spell.accessories.sound.CompoundSound;
import me.dylan.wands.spell.types.Behavior;
import me.dylan.wands.spell.types.LaunchableBlock;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;

public class BloodBlock implements Castable {
    @Override
    public Behavior createBehaviour() {
        return LaunchableBlock.newBuilder(Material.REDSTONE_BLOCK)
                .setCastSound(Sound.ENTITY_FIREWORK_ROCKET_BLAST)
                .setEntityDamage(12)
                .setSpellEffectRadius(3.5F)
                .setSpellRelativeEffects((loc, spellInfo) -> {
                    World world = spellInfo.world();
                    world.spawnParticle(Particle.BLOCK_CRACK, loc, 10, 0.5, 0.5, 0.5, 0.15, BloodSpark.BLOCK_CRACK_REDSTONE, true);
                    world.spawnParticle(Particle.SMOKE_LARGE, loc, 2, 0.2, 0.2, 0.2, 0.1, null, true);
                })
                .setKnockBack(KnockBack.EXPLOSION)
                .setBlockRelativeSounds(CompoundSound.chain()
                        .addAll(Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1, 20)
                        .addAll(Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, 1, 30, 5)
                        .addAll(Sound.ENTITY_FIREWORK_ROCKET_TWINKLE_FAR, 1, 40)
                )
                .setHitEffects(BloodExplode.BLOOD_EXPLOSION_EFFECTS)
                .build();
    }
}

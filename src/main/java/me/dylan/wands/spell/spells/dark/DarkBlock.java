package me.dylan.wands.spell.spells.dark;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.accessories.KnockBack;
import me.dylan.wands.spell.accessories.sound.CompoundSound;
import me.dylan.wands.spell.spellbuilders.Behavior;
import me.dylan.wands.spell.spellbuilders.LaunchableBlock;
import me.dylan.wands.spell.spells.AffinityType;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;

public class DarkBlock implements Castable {
    @Override
    public AffinityType[] getAffinityTypes() {
        return new AffinityType[]{AffinityType.DARK_MAGIC};
    }

    @Override
    public Behavior createBehaviour() {
        return LaunchableBlock.newBuilder(Material.COAL_BLOCK)
                .setCastSound(Sound.ENTITY_FIREWORK_ROCKET_BLAST)
                .setBlockRelativeSounds(CompoundSound.chain()
                        .addAll(Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1, 20)
                        .addAll(Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, 1, 30, 5)
                        .addAll(Sound.ENTITY_FIREWORK_ROCKET_TWINKLE_FAR, 1, 40)
                )
                .setHitEffects(((loc, spellInfo) -> {
                    World world = spellInfo.world();
                    world.spawnParticle(Particle.SMOKE_LARGE, loc, 20, 1, 1, 1, 0.1, null, true);
                    world.spawnParticle(Particle.BLOCK_CRACK, loc, 15, 1, 1, 1, 0.15, Material.COAL_BLOCK.createBlockData(), true);
                    world.spawnParticle(Particle.EXPLOSION_HUGE, loc, 0, 0.0, 0.0, 0.0, 0.0, null, true);
                    world.createExplosion(loc, 0.0f);
                }))
                .setSpellRelativeEffects((loc, spellInfo) -> {
                    World world = spellInfo.world();
                    world.spawnParticle(Particle.BLOCK_CRACK, loc, 10, 0.5, 0.5, 0.5, 0.15, Material.COAL_BLOCK.createBlockData(), true);
                    world.spawnParticle(Particle.SMOKE_LARGE, loc, 2, 0.2, 0.2, 0.2, 0.1, null, true);
                })
                .setSpellEffectRadius(3.5F)
                .setEntityDamage(12)
                .setKnockBack(KnockBack.EXPLOSION)
                .build();
    }
}

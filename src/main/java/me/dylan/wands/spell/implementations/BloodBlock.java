package me.dylan.wands.spell.implementations;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.handler.Behaviour;
import me.dylan.wands.spell.handler.MovingBlockSpell;
import me.dylan.wands.spell.spelleffect.sound.CompoundSound;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;

public enum BloodBlock implements Castable {
    INSTANCE;
    private final Behaviour behaviour;

    BloodBlock() {
        this.behaviour = MovingBlockSpell.newBuilder(Material.REDSTONE_BLOCK)
                .setCastSound(Sound.ENTITY_FIREWORK_ROCKET_BLAST)
                .setEntityDamage(12)
                .setSpellEffectRadius(3F)
                .setSpellRelativeEffects((loc, world) -> {
                    world.spawnParticle(Particle.BLOCK_CRACK, loc, 10, 0.5, 0.5, 0.5, 0.15, Material.REDSTONE_BLOCK.createBlockData(), true);
                    world.spawnParticle(Particle.SMOKE_LARGE, loc, 2, 0.2, 0.2, 0.2, 0.1, null, true);
                })
                .setImpactSpeed(0.7F)
                .setBlockRelativeSounds(CompoundSound.chain()
                        .add(Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1, 20)
                        .add(Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, 1, 30, 5)
                        .add(Sound.ENTITY_FIREWORK_ROCKET_TWINKLE_FAR, 1, 40)
                )
                .setHitEffects((loc, world) -> {
                    world.spawnParticle(Particle.SMOKE_LARGE, loc, 20, 1, 1, 1, 0.1, null, true);
                    world.spawnParticle(Particle.BLOCK_CRACK, loc, 15, 1, 1, 1, 0.15, Material.REDSTONE_BLOCK.createBlockData(), true);
                    world.spawnParticle(Particle.EXPLOSION_HUGE, loc, 0, 0.0, 0.0, 0.0, 0.0, null, true);
                    loc.createExplosion(0);
                })
                .build();
    }

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }
}

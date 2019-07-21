package me.dylan.wands.spell.implementations;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.handler.Behaviour;
import me.dylan.wands.spell.handler.MovingBlockSpell;
import me.dylan.wands.spell.spelleffect.sound.CompoundSound;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;

public enum DarkBlock implements Castable {
    INSTANCE;
    private final Behaviour behaviour;

    DarkBlock() {
        this.behaviour = MovingBlockSpell.newBuilder(Material.COAL_BLOCK)
                .setCastSound(Sound.ENTITY_FIREWORK_ROCKET_BLAST)
                .setBlockRelativeSounds(CompoundSound.chain()
                        .add(Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1, 20)
                        .add(Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, 1, 30, 5)
                        .add(Sound.ENTITY_FIREWORK_ROCKET_TWINKLE_FAR, 1, 40)
                )
                .setHitEffects(((loc, world) -> {
                    world.spawnParticle(Particle.SMOKE_LARGE, loc, 20, 1, 1, 1, 0.1, null, true);
                    world.spawnParticle(Particle.BLOCK_CRACK, loc, 15, 1, 1, 1, 0.15, Material.COAL_BLOCK.createBlockData(), true);
                    world.spawnParticle(Particle.EXPLOSION_HUGE, loc, 0, 0.0, 0.0, 0.0, 0.0, null, true);
                    loc.createExplosion(0);
                }))
                .setSpellRelativeEffects((loc, world) -> {
                    world.spawnParticle(Particle.BLOCK_CRACK, loc, 10, 0.5, 0.5, 0.5, 0.15, Material.COAL_BLOCK.createBlockData(), true);
                    world.spawnParticle(Particle.SMOKE_LARGE, loc, 2, 0.2, 0.2, 0.2, 0.1, null, true);
                })
                .setSpellEffectRadius(3F)
                .setEntityDamage(12)
                .setImpactSpeed(0.7F)
                .build();
    }

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }
}
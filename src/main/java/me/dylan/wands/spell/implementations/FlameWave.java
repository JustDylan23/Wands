package me.dylan.wands.spell.implementations;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.handler.Behaviour;
import me.dylan.wands.spell.handler.WaveSpell;
import me.dylan.wands.spell.spelleffect.sound.CompoundSound;
import org.bukkit.Particle;
import org.bukkit.Sound;

public enum FlameWave implements Castable {
    INSTANCE;
    private final Behaviour behaviour;

    FlameWave() {
        this.behaviour = WaveSpell.newBuilder()
                .setSpellEffectRadius(2.5F)
                .setEntityDamage(6)
                .setEffectDistance(20)
                .setCastSound(CompoundSound.chain()
                        .add(Sound.ENTITY_BLAZE_SHOOT)
                        .add(Sound.ENTITY_ENDER_DRAGON_FLAP, 1, 0, 3, 3, 3, 3, 3)
                )
                .setEntityEffects(entity -> entity.setFireTicks(140))
                .setSpellRelativeEffects((loc, world) -> {
                    world.spawnParticle(Particle.FLAME, loc, 10, 0.8, 0.8, 0.8, 0.1, null, true);
                    world.spawnParticle(Particle.LAVA, loc, 1, 0.8, 0.8, 0.8, 0, null, true);
                    world.spawnParticle(Particle.SMOKE_LARGE, loc, 7, 0.6, 0.6, 0.6, 0.1, null, true);
                    world.spawnParticle(Particle.SMOKE_NORMAL, loc, 7, 1, 1, 1, 0.05, null, true);
                })
                .build();
    }

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }
}
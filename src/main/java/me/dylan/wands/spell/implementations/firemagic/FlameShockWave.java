package me.dylan.wands.spell.implementations.firemagic;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.handler.Behaviour;
import me.dylan.wands.spell.handler.ShockWaveSpell;
import org.bukkit.Particle;
import org.bukkit.Sound;

public enum FlameShockWave implements Castable {
    INSTANCE;
    private final Behaviour behaviour;

    FlameShockWave() {
        this.behaviour = ShockWaveSpell.newBuilder()
                .setCastSound(Sound.ENTITY_BLAZE_SHOOT)
                .setWaveRadius(10)
                .setEntityEffects(entity -> entity.setFireTicks(120))
                .setSpellRelativeEffects((loc, world) -> {
                    world.spawnParticle(Particle.FLAME, loc, 1, 1, 1, 1, 0.1, null, true);
                    world.spawnParticle(Particle.LAVA, loc, 1, 1, 1, 1, 0, null, true);
                })
                .setExpentionDelay(3)
                .build();
    }

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }
}
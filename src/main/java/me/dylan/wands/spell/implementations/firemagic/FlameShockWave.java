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
                .setWaveRadius(8)
                .setEntityEffects(entity -> entity.setFireTicks(120))
                .setSpellRelativeEffects((loc, world) -> world.spawnParticle(Particle.LAVA, loc, 1, 0, 0, 0, 0, null, true))
                .setExpansionDelay(2)
                .build();
    }

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }
}
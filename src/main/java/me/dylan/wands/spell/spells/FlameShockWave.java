package me.dylan.wands.spell.spells;

import me.dylan.wands.spell.SpellData;
import me.dylan.wands.spell.types.Behaviour;
import me.dylan.wands.spell.types.ShockWave;
import org.bukkit.Particle;
import org.bukkit.Sound;

public class FlameShockWave implements SpellData {
    private final Behaviour behaviour;

    public FlameShockWave() {
        this.behaviour = ShockWave.newBuilder()
                .setCastSound(Sound.ENTITY_BLAZE_SHOOT)
                .setWaveRadius(8)
                .setEntityDamage(6)
                .setEntityEffects(entity -> entity.setFireTicks(140))
                .setSpellRelativeEffects((loc, world) -> world.spawnParticle(Particle.LAVA, loc, 1, 0, 0, 0, 0, null, true))
                .setExpansionDelay(2)
                .build();
    }

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }
}
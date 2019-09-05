package me.dylan.wands.spell.spells;

import me.dylan.wands.spell.SpellData;
import me.dylan.wands.spell.types.Behavior;
import me.dylan.wands.spell.types.ShockWave;
import org.bukkit.Particle;
import org.bukkit.Sound;

public class FlameShockWave implements SpellData {
    private final Behavior behavior;

    public FlameShockWave() {
        this.behavior = ShockWave.newBuilder()
                .setCastSound(Sound.ENTITY_BLAZE_SHOOT)
                .setWaveRadius(8)
                .setEntityDamage(6)
                .setEntityEffects(entity -> entity.setFireTicks(140))
                .setSpellRelativeEffects((loc, world) -> world.spawnParticle(Particle.LAVA, loc, 1, 0, 0, 0, 0, null, true))
                .setExpansionDelay(2)
                .build();
    }

    @Override
    public Behavior getBehavior() {
        return behavior;
    }
}
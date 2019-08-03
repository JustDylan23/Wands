package me.dylan.wands.spell.spells;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.types.ShockWave;
import me.dylan.wands.spell.types.Base;
import org.bukkit.Particle;
import org.bukkit.Sound;

public enum FlameShockWave implements Castable {
    INSTANCE;
    private final Base baseType;

    FlameShockWave() {
        this.baseType = ShockWave.newBuilder()
                .setCastSound(Sound.ENTITY_BLAZE_SHOOT)
                .setWaveRadius(8)
                .setEntityDamage(6)
                .setEntityEffects(entity -> entity.setFireTicks(140))
                .setSpellRelativeEffects((loc, world) -> world.spawnParticle(Particle.LAVA, loc, 1, 0, 0, 0, 0, null, true))
                .setExpansionDelay(2)
                .build();
    }

    @Override
    public Base getBaseType() {
        return baseType;
    }
}
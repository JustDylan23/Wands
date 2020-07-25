package me.dylan.wands.spell.spells.fire;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.spellbuilders.Behavior;
import me.dylan.wands.spell.spellbuilders.ShockWave;
import org.bukkit.Particle;
import org.bukkit.Sound;

public class FlameShockWave implements Castable {
    @Override
    public Behavior createBehaviour() {
        return ShockWave.newBuilder()
                .setCastSound(Sound.ENTITY_BLAZE_SHOOT)
                .setWaveRadius(8)
                .setEntityDamage(6)
                .setEntityEffects((entity, spellInfo) -> entity.setFireTicks(140))
                .setSpellRelativeEffects((loc, spellInfo) -> spellInfo.world().spawnParticle(Particle.LAVA, loc, 1, 0, 0, 0, 0, null, true))
                .setExpansionDelay(2)
                .build();
    }
}
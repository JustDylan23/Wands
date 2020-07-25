package me.dylan.wands.spell.spells.fire;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.accessories.sound.CompoundSound;
import me.dylan.wands.spell.spellbuilders.Behavior;
import me.dylan.wands.spell.spellbuilders.Wave;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;

public class FlameWave implements Castable {
    @Override
    public Behavior createBehaviour() {
        return Wave.newBuilder()
                .setSpellEffectRadius(2.5F)
                .setEntityDamage(6)
                .setEffectDistance(20)
                .setCastSound(CompoundSound.chain()
                        .add(Sound.ENTITY_BLAZE_SHOOT)
                        .addAll(Sound.ENTITY_ENDER_DRAGON_FLAP, 1, 0, 3, 3, 3, 3, 3)
                )
                .setEntityEffects((entity, spellInfo) -> entity.setFireTicks(140))
                .setSpellRelativeEffects((loc, spellInfo) -> {
                    World world = spellInfo.world();
                    world.spawnParticle(Particle.FLAME, loc, 10, 0.8, 0.8, 0.8, 0.1, null, true);
                    world.spawnParticle(Particle.LAVA, loc, 1, 0.8, 0.8, 0.8, 0, null, true);
                    world.spawnParticle(Particle.SMOKE_LARGE, loc, 7, 0.6, 0.6, 0.6, 0.1, null, true);
                    world.spawnParticle(Particle.SMOKE_NORMAL, loc, 7, 1, 1, 1, 0.05, null, true);
                })
                .build();
    }
}
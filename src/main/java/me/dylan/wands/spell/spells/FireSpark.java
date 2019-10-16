package me.dylan.wands.spell.spells;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.spellbuilders.Behavior;
import me.dylan.wands.spell.spellbuilders.BuildableBehaviour;
import me.dylan.wands.spell.spellbuilders.Spark;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;

public class FireSpark implements Castable {
    @Override
    public Behavior createBehaviour() {
        return Spark.newBuilder(BuildableBehaviour.Target.MULTI)
                .setSpellEffectRadius(2.8F)
                .setEntityDamage(9)
                .setEntityEffects((entity, spellInfo) -> entity.setFireTicks(100))
                .setSpellRelativeEffects((loc, spellInfo) -> {
                    World world = spellInfo.world();
                    world.spawnParticle(Particle.SMOKE_LARGE, loc, 10, 0.3, 0.3, 0.3, 0.1, null, true);
                    world.spawnParticle(Particle.SMOKE_NORMAL, loc, 10, 0.2, 0.2, 0.2, 0.1, null, true);
                    world.spawnParticle(Particle.FLAME, loc, 10, 0.8, 0.8, 0.8, 0.1, null, true);
                    world.spawnParticle(Particle.LAVA, loc, 10, 0.8, 0.8, 0.8, 0, null, true);
                    MagicSpark.SPARK_SOUND.play(loc);
                })
                .setCastSound(Sound.ENTITY_FIREWORK_ROCKET_BLAST)

                .setEffectDistance(30)
                .build();
    }
}
package me.dylan.wands.spell.spells.blood;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.spellbuilders.Behavior;
import me.dylan.wands.spell.spellbuilders.Spark;
import me.dylan.wands.spell.spellbuilders.Spark.Target;
import me.dylan.wands.spell.spells.AffinityType;
import me.dylan.wands.spell.spells.witch.MagicSpark;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;

public class BloodSpark implements Castable {
    @Override
    public AffinityType[] getAffinityTypes() {
        return new AffinityType[]{AffinityType.BLOOD_MAGIC};
    }

    @Override
    public Behavior createBehaviour() {
        return Spark.newBuilder(Target.MULTI)
                .setSpellEffectRadius(2.8F)
                .setEntityDamage(12)
                .setSpellRelativeEffects((loc, spellInfo) -> {
                    World world = spellInfo.world();
                    world.spawnParticle(Particle.LARGE_SMOKE, loc, 20, 0.2, 0.2, 0.2, 0.1, null, true);
                    world.spawnParticle(Particle.BLOCK, loc, 20, 0.6, 0.7, 0.6, 0.15, BloodMagicConstants.BLOCK_CRACK_REDSTONE, true);
                    MagicSpark.SPARK_SOUND.play(loc);
                })
                .setCastSound(Sound.ENTITY_FIREWORK_ROCKET_BLAST)
                .setEffectDistance(30)
                .build();
    }
}

package me.dylan.wands.spell.spells;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.spellbuilders.Behavior;
import me.dylan.wands.spell.spellbuilders.BuildableBehaviour;
import me.dylan.wands.spell.spellbuilders.Spark;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;

public class BloodSpark implements Castable {
    public static final BlockData BLOCK_CRACK_REDSTONE = Material.REDSTONE_BLOCK.createBlockData();

    @Override
    public Behavior createBehaviour() {
        return Spark.newBuilder(BuildableBehaviour.Target.MULTI)
                .setSpellEffectRadius(2.8F)
                .setEntityDamage(12)
                .setSpellRelativeEffects((loc, spellInfo) -> {
                    World world = spellInfo.world();
                    world.spawnParticle(Particle.SMOKE_LARGE, loc, 20, 0.2, 0.2, 0.2, 0.1, null, true);
                    world.spawnParticle(Particle.BLOCK_CRACK, loc, 20, 0.6, 0.7, 0.6, 0.15, BLOCK_CRACK_REDSTONE, true);
                    MagicSpark.SPARK_SOUND.play(loc);
                })
                .setCastSound(Sound.ENTITY_FIREWORK_ROCKET_BLAST)
                .setEffectDistance(30)
                .build();
    }
}

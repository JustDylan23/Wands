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

public class CorruptedSpark implements Castable {
    @Override
    public Behavior createBehaviour() {
        BlockData obsidian = Material.OBSIDIAN.createBlockData();
        return Spark.newBuilder(BuildableBehaviour.Target.MULTI)
                .setSpellEffectRadius(2.8F)
                .setEntityDamage(12)
                .setSpellRelativeEffects((loc, spellInfo) -> {
                    World world = spellInfo.world();
                    world.spawnParticle(Particle.SPELL_MOB, loc, 20, 0.6, 0.6, 0.6, 1, null, true);
                    world.spawnParticle(Particle.BLOCK_CRACK, loc, 10, 0.8, 0.3, 0.8, 0.4, obsidian, true);
                    MagicSpark.SPARK_SOUND.play(loc);
                })
                .setCastSound(Sound.ENTITY_FIREWORK_ROCKET_BLAST)
                .setEffectDistance(30)
                .build();
    }
}
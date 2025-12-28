package me.dylan.wands.spell.spells.corrupt;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.spellbuilders.Behavior;
import me.dylan.wands.spell.spellbuilders.Spark;
import me.dylan.wands.spell.spellbuilders.Spark.Target;
import me.dylan.wands.spell.spells.AffinityType;
import me.dylan.wands.spell.spells.witch.MagicSpark;
import me.dylan.wands.spell.util.SpellEffectUtil;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;

public class CorruptedSpark implements Castable {
    private final BlockData obsidian = Material.OBSIDIAN.createBlockData();

    @Override
    public AffinityType[] getAffinityTypes() {
        return new AffinityType[]{AffinityType.CORRUPTED_MAGIC};
    }

    @Override
    public Behavior createBehaviour() {
        return Spark.newBuilder(Target.SINGLE_REQUIRED)
                .setSpellEffectRadius(2.8F)
                .setEntityDamage(12)
                .setSpellRelativeEffects((loc, spellInfo) -> {
                    World world = spellInfo.world();
                    SpellEffectUtil.spawnEntityEffect(loc, 20, 0.6, 0.6, 0.6);
                    world.spawnParticle(Particle.BLOCK, loc, 10, 0.8, 0.3, 0.8, 0.4, obsidian, true);
                    MagicSpark.SPARK_SOUND.play(loc);
                })
                .setCastSound(Sound.ENTITY_FIREWORK_ROCKET_BLAST)
                .setEffectDistance(30)
                .build();
    }
}
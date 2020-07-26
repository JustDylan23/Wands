package me.dylan.wands.spell.spells.dark;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.spellbuilders.Behavior;
import me.dylan.wands.spell.spellbuilders.Spark;
import me.dylan.wands.spell.spellbuilders.Spark.Target;
import me.dylan.wands.spell.spells.AffinityType;
import me.dylan.wands.spell.spells.witch.MagicSpark;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;

public class DarkSpark implements Castable {
    @Override
    public AffinityType[] getAffinityTypes() {
        return new AffinityType[]{AffinityType.DARK_MAGIC};
    }

    @Override
    public Behavior createBehaviour() {
        return Spark.newBuilder(Target.MULTI)
                .setSpellEffectRadius(2.8F)
                .setEntityDamage(12)
                .setSpellRelativeEffects((loc, spellInfo) -> {
                    World world = spellInfo.world();
                    world.spawnParticle(Particle.SMOKE_LARGE, loc, 20, 0.4, 0.4, 0.4, 0.1, null, true);
                    world.spawnParticle(Particle.SMOKE_NORMAL, loc, 20, 0.4, 0.4, 0.4, 0.1, null, true);
                    world.spawnParticle(Particle.ENCHANTMENT_TABLE, loc, 40, 0.8, 0.8, 0.8, 0.3, null, true);
                    MagicSpark.SPARK_SOUND.play(loc);
                })
                .setCastSound(Sound.ENTITY_FIREWORK_ROCKET_BLAST)
                .setEffectDistance(30)
                .build();
    }
}
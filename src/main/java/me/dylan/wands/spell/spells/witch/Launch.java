package me.dylan.wands.spell.spells.witch;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.spellbuilders.Behavior;
import me.dylan.wands.spell.spellbuilders.BuildableBehaviour.Target;
import me.dylan.wands.spell.spellbuilders.Phase;
import me.dylan.wands.spell.spells.AffinityType;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class Launch implements Castable {
    @Override
    public AffinityType[] getAffinityTypes() {
        return new AffinityType[]{AffinityType.WITCH_MAGIC};
    }

    @Override
    public Behavior createBehaviour() {
        return Phase.newBuilder(Target.MULTI)
                .setSpellEffectRadius(2.8f)
                .setEntityDamage(7)
                .setEntityEffects((entity, spellInfo) -> entity.setVelocity(new Vector(0, 1.2, 0)))
                .setSpellRelativeEffects((loc, spellInfo) -> {
                    World world = spellInfo.world();
                    world.spawnParticle(Particle.WITCH, loc, 30, 0.6, 0.7, 0.6, 0.4, null, true);
                    world.spawnParticle(Particle.LARGE_SMOKE, loc, 20, 0.2, 0.2, 0.2, 0.2, null, true);
                    world.playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_BLAST, SoundCategory.MASTER, 4.0F, 1.0F);
                    MagicSpark.SPARK_SOUND.play(loc);
                })
                .setStagePassCondition(Entity::isOnGround)
                .setEffectsDuringPhase(entity -> {
                    World world = entity.getWorld();
                    Location location = entity.getLocation();
                    world.spawnParticle(Particle.LARGE_SMOKE, location, 1, 0, 0, 0, 0.3, null, true);
                    world.spawnParticle(Particle.PORTAL, location, 10, 0.4, 0.4, 0.4, 0, null, true);
                })
                .setEffectDistance(30)
                .build();
    }
}

package me.dylan.wands.spell.spells.corrupt;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.spellbuilders.Behavior;
import me.dylan.wands.spell.spellbuilders.BuildableBehaviour.Target;
import me.dylan.wands.spell.spellbuilders.Phase;
import me.dylan.wands.spell.spells.witch.MagicSpark;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class CorruptedLaunch implements Castable {
    @Override
    public Behavior createBehaviour() {
        return Phase.newBuilder(Target.MULTI)
                .setSpellEffectRadius(2.8f)
                .setEntityDamage(7)
                .setEntityEffects((entity, spellInfo) -> entity.setVelocity(new Vector(0, 1.2, 0)))
                .setSpellRelativeEffects((loc, spellInfo) -> {
                    World world = spellInfo.world();
                    world.spawnParticle(Particle.SMOKE_NORMAL, loc, 15, 0.5, 0.5, 0.5, 0.05, null, true);
                    world.spawnParticle(Particle.SPELL_MOB, loc, 10, 1, 1, 1, 1, null, true);
                    world.spawnParticle(Particle.SPELL_MOB_AMBIENT, loc, 30, 1, 1, 1, 1, null, true);
                    world.playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_BLAST, SoundCategory.MASTER, 4.0F, 1.0F);
                    MagicSpark.SPARK_SOUND.play(loc);
                })
                .setStagePassCondition(Entity::isOnGround)
                .setEffectsDuringPhase(entity -> {
                    World world = entity.getWorld();
                    Location loc = entity.getLocation();
                    world.spawnParticle(Particle.SMOKE_NORMAL, loc, 1, 0, 0, 0, 0.3, null, true);
                    world.spawnParticle(Particle.SPELL_MOB, loc, 10, 0.6, 0.6, 0.6, 1, null, true);
                    world.spawnParticle(Particle.SPELL_MOB_AMBIENT, loc, 3, 0.6, 0.6, 0.6, 1, null, true);
                })
                .setEffectDistance(30)
                .build();
    }
}

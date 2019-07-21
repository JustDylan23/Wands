package me.dylan.wands.spell.implementations.firemagic;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.handler.Behaviour;
import me.dylan.wands.spell.handler.Behaviour.ImpactCourse;
import me.dylan.wands.spell.handler.CircleSpell;
import me.dylan.wands.spell.handler.CircleSpell.CirclePlacement;
import org.bukkit.Particle;
import org.bukkit.Sound;

public enum FireTwister implements Castable {
    INSTANCE;
    private final Behaviour behaviour;

    FireTwister() {
        this.behaviour = CircleSpell.newBuilder(CirclePlacement.TARGET)
                .setCircleRadius(3)
                .setEntityDamage(2)
                .setSpellEffectRadius(3F)
                .setEffectDistance(30)
                .setEntityEffects(entity -> {
                    entity.getLocation().createExplosion(0);
                    entity.setFireTicks(40);
                })
                .setMetersPerTick(2)
                .setCastSound(Sound.ENTITY_BLAZE_SHOOT)
                .setSpellRelativeEffects((loc, world) -> {
                    world.spawnParticle(Particle.SMOKE_LARGE, loc, 5, 0.3, 0.3, 0.3, 0.1, null, true);
                    world.spawnParticle(Particle.FLAME, loc, 2, 0.3, 0.3, 0.3, 0.1, null, true);
                    world.spawnParticle(Particle.LAVA, loc, 2, 0.3, 0.3, 0.3, 0, null, true);
                })
                .setImpactSpeed(1.1F)
                .setImpactCourse(ImpactCourse.PLAYER)
//                .setCircleHeight(1)
                .build();
    }

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }
}
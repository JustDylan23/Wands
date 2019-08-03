package me.dylan.wands.spell.spells;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.types.Circle;
import me.dylan.wands.spell.types.Circle.CirclePlacement;
import me.dylan.wands.spell.types.Base;
import org.bukkit.Particle;
import org.bukkit.Sound;

public enum FireTwister implements Castable {
    INSTANCE;
    private final Base baseType;

    FireTwister() {
        this.baseType = Circle.newBuilder(CirclePlacement.TARGET)
                .setCircleRadius(3)
                .setEntityDamage(7)
                .setSpellEffectRadius(3.0F)
                .setEffectDistance(30)
                .setEntityEffects(entity -> {
                    entity.setVelocity(entity.getVelocity().setY(0.6));
                    entity.getLocation().createExplosion(0.0f);
                    entity.setFireTicks(60);
                })
                .setMetersPerTick(2)
                .setCastSound(Sound.ENTITY_BLAZE_SHOOT)
                .setSpellRelativeEffects((loc, world) -> {
                    world.spawnParticle(Particle.SMOKE_LARGE, loc, 5, 0.3, 0.3, 0.3, 0.1, null, true);
                    world.spawnParticle(Particle.FLAME, loc, 2, 0.3, 0.3, 0.3, 0.1, null, true);
                    world.spawnParticle(Particle.LAVA, loc, 2, 0.3, 0.3, 0.3, 0, null, true);
                })
                .setKnockBack(1.0F, 0.5F)

                .setCircleHeight(1)
                .build();
    }

    @Override
    public Base getBaseType() {
        return baseType;
    }
}
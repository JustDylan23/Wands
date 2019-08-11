package me.dylan.wands.spell.spells;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.types.Behaviour;
import me.dylan.wands.spell.types.Circle;
import me.dylan.wands.spell.types.Circle.CirclePlacement;
import org.bukkit.Particle;
import org.bukkit.Sound;

public enum MephiAway implements Castable {
    INSTANCE;
    private final Behaviour behaviour;

    MephiAway() {
        this.behaviour = Circle.newBuilder(CirclePlacement.RELATIVE)
                .setCircleRadius(5)
                .setEntityDamage(8)
                .setSpellEffectRadius(5.0F)
                .setEntityEffects(entity -> {
                    entity.getLocation().createExplosion(0.0f);
                })
                .setMetersPerTick(3)
                .setCastSound(Sound.ENTITY_ENDER_DRAGON_FLAP)
                .setSpellRelativeEffects((loc, world) -> {
                    world.spawnParticle(Particle.SMOKE_NORMAL, loc, 5, 0.3, 0.3, 0.3, 0.1, null, true);
                    world.spawnParticle(Particle.SMOKE_LARGE, loc, 3, 0.3, 0.3, 0.3, 0.1, null, true);
                    world.spawnParticle(Particle.VILLAGER_HAPPY, loc, 2, 0.3, 0.3, 0.3, 0.1, null, true);
                })
                .setKnockBack(1.3F, 0.5F)
                .setCircleHeight(1)
                .build();
    }

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }
}

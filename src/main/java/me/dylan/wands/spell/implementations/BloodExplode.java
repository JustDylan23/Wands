package me.dylan.wands.spell.implementations;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.handler.Behaviour;
import me.dylan.wands.spell.handler.SparkSpell;
import me.dylan.wands.spell.handler.SparkSpell.Target;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;

public enum BloodExplode implements Castable {
    INSTANCE;
    private final Behaviour behaviour;

    BloodExplode() {
        this.behaviour = SparkSpell.newBuilder(Target.MULTI)
                .setSpellEffectRadius(3F)
                .setEntityDamage(10)
                .setImpactSpeed(0.7F)
                .setEntityEffects(entity -> entity.setFireTicks(80))
                .setSpellRelativeEffects((loc, world) -> {
                    world.spawnParticle(Particle.SMOKE_LARGE, loc, 20, 1, 1, 1, 0.1, null, true);
                    world.spawnParticle(Particle.BLOCK_CRACK, loc, 15, 1, 1, 1, 0.15, Material.REDSTONE_BLOCK.createBlockData(), true);
                    world.spawnParticle(Particle.EXPLOSION_HUGE, loc, 0, 0.0, 0.0, 0.0, 0.0, null, true);
                    loc.createExplosion(0);
                })
                .setCastSound(Sound.ENTITY_FIREWORK_ROCKET_BLAST)
                .setEffectDistance(30)
                .build();
    }

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }
}

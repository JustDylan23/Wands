package me.dylan.wands.spell.implementations;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.handler.Behaviour;
import me.dylan.wands.spell.handler.BlockProjectileSpell;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;

public enum FlameThrower implements Castable {
    INSTANCE;
    private final Behaviour behaviour;

    FlameThrower() {
        this.behaviour = BlockProjectileSpell.newBuilder(Material.FIRE, 2F)
                .setCastSound(Sound.ENTITY_BLAZE_SHOOT)
                .setProjectileAmount(8)
                .setProjectileShootDelay(3)
                .setEntityEffects(entity -> entity.setFireTicks(100))
                .setSpellEffectRadius(3F)
                .setSpellRelativeEffects((loc, world) -> {
                    world.spawnParticle(Particle.FLAME, loc, 2, 0.8, 0.8, 0.8, 0.1, null, true);
                    world.spawnParticle(Particle.SMOKE_LARGE, loc, 1, 0.3, 0.3, 0.3, 0.1, null, true);
                })
                .build();
    }

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }
}
package me.dylan.wands.spell.implementations;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.handler.Behaviour;
import me.dylan.wands.spell.handler.ProjectileSpell;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;

public enum LightningArrow implements Castable {
    INSTANCE;
    private final Behaviour behaviour;

    LightningArrow() {
        this.behaviour = ProjectileSpell.newBuilder(Arrow.class, 2.2F)
                .setHitEffects((loc, world) -> {
                    world.strikeLightningEffect(loc);
                    world.playSound(loc, Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 4, 1);
                    world.spawnParticle(Particle.CLOUD, loc, 40, 0.2, 0.2, 0.2, 0.3, null, true);
                })
                .setSpellEffectRadius(3.5F)
                .setLifeTime(25)
                .setSpellRelativeEffects((loc, world) -> {
                    world.spawnParticle(Particle.CLOUD, loc, 10, 0.2, 0.2, 0.2, 0.1, null, true);
                    world.spawnParticle(Particle.ENCHANTMENT_TABLE, loc, 20, 0.5, 0.5, 0.5, 1, null, true);
                })
                .setEntityDamage(6)
                .setEntityEffects(entity -> entity.setFireTicks(80))
                .setCastSound(Sound.ENTITY_WITHER_SHOOT)
                .build();
    }

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }

    @Override
    public String getDisplayName() {
        return "Lightning Arrow";
    }
}
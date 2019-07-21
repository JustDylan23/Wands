package me.dylan.wands.spell.implementations;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.SpellEffectUtil;
import me.dylan.wands.spell.handler.Behaviour;
import me.dylan.wands.spell.handler.RaySpell;
import me.dylan.wands.spell.handler.RaySpell.Target;
import me.dylan.wands.spell.handler.SparkSpell;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;

public enum ThunderStrike implements Castable {
    INSTANCE;
    private final Behaviour behaviour;

    ThunderStrike() {
        this.behaviour = RaySpell.newBuilder(Target.MULTI)
                .setEffectDistance(40)
                .setEntityDamage(6)
                .setImpactSpeed(0.8F)
                .setCastSound(Sound.ENTITY_WITHER_SHOOT)
                .setEntityEffects(entity -> entity.setFireTicks(80))
                .setMetersPerTick(3)
                .setSpellRelativeEffects((loc, world) -> {
                    world.spawnParticle(Particle.CLOUD, loc, 5, 0.2, 0.2, 0.2, 0.05, null, true);
                    world.spawnParticle(Particle.ENCHANTMENT_TABLE, loc, 15, 0.5, 0.5, 0.5, 1, null, true);
                })
                .setHitEffects((loc, world) -> {
                    world.spawnParticle(Particle.CLOUD, loc, 40, 2, 2, 2, 0.2, null, true);
                    world.spawnParticle(Particle.SMOKE_NORMAL, loc, 20, 2, 2, 2, 0.2, null, true);
                    world.spawnParticle(Particle.SMOKE_LARGE, loc, 5, 2, 2, 2, 0.2, null, true);
                    world.spawnParticle(Particle.EXPLOSION_HUGE, loc, 0, 0.0, 0.0, 0.0, 0.0, null, true);
                    SpellEffectUtil.runTaskLater(() -> {
                        Location loc2 = SpellEffectUtil.randomizeLoc(loc, 3, 1, 3);
                        world.playSound(loc2, Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 4, 1);
                        world.strikeLightningEffect(loc2);
                    }, 1, 2, 3);
                })
                .setSpellEffectRadius(4F)
                .build();
    }

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }
}
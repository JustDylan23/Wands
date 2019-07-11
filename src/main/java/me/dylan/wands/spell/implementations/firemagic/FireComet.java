package me.dylan.wands.spell.implementations.firemagic;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.handler.Behaviour;
import me.dylan.wands.spell.handler.ProjectileSpell;
import me.dylan.wands.spell.spelleffect.sound.CompoundSound;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.SmallFireball;

public enum FireComet implements Castable {
    INSTANCE;
    private final Behaviour behaviour;

    FireComet() {
        this.behaviour = ProjectileSpell.newBuilder(SmallFireball.class, 4)
                .setCastSound(CompoundSound.chain()
                        .add(Sound.ENTITY_FIREWORK_ROCKET_BLAST)
                        .add(Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, 1, 10, 5)
                        .add(Sound.ENTITY_FIREWORK_ROCKET_TWINKLE_FAR, 1, 20)
                )
                .setSpellEffectRadius(3)
                .setEntityDamage(3)
                .setEntityEffects(entity -> entity.setFireTicks(100))
                .setImpactSpeed(1)
                .setLifeTime(20)
                .setProjectileProps(projectile -> {
                    projectile.setIsIncendiary(false);
                    projectile.setYield(0);
                })
                .setSpellRelativeEffects(loc -> {
                    World world = loc.getWorld();
                    world.spawnParticle(Particle.FLAME, loc, 6, 0.8, 0.8, 0.8, 0.1, null, true);
                    world.spawnParticle(Particle.SMOKE_NORMAL, loc, 10, 1, 1, 1, 0.05, null, true);
                    world.spawnParticle(Particle.SMOKE_LARGE, loc, 10, 0.6, 0.6, 0.6, 0.15, null, true);
                    world.spawnParticle(Particle.LAVA, loc, 5, 1, 1, 1, 0, null, true);
                })
                .setHitEffects(loc -> {
                    loc.createExplosion(0);
                    loc.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, loc, 0, 0.0, 0.0, 0.0, 0.0, null, true);
                })
                .build();
    }

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }
}
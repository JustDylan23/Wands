package me.dylan.wands.spell.spells;

import me.dylan.wands.knockback.KnockBack;
import me.dylan.wands.sound.CompoundSound;
import me.dylan.wands.spell.SpellData;
import me.dylan.wands.spell.types.Behaviour;
import me.dylan.wands.spell.types.MagicProjectile;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.SmallFireball;

public class FireComet implements SpellData {
    private final Behaviour behaviour;

    public FireComet() {
        this.behaviour = MagicProjectile.newBuilder(SmallFireball.class, 4)
                .setCastSound(CompoundSound.chain()
                        .add(Sound.ENTITY_FIREWORK_ROCKET_BLAST)
                        .add(Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, 1, 10, 5)
                        .add(Sound.ENTITY_FIREWORK_ROCKET_TWINKLE_FAR, 1, 20)
                )
                .setSpellEffectRadius(3.0F)
                .setEntityDamage(8)
                .setEntityEffects(entity -> entity.setFireTicks(80))
                .setKnockBack(KnockBack.EXPLOSION)
                .setLifeTime(20)
                .setProjectileProps(projectile -> {
                    projectile.setIsIncendiary(false);
                    projectile.setYield(0);
                })
                .setSpellRelativeEffects((loc, world) -> {
                    world.spawnParticle(Particle.FLAME, loc, 6, 0.8, 0.8, 0.8, 0.1, null, true);
                    world.spawnParticle(Particle.SMOKE_NORMAL, loc, 10, 1, 1, 1, 0.05, null, true);
                    world.spawnParticle(Particle.SMOKE_LARGE, loc, 10, 0.6, 0.6, 0.6, 0.15, null, true);
                    world.spawnParticle(Particle.LAVA, loc, 5, 1, 1, 1, 0, null, true);
                })
                .setHitEffects((loc, world) -> {
                    loc.createExplosion(0.0f);
                    world.spawnParticle(Particle.EXPLOSION_HUGE, loc, 0, 0.0, 0.0, 0.0, 0.0, null, true);
                })
                .build();
    }

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }
}
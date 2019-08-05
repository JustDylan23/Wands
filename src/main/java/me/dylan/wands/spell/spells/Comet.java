package me.dylan.wands.spell.spells;

import me.dylan.wands.knockback.KnockBack;
import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.SpellEffectUtil;
import me.dylan.wands.spell.types.Base;
import me.dylan.wands.spell.types.MagicProjectile;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.SmallFireball;

public enum Comet implements Castable {
    INSTANCE;
    private final Base baseType;

    Comet() {
        this.baseType = MagicProjectile.newBuilder(SmallFireball.class, 4.0F)
                .setSpellEffectRadius(4.0F)
                .setEntityDamage(10)
                .setEntityEffects(entity -> entity.setFireTicks(100))
                .setKnockBack(KnockBack.EXPLOSION)
                .setCastSound(Sound.ENTITY_FIREWORK_ROCKET_BLAST)
                .setSpellRelativeEffects((loc, world) -> {
                    world.spawnParticle(Particle.SPELL_WITCH, loc, 40, 0.8, 0.8, 0.8, 0.15, null, true);
                    world.spawnParticle(Particle.SMOKE_LARGE, loc, 10, 0.6, 0.6, 0.6, 0.1, null, true);
                    world.spawnParticle(Particle.SMOKE_LARGE, loc, 10, 1.0, 1.0, 1.0, 0.1, null, true);
                })
                .setProjectileProps(projectile -> {
                    projectile.setIsIncendiary(false);
                    projectile.setYield(0.0F);
                })
                .setLifeTime(20)
                .setHitEffects((loc, world) -> {
                    loc.createExplosion(0.0f);
                    world.spawnParticle(Particle.EXPLOSION_HUGE, loc, 0, 0.0, 0.0, 0.0, 0.0, null, true);
                    world.spawnParticle(Particle.SMOKE_LARGE, loc, 50, 2.0, 2.0, 2.0, 0.05, null, true);
                    SpellEffectUtil.runTaskLater(() ->
                                    world.playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, SoundCategory.MASTER, 5F, 1F)
                            , 0, 3, 3);
                })
                .build();
    }

    @Override
    public Base getBaseType() {
        return baseType;
    }
}

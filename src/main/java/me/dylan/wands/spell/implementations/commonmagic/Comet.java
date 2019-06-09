package me.dylan.wands.spell.implementations.commonmagic;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.handler.Behaviour;
import me.dylan.wands.spell.handler.ProjectileSpell;
import me.dylan.wands.util.EffectUtil;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.SmallFireball;

public enum Comet implements Castable {
    INSTANCE;

    private Behaviour behaviour;

    Comet() {
        this.behaviour = behaviour = ProjectileSpell.newBuilder(SmallFireball.class, 3F)
                .setCastEffects(loc ->
                        loc.getWorld().playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_BLAST, SoundCategory.MASTER, 5F, 1F)
                )
                .setEffectRadius(4.5F)
                .setEntityDamage(10)
                .setEntityEffects(entity -> entity.setFireTicks(60))
                .setRelativeEffects(loc -> {
                    World world = loc.getWorld();
                    world.spawnParticle(Particle.SPELL_WITCH, loc, 40, 0.8, 0.8, 0.8, 0.15, null, true);
                    world.spawnParticle(Particle.SMOKE_LARGE, loc, 10, 0.6, 0.6, 0.6, 0.1, null, true);
                    world.spawnParticle(Particle.SMOKE_LARGE, loc, 10, 1.0, 1.0, 1.0, 0.1, null, true);
                })
                .setProjectileProps(projectile -> {
                    projectile.setIsIncendiary(false);
                    projectile.setYield(0);
                })
                .setLifeTime(20)
                .setPushSpeed(1)
                .setHitEffects(loc -> {
                    loc.getWorld().playSound(loc, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, SoundCategory.MASTER, 5F, 1F);
                    loc.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, loc, 0, 0.0, 0.0, 0.0, 0.0, null, true);
                    loc.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 50, 2, 2, 2, 0.05, null, true);
                    EffectUtil.runTaskLater(() ->
                                    loc.getWorld().playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, SoundCategory.MASTER, 5F, 1F)
                            , 0, 3, 3);
                })
                .build();
    }

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }
}

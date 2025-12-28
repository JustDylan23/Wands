package me.dylan.wands.spell.spells.fire;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.accessories.KnockBack;
import me.dylan.wands.spell.accessories.sound.CompoundSound;
import me.dylan.wands.spell.spellbuilders.Behavior;
import me.dylan.wands.spell.spellbuilders.MagicProjectile;
import me.dylan.wands.spell.spells.AffinityType;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.SmallFireball;

public class FireComet implements Castable {
    @Override
    public AffinityType[] getAffinityTypes() {
        return new AffinityType[]{AffinityType.FIRE_MAGIC};
    }

    @Override
    public Behavior createBehaviour() {
        return MagicProjectile.newBuilder(SmallFireball.class, 4)
                .setCastSound(CompoundSound.chain()
                        .add(Sound.ENTITY_FIREWORK_ROCKET_BLAST)
                        .addAll(Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, 1, 10, 5)
                        .addAll(Sound.ENTITY_FIREWORK_ROCKET_TWINKLE_FAR, 1, 20)
                )
                .setSpellEffectRadius(3.0F)
                .setEntityDamage(8)
                .setEntityEffects((entity, spellInfo) -> entity.setFireTicks(80))
                .setKnockBack(KnockBack.EXPLOSION)
                .setLifeTime(20)
                .setProjectileProps(projectile -> {
                    projectile.setIsIncendiary(false);
                    projectile.setYield(0);
                })
                .setSpellRelativeEffects((loc, spellInfo) -> {
                    World world = spellInfo.world();
                    world.spawnParticle(Particle.FLAME, loc, 6, 0.8, 0.8, 0.8, 0.1, null, true);
                    world.spawnParticle(Particle.SMOKE, loc, 10, 1, 1, 1, 0.05, null, true);
                    world.spawnParticle(Particle.LARGE_SMOKE, loc, 10, 0.6, 0.6, 0.6, 0.15, null, true);
                    world.spawnParticle(Particle.DRIPPING_LAVA, loc, 5, 1, 1, 1, 0, null, true);
                })
                .setHitEffects((loc, spellInfo) -> {
                    World world = spellInfo.world();
                    world.spawnParticle(Particle.EXPLOSION_EMITTER, loc, 0, 0.0, 0.0, 0.0, 0.0, null, true);
                    world.createExplosion(loc, 0.0f);
                })
                .build();
    }
}
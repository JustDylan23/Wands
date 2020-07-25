package me.dylan.wands.spell.spells.witch;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.accessories.KnockBack;
import me.dylan.wands.spell.accessories.sound.RepeatableSound;
import me.dylan.wands.spell.accessories.sound.SoundEffect;
import me.dylan.wands.spell.spellbuilders.Behavior;
import me.dylan.wands.spell.spellbuilders.MagicProjectile;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.SmallFireball;

public class Comet implements Castable {
    @Override
    public Behavior createBehaviour() {
        SoundEffect soundEffect = RepeatableSound.from(Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, 1, 0, 3, 3);
        return MagicProjectile.newBuilder(SmallFireball.class, 4)
                .setSpellEffectRadius(3.0F)
                .setEntityDamage(8)
                .setEntityEffects((entity, spellInfo) -> entity.setFireTicks(80))
                .setKnockBack(KnockBack.EXPLOSION)
                .setCastSound(Sound.ENTITY_FIREWORK_ROCKET_BLAST)
                .setSpellRelativeEffects((loc, spellInfo) -> {
                    World world = spellInfo.world();
                    world.spawnParticle(Particle.SPELL_WITCH, loc, 40, 0.8, 0.8, 0.8, 0.15, null, true);
                    world.spawnParticle(Particle.SMOKE_LARGE, loc, 10, 0.6, 0.6, 0.6, 0.1, null, true);
                    world.spawnParticle(Particle.SMOKE_LARGE, loc, 10, 1.0, 1.0, 1.0, 0.1, null, true);
                })
                .setProjectileProps(projectile -> {
                    projectile.setIsIncendiary(false);
                    projectile.setYield(0.0F);
                })
                .setLifeTime(20)
                .setHitEffects((loc, spellInfo) -> {
                    World world = spellInfo.world();
                    world.createExplosion(loc, 0.0f);
                    world.spawnParticle(Particle.EXPLOSION_HUGE, loc, 0, 0.0, 0.0, 0.0, 0.0, null, true);
                    world.spawnParticle(Particle.SMOKE_LARGE, loc, 50, 2.0, 2.0, 2.0, 0.05, null, true);
                    soundEffect.play(loc);
                })
                .build();
    }
}

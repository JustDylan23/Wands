package me.dylan.wands.spells;

import me.dylan.wands.CastableSpell;
import me.dylan.wands.spellbehaviour.ProjectileSpell;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.entity.SmallFireball;

public class Comet extends CastableSpell {

    private final ProjectileSpell spellBehaviour;

    @Override
    protected void cast(Player player) {
        spellBehaviour.executeFrom(player);
    }

    public Comet() {
        super("Comet");
        spellBehaviour = new ProjectileSpell.Builder<>(SmallFireball.class, "Spell", 4F)
                .setProjectilePropperties(projectile -> {
                    projectile.setIsIncendiary(false);
                    projectile.setYield(0);
                })
                .setLifeTime(20)
                .setEntityDamage(10)
                .setPushSpeed(1)
                .setEffectAreaRange(4.5F)
                .setCastEffects(loc ->
                        loc.getWorld().playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_BLAST, SoundCategory.MASTER, 5F, 1F)
                )
                .setVisualEffects(loc -> {
                    World world = loc.getWorld();
                    world.spawnParticle(Particle.SPELL_WITCH, loc, 40, 0.8, 0.8, 0.8, 0.15, null, true);
                    world.spawnParticle(Particle.SMOKE_LARGE, loc, 10, 0.6, 0.6, 0.6, 0.1, null, true);
                    world.spawnParticle(Particle.SMOKE_LARGE, loc, 10, 1.0, 1.0, 1.0, 0.1, null, true);
                })
                .setEntityEffects(entity -> entity.setFireTicks(60))
                .setHitEffects(loc -> {
                    loc.getWorld().playSound(loc, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, SoundCategory.MASTER, 5F, 1F);
                    loc.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, loc, 0, 0.0, 0.0, 0.0, 0.0, null, true);
                    loc.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 50, 2, 2, 2, 0.05, null, true);

                    for (int i = 0; i < 3; ++i) {
                        Bukkit.getScheduler().runTaskLater(plugin, () ->
                                loc.getWorld().playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, SoundCategory.MASTER, 5F, 1F), (i * 5));
                    }
                })
                .build();
    }
}

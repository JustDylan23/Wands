package me.dylan.wands.spells;

import me.dylan.wands.Spell;
import me.dylan.wands.spellbehaviour.ProjectileSpell;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;

public class Comet extends Spell {

    private final ProjectileSpell spellBehaviour;

    private Comet() {
        super("Comet");
        spellBehaviour = new ProjectileSpell.Builder<>(Fireball.class)
                .setProjectilePropperties(projectile -> projectile.setIsIncendiary(false))
                .setLifeTime(20)
                .setEntityDamage(10)
                .onCast(loc ->
                    loc.getWorld().playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_BLAST, SoundCategory.MASTER, 4.0F, 1.0F)
                )
                .setVisualEffects(loc -> {
                    World world = loc.getWorld();
                    world.spawnParticle(Particle.SPELL_WITCH, loc, 40, 0.8, 0.8, 0.8, 0.15, null, true);
                    world.spawnParticle(Particle.SMOKE_LARGE, loc, 10, 0.6, 0.6, 0.6, 0.1, null, true);
                    world.spawnParticle(Particle.SMOKE_LARGE, loc, 10, 1.0, 1.0, 1.0, 0.1, null, true);
                })
                .setEntityEffects(entity -> {
                    entity.setFireTicks(60);
                })
                .build();
    }

    @Override
    protected void cast(Player player) {
        spellBehaviour.executeFrom(player);
    }

    public static Comet getInstance() {
        return InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder {
        private static final Comet INSTANCE = new Comet();
    }
}

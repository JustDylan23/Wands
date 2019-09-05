package me.dylan.wands.spell.spells;

import me.dylan.wands.Main;
import me.dylan.wands.spell.SpellData;
import me.dylan.wands.spell.types.Behavior;
import me.dylan.wands.spell.types.Behavior.Target;
import me.dylan.wands.spell.types.Phase;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class MephiChoke implements SpellData {
    private final Behavior behavior;
    private final PotionEffect wither = new PotionEffect(PotionEffectType.WITHER, 100, 2);

    public MephiChoke() {
        this.behavior = Phase.newBuilder(Target.SINGLE)
                .setCastSound(Sound.ENTITY_PHANTOM_BITE)
                .setEffectDistance(30)
                .setSpellEffectRadius(2.8F)
                .setSpellRelativeEffects((loc, world) -> {
                    world.spawnParticle(Particle.SMOKE_NORMAL, loc, 5, 0.8, 0.8, 0.8, 0.1, null, true);
                    world.spawnParticle(Particle.SMOKE_LARGE, loc, 3, 0.8, 0.8, 0.8, 0.1, null, true);
                    world.spawnParticle(Particle.VILLAGER_HAPPY, loc, 5, 0.8, 0.8, 0.8, 0.1, null, true);
                })
                .setStagePassCondition(Entity::isOnGround)
                .setEntityEffects(this::entityEffects)
                .build();
    }

    private void entityEffects(LivingEntity entity) {
        entity.setVelocity(new Vector(0, 0.5, 0));
        Vector vector = new Vector(0, 0.02, 0);
        World world = entity.getWorld();
        entity.addPotionEffect(wither, true);
        new BukkitRunnable() {
            int i;

            @Override
            public void run() {
                if (i++ == 90) {
                    cancel();
                } else {
                    entity.setVelocity(vector);
                    Location loc = entity.getLocation();
                    world.spawnParticle(Particle.SMOKE_NORMAL, loc, 1, 0.5, 0.5, 0.5, 0.1, null, true);
                    world.spawnParticle(Particle.SMOKE_LARGE, loc, 1, 0.5, 0.5, 0.5, 0.1, null, true);
                    world.spawnParticle(Particle.VILLAGER_HAPPY, loc, 1, 0.5, 0.5, 0.5, 0.1, null, true);
                    if (i % 10 == 0) {
                        world.playSound(loc, Sound.ENTITY_PHANTOM_FLAP, SoundCategory.MASTER, 4, 1);
                    }
                }
            }
        }.runTaskTimer(Main.getPlugin(), 10, 1);
    }

    public Behavior getBehavior() {
        return behavior;
    }
}

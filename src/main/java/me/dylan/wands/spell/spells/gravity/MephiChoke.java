package me.dylan.wands.spell.spells.gravity;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.accessories.SpellInfo;
import me.dylan.wands.spell.spellbuilders.Behavior;
import me.dylan.wands.spell.spellbuilders.BuildableBehaviour.Target;
import me.dylan.wands.spell.spellbuilders.Phase;
import me.dylan.wands.spell.spells.AffinityType;
import me.dylan.wands.utils.Common;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class MephiChoke implements Castable {

    @Override
    public AffinityType[] getAffinityTypes() {
        return new AffinityType[]{AffinityType.GRAVITY_MAGIC};
    }

    public Behavior createBehaviour() {
        return Phase.newBuilder(Target.SINGLE)
                .setCastSound(Sound.ENTITY_PHANTOM_BITE)
                .setEffectDistance(30)
                .setSpellEffectRadius(2.8F)
                .setSpellRelativeEffects((loc, spellInfo) -> {
                    World world = spellInfo.world();
                    world.spawnParticle(Particle.SMOKE, loc, 5, 0.8, 0.8, 0.8, 0.1, null, true);
                    world.spawnParticle(Particle.LARGE_SMOKE, loc, 3, 0.8, 0.8, 0.8, 0.1, null, true);
                    world.spawnParticle(Particle.HAPPY_VILLAGER, loc, 5, 0.8, 0.8, 0.8, 0.1, null, true);
                })
                .setStagePassCondition(Entity::isOnGround)
                .setPotionEffects(new PotionEffect(PotionEffectType.WITHER, 100, 2))
                .setEntityEffects(this::entityEffects)
                .build();
    }

    private void entityEffects(LivingEntity entity, SpellInfo ignored) {
        entity.setVelocity(new Vector(0, 0.5, 0));
        Vector vector = new Vector(0, 0.02, 0);
        World world = entity.getWorld();
        BukkitRunnable bukkitRunnable = new BukkitRunnable() {
            int i = 0;

            @Override
            public void run() {
                if (i++ == 90) {
                    cancel();
                } else {
                    entity.setVelocity(vector);
                    Location loc = entity.getLocation();
                    world.spawnParticle(Particle.SMOKE, loc, 1, 0.5, 0.5, 0.5, 0.1, null, true);
                    world.spawnParticle(Particle.LARGE_SMOKE, loc, 1, 0.5, 0.5, 0.5, 0.1, null, true);
                    world.spawnParticle(Particle.HAPPY_VILLAGER, loc, 1, 0.5, 0.5, 0.5, 0.1, null, true);
                    if (i % 10 == 0) {
                        world.playSound(loc, Sound.ENTITY_PHANTOM_FLAP, SoundCategory.MASTER, 4, 1);
                    }
                }
            }
        };
        Common.runTaskTimer(bukkitRunnable, 10, 1);
    }
}

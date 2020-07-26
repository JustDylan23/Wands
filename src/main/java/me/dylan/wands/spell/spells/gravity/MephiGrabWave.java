package me.dylan.wands.spell.spells.gravity;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.spellbuilders.Behavior;
import me.dylan.wands.spell.spellbuilders.BuildableBehaviour.Target;
import me.dylan.wands.spell.spellbuilders.Ray;
import me.dylan.wands.spell.spells.AffinityType;
import me.dylan.wands.utils.Common;
import org.bukkit.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class MephiGrabWave implements Castable {
    @Override
    public AffinityType[] getAffinityTypes() {
        return new AffinityType[]{AffinityType.GRAVITY_MAGIC};
    }

    @Override
    public Behavior createBehaviour() {
        return Ray.newBuilder(Target.MULTI)
                .setCastSound(Sound.ENTITY_EVOKER_CAST_SPELL)
                .setRayWidth(2)
                .setEntityDamage(5)
                .setSpellEffectRadius(2)
                .setSpellRelativeEffects((location, spellInfo) -> {
                    World world = spellInfo.world();
                    world.spawnParticle(Particle.VILLAGER_HAPPY, location, 4, 0.4, 0.4, 0.4, 0.1, null, true);
                    world.spawnParticle(Particle.SMOKE_LARGE, location, 4, 0.3, 0.3, 0.3, 0.1, null, true);
                    world.spawnParticle(Particle.SMOKE_LARGE, location, 4, 1, 1, 1, 0.05, null, true);
                })
                .setHitEffects((location, spellInfo) -> {
                    World world = spellInfo.world();
                    world.spawnParticle(Particle.VILLAGER_HAPPY, location, 6, 3, 3, 3, 0.4, null, true);
                    world.spawnParticle(Particle.SMOKE_LARGE, location, 10, 0.3, 0.3, 0.3, 0.4, null, true);
                    world.playSound(location, Sound.ENTITY_ILLUSIONER_MIRROR_MOVE, SoundCategory.MASTER, 4, 1);
                })
                .setEffectDistance(30)
                .setPotionEffects(
                        new PotionEffect(PotionEffectType.SLOW, 60, 2, false),
                        new PotionEffect(PotionEffectType.BLINDNESS, 60, 2, false)
                )
                .setEntityEffects((livingEntity, spellInfo) -> {
                    BukkitRunnable bukkitRunnable = new BukkitRunnable() {
                        int i = 0;

                        @Override
                        public void run() {
                            Location entityLoc = livingEntity.getLocation();
                            double distance = entityLoc.distance(spellInfo.origin());
                            if (++i == 30 || distance <= 6) {
                                cancel();
                            } else {
                                livingEntity.setVelocity(spellInfo.origin().clone().subtract(entityLoc).toVector().normalize());
                            }
                        }
                    };
                    Common.runTaskTimer(bukkitRunnable, 0, 1);
                })
                .build();
    }
}

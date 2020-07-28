package me.dylan.wands.spell.spells.common;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.spellbuilders.Behavior;
import me.dylan.wands.spell.spellbuilders.Spark;
import me.dylan.wands.spell.spellbuilders.Spark.Target;
import me.dylan.wands.spell.spellbuilders.Wave;
import me.dylan.wands.spell.spells.AffinityType;
import me.dylan.wands.spell.util.SpellEffectUtil;
import me.dylan.wands.spell.util.SpellInteractionUtil;
import me.dylan.wands.utils.Common;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class SoulHaunt implements Castable {
    @Override
    public AffinityType[] getAffinityTypes() {
        return new AffinityType[]{AffinityType.DARK_MAGIC, AffinityType.CORRUPTED_MAGIC};
    }

    @Override
    public Behavior createBehaviour() {
        return Spark.newBuilder(Target.SINGLE_REQUIRED)
                .setEffectDistance(40)
                .setCastSound(Sound.ENTITY_PHANTOM_AMBIENT)
                .setEntityEffects((target, spellInfo) -> {
                    Location projectileLoc = spellInfo.caster().getEyeLocation();
                    double speed = 0.6, turnRate = 0.28;

                    Common.runTaskTimer(new BukkitRunnable() {
                        int count;
                        Vector projectileVel = projectileLoc.getDirection().multiply(speed);

                        @Override
                        public void run() {
                            count++;
                            if (count > 200 || !target.isValid()) {
                                cancel();
                                return;
                            }
                            Vector dir = projectileVel.clone().multiply(1.0 / speed);
                            Vector toTarget = target.getEyeLocation().subtract(projectileLoc).toVector().normalize();
                            if (toTarget.dot(dir) < Math.cos(Math.toRadians(110))) {
                                projectileLoc.add(projectileVel);
                            } else {
                                Vector newVelocity = toTarget.add(dir.multiply(1.0 / turnRate)).normalize().multiply(speed);
                                projectileLoc.add(newVelocity);
                                projectileVel = newVelocity;
                            }

                            if (target.getEyeLocation().distance(projectileLoc) < 0.8) {
                                projectileLoc.getWorld().spawnParticle(Particle.SOUL, projectileLoc, 10, 0.5, 0.5, 0.5, 0);
                                projectileLoc.getWorld().playSound(projectileLoc, Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 5, 1);
                                target.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 2));
                                target.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 100, 3));
                                cancel();
                            } else {
                                projectileLoc.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, projectileLoc, 8, 0.2, 0.2, 0.2, 0.05);
                                projectileLoc.getWorld().spawnParticle(Particle.SOUL_FIRE_FLAME, projectileLoc, 5, 0.05, 0.05, 0.05, 0);
                            }
                        }
                    }, 0, 1);
                })
                .build();
    }
}

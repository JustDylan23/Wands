package me.dylan.wands.spell.spells.common;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.spellbuilders.Behavior;
import me.dylan.wands.spell.spells.AffinityType;
import me.dylan.wands.spell.util.SpellEffectUtil;
import me.dylan.wands.utils.Common;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class SoulSeeker extends Behavior implements Castable {
    public static final int DIAMETER = 30, SEEKING_ANGLE = 110;
    public static final double SPEED = 0.6, TURN_RATE = 0.28;

    @Override
    public AffinityType[] getAffinityTypes() {
        return new AffinityType[]{AffinityType.DARK_MAGIC, AffinityType.CORRUPTED_MAGIC};
    }

    @Override
    public Behavior createBehaviour() {
        return this;
    }

    @Nullable LivingEntity findOptimalTarget(Location location) {
        Vector playerVector = location.getDirection().normalize();
        World world = location.getWorld();
        int radius = DIAMETER / 2;
        if (world != null) {
            Location searchCenter = location.clone().add(playerVector.clone().multiply(radius));
            Collection<Entity> nearbyEntities = world.getNearbyEntities(searchCenter, radius, radius, radius, LivingEntity.class::isInstance);

            double highestDot = -1;
            Entity bestMatch = null;
            double minDot = Math.cos(Math.toRadians(SEEKING_ANGLE));

            for (Entity entity : nearbyEntities) {
                Location entityLocation = entity.getLocation();
                Vector entityVector = entityLocation.subtract(location).toVector().normalize();
                double entityDot = playerVector.dot(entityVector);
                if (entityDot > minDot && entityDot > highestDot) {
                    highestDot = entityDot;
                    bestMatch = entity;
                }
            }
            return (LivingEntity) bestMatch;
        }
        return null;
    }

    @Override
    public boolean cast(@NotNull Player player, @NotNull String weapon) {
        Location playerLocation = player.getLocation();
        //noinspection ConstantConditions
        playerLocation.getWorld().playSound(playerLocation, Sound.ENTITY_PHANTOM_AMBIENT, 4, 1);

        LivingEntity target = findOptimalTarget(playerLocation);
        Common.runTaskTimer(new BukkitRunnable() {
            int count;
            final Location projectileLoc = player.getEyeLocation();
            Vector projectileVel = projectileLoc.getDirection().multiply(SPEED);

            @Override
            public void run() {
                if (++count > 200) {
                    cancel();
                } else {
                    advanceLocation();
                    if (!collideWithTarget()) {
                        applyTrail(projectileLoc);
                    } else {
                        handleCollision(weapon);
                    }
                }
            }

            void advanceLocation() {
                if (target != null) {
                    Vector dir = projectileVel.clone().multiply(1.0 / SPEED);
                    Vector toTarget = target.getEyeLocation().subtract(projectileLoc).toVector().normalize();
                    if (toTarget.dot(dir) > Math.cos(Math.toRadians(110))) {
                        projectileLoc.add(projectileVel);
                        projectileVel = toTarget.add(dir.multiply(1.0 / TURN_RATE)).normalize().multiply(SPEED);
                    }
                }
                projectileLoc.add(projectileVel);
            }

            boolean collideWithTarget() {
                return target != null && target.getEyeLocation().distance(projectileLoc) < 0.8;
            }

            void handleCollision(String weapon) {
                projectileLoc.getWorld().spawnParticle(Particle.SOUL, projectileLoc, 15, 0.6, 0.6, 0.6, 0);
                projectileLoc.getWorld().playSound(projectileLoc, Sound.BLOCK_RESPAWN_ANCHOR_DEPLETE, 5, 1);
                SpellEffectUtil.damageEffect(player, target, 3, weapon);
                target.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 100, 1));
                target.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 100, 2));
                target.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 100, 1));
                cancel();
            }

            void applyTrail(Location location) {
                World world = location.getWorld();
                world.spawnParticle(Particle.SOUL, projectileLoc, 2, 0.3, 0.3, 0.3, 0);
                world.spawnParticle(Particle.SOUL_FIRE_FLAME, projectileLoc, 8, 0.2, 0.2, 0.2, 0.05);
                world.spawnParticle(Particle.SOUL_FIRE_FLAME, projectileLoc, 5, 0.05, 0.05, 0.05, 0);
            }
        }, 0, 1);
        return true;
    }
}

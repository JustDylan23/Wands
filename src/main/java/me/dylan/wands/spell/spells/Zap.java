package me.dylan.wands.spell.spells;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.accessories.SpellInfo;
import me.dylan.wands.spell.spellbuilders.Behavior;
import me.dylan.wands.spell.spellbuilders.BuildableBehaviour.Target;
import me.dylan.wands.spell.spellbuilders.Ray;
import me.dylan.wands.spell.util.SpellEffectUtil;
import me.dylan.wands.utils.Common;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Zap implements Castable {

    private static final PotionEffect SLOW_EFFECT = new PotionEffect(PotionEffectType.SLOW, 40, 3);
    private static final int TOTAL_RICOCHET = 5;
    private static final int RICOCHET_REACH = 5;
    private static final int DAMAGE = 7;

    @Override
    public Behavior createBehaviour() {
        return Ray.newBuilder(Target.SINGLE)
                .setMetersPerTick(2)
                .setRayWidth(1)
                .setSpellRelativeEffects((loc, spellInfo) -> {
                    World world = spellInfo.world();
                    world.spawnParticle(Particle.EXPLOSION_NORMAL, loc, 8, 0.1, 0.1, 0.1, 0.02, null, true);
                })
                .setCastSound(Sound.ENTITY_LLAMA_SWAG)
                .setEffectDistance(30)
                .setEntityEffects(this::zap)
                .build();
    }

    private void zap(LivingEntity zappedEntity, SpellInfo spellInfo) {
        LivingEntity zapTo = zappedEntity;
        Vector noVelocity = new Vector(0, 0, 0);
        Set<Entity> zappedEntities = new HashSet<>();

        for (int i = 0; i < TOTAL_RICOCHET; i++) {
            zappedEntities.add(zapTo);
            LivingEntity victim = zapTo;
            Common.runTaskLater(() -> {
                victim.damage(DAMAGE);
                Location location = victim.getLocation();
                location.getWorld().playSound(location, Sound.ITEM_TRIDENT_RETURN, 1F, 2F);
            }, i * 2);
            zapTo.setVelocity(noVelocity);
            zapTo.addPotionEffect(SLOW_EFFECT, true);

            if (i >= TOTAL_RICOCHET - 1) {
                break;
            }

            LivingEntity next = getClosestZappableEntity(zapTo, zappedEntities);
            drawLine(zapTo, next);
            zapTo = next;
        }
    }

    private void drawLine(LivingEntity from, LivingEntity to) {
        Location start = from.getEyeLocation();
        Location target = to.getEyeLocation();

        World world = start.getWorld();

        Vector toTarget = target.subtract(start).toVector();
        double distance = toTarget.length();

        double stepSize = 0.2;
        int numSteps = (int) (distance / stepSize);

        Vector direction = toTarget.multiply(stepSize / distance); // Normalize vector

        for (int i = 0; i < numSteps; ++i) {
            start.add(direction);
            world.spawnParticle(Particle.EXPLOSION_NORMAL, start, 1, 0, 0, 0, 0.02, null, true);
        }
    }

    private LivingEntity getClosestZappableEntity(@NotNull Entity zappedEntity, Set<Entity> zappedEntities) {
        Location loc = zappedEntity.getLocation();
        List<LivingEntity> nearby = loc.getWorld()
                .getNearbyEntities(loc, RICOCHET_REACH, RICOCHET_REACH, RICOCHET_REACH).stream()
                .filter(entity -> isZappable(zappedEntity, entity, zappedEntities))
                .map(LivingEntity.class::cast)
                .collect(Collectors.toList());

        double closestDistance = Double.MAX_VALUE;
        LivingEntity closestEntity = null;

        for (LivingEntity livingEntity : nearby) {
            double distance = livingEntity.getLocation().distanceSquared(loc);
            if (distance < closestDistance) {
                closestDistance = distance;
                closestEntity = livingEntity;
            }
        }

        return closestEntity;
    }

    private boolean isZappable(Entity zappedEntity, Entity entity, Set<Entity> zappedEntities) {
        return entity instanceof LivingEntity
                && !(entity instanceof ArmorStand)
                && !entity.equals(zappedEntity)
                && SpellEffectUtil.checkFriendlyFireOption(entity, entity)
                && !zappedEntities.contains(entity);
    }
}
package me.dylan.wands.spell.spells;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.accessories.SpellInfo;
import me.dylan.wands.spell.spellbuilders.Behavior;
import me.dylan.wands.spell.spellbuilders.BuildableBehaviour;
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

    @Override
    public Behavior createBehaviour() {
        return Ray.newBuilder(BuildableBehaviour.Target.SINGLE)
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
        Vector noVelocity = new Vector(0, 0, 0);
        Set<Entity> zappedEntities = new HashSet<>();

        LivingEntity lastConductor = spellInfo.caster();
        int totalRicochet = 5;
        for (int i = 0; i < totalRicochet; i++) {
            zappedEntities.add(zappedEntity);

            if (lastConductor.equals(spellInfo.caster())) {
                zappedEntity.damage(7);
            } else {
                zappedEntity.damage(7 - i, lastConductor);
            }
            zappedEntity.setVelocity(noVelocity);
            zappedEntity.addPotionEffect(SLOW_EFFECT, true);

            if (i >= totalRicochet - 1) {
                break;
            }

            LivingEntity next = getClosestZappableEntity(zappedEntity, 4, zappedEntities);
            drawLine(zappedEntity, next);
            zappedEntity = next;
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

    private LivingEntity getClosestZappableEntity(@NotNull Entity zappedEntity, int radius, Set<Entity> zappedEntities) {
        Location loc = zappedEntity.getLocation();
        List<LivingEntity> nearby = loc.getWorld()
                .getNearbyEntities(loc, radius, radius, radius).stream()
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

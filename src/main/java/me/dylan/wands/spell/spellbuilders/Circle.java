package me.dylan.wands.spell.spellbuilders;

import me.dylan.wands.spell.accessories.SpellInfo;
import me.dylan.wands.spell.util.SpellEffectUtil;
import me.dylan.wands.utils.Common;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

/**
 * Draws circle around player or target depending on how its configured.
 * <p>
 * configurable:
 * - Circle radius
 * - Circle height
 * - Circle placement, either around player or target.
 * - Effect distance, only has effect when circle placement is set to target.
 * - Circle drawing speed
 */
public final class Circle extends BuildableBehaviour {
    private final int speed, height, effectDistance, circleRadius;
    private final CirclePlacement circlePlacement;

    private Circle(@NotNull Builder builder) {
        super(builder.baseProps);
        this.speed = builder.speed;
        this.height = builder.height;
        this.effectDistance = builder.effectDistance;
        this.circleRadius = builder.circleRadius;
        this.circlePlacement = builder.circlePlacement;

        addPropertyInfo("Radius", circleRadius, "meters");
        addPropertyInfo("Circle origin", circlePlacement);
        addPropertyInfo("Height", height, "meters");
        addPropertyInfo("Meters per tick", speed, "meters");
        addPropertyInfo("Effect distance", effectDistance, "meters");
    }

    public static @NotNull Builder newBuilder(CirclePlacement circlePlacement) {
        return new Builder(circlePlacement);
    }

    @Override
    public boolean cast(@NotNull Player player, @NotNull String weapon) {
        Location circleCenter = (circlePlacement == CirclePlacement.TARGET)
                ? SpellEffectUtil.getSpellLocation(player, effectDistance)
                : player.getLocation();

        SpellInfo spellInfo = new SpellInfo(player, player.getLocation(), circleCenter);

        castSounds.play(player);

        Location[] circlePoints = SpellEffectUtil.getHorizontalCircleFrom(circleCenter.clone().add(0, height, 0), circleRadius, player.getLocation().getYaw(), 1);

        BukkitRunnable bukkitRunnable = new BukkitRunnable() {
            int circlePoint;

            @Override
            public void run() {
                for (int i = 0; i < speed; i++) {
                    if (circlePoint >= circlePoints.length) {
                        cancel();
                        for (LivingEntity entity : SpellEffectUtil.getNearbyLivingEntities(player, circleCenter, spellEffectRadius)) {
                            knockBack.apply(entity, circleCenter);
                            SpellEffectUtil.damageEffect(player, entity, entityDamage, weapon);
                            entityEffects.accept(entity, spellInfo);
                            for (PotionEffect potionEffect : potionEffects) {
                                entity.addPotionEffect(potionEffect, true);
                            }
                        }
                        break;
                    } else {
                        Location loc = circlePoints[circlePoint];
                        spellRelativeEffects.accept(loc, spellInfo);
                    }
                    circlePoint++;
                }
            }
        };
        Common.runTaskTimer(bukkitRunnable, 0, 1);
        return true;
    }

    public enum CirclePlacement {
        TARGET,
        RELATIVE
    }

    public static final class Builder extends AbstractBuilder<Builder> {
        private final CirclePlacement circlePlacement;
        private int circleRadius, speed = 1;
        private int effectDistance, height;

        private Builder(CirclePlacement circlePlacement) {
            this.circlePlacement = circlePlacement;
        }

        @Override
        Builder self() {
            return this;
        }

        @Override
        public @NotNull Behavior build() {
            return new Circle(this);
        }

        public Builder setCircleRadius(int circleRadius) {
            this.circleRadius = circleRadius;
            return this;
        }

        public Builder setCircleHeight(int height) {
            this.height = height;
            return this;
        }

        public Builder setMetersPerTick(int meters) {
            this.speed = Math.max(1, meters);
            return this;
        }

        public Builder setEffectDistance(int distance) {
            this.effectDistance = distance;
            return this;
        }
    }
}

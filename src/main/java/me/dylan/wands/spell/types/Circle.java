package me.dylan.wands.spell.types;

import me.dylan.wands.spell.SpellEffectUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
public final class Circle extends Base {
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
        addPropertyInfo("Meters per tick", speed, "ticks");
        addPropertyInfo("Effect distance", effectDistance, "meters");
    }

    @NotNull
    public static Builder newBuilder(CirclePlacement circlePlacement) {
        return new Builder(circlePlacement);
    }

    @Override
    public boolean cast(@NotNull Player player, @NotNull String weaponName) {
        Location circleCenter = getCircleCenter(player);

        if (circleCenter == null) {
            return false;
        }

        castSounds.play(player);

        Location[] circlePoints = SpellEffectUtil.getHorizontalCircleFrom(circleCenter.clone().add(0, height, 0), circleRadius);

        new BukkitRunnable() {
            int circlePoint = 0;

            @Override
            public void run() {
                for (int i = 0; i < speed; i++) {
                    if (circlePoint >= circlePoints.length) {
                        cancel();
                        applyEntityEffects(player, circleCenter, weaponName);
                        break;
                    } else {
                        Location loc = circlePoints[circlePoint];
                        spellRelativeEffects.accept(loc, loc.getWorld());
                    }
                    circlePoint++;
                }
            }
        }.runTaskTimer(plugin, 0, 1);
        return true;
    }

    @Nullable
    private Location getCircleCenter(Player player) {
        return (circlePlacement == CirclePlacement.TARGET)
                ? SpellEffectUtil.getSpellLocation(effectDistance, player)
                : player.getLocation();
    }

    public enum CirclePlacement {
        TARGET,
        RELATIVE
    }

    public static final class Builder extends AbstractBuilder<Builder> {
        private final CirclePlacement circlePlacement;
        private int circleRadius, speed = 1;
        private int effectDistance, height;
        private boolean requireLivingTarget = false;

        private Builder(CirclePlacement circlePlacement) {
            this.circlePlacement = circlePlacement;
        }

        @Override
        Builder self() {
            return this;
        }

        @NotNull
        @Override
        public Base build() {
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

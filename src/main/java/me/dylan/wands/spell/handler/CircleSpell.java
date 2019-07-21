package me.dylan.wands.spell.handler;

import me.dylan.wands.spell.SpellEffectUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public final class CircleSpell extends Behaviour {
    private final int speed, height, effectDistance, circleRadius;
    private final CirclePlacement circlePlacement;
    private final boolean requireLivingTarget;

    private CircleSpell(Builder builder) {
        super(builder.baseMeta);
        this.speed = builder.speed;
        this.height = builder.height;
        this.effectDistance = builder.effectDistance;
        this.circleRadius = builder.circleRadius;
        this.circlePlacement = builder.circlePlacement;
        this.requireLivingTarget = builder.requireLivingTarget;

        addStringProperty("Radius", circleRadius, "meters");
        addStringProperty("Circle origin", circlePlacement);
        addStringProperty("Height", height, "meters");
        addStringProperty("Meters per tick", speed, "ticks");
        addStringProperty("Effect distance", effectDistance, "meters");
        addStringProperty("Require Living Target", requireLivingTarget);
    }

    public static Builder newBuilder(CirclePlacement circlePlacement) {
        return new Builder(circlePlacement);
    }

    @Override
    public boolean cast(Player player, String wandDisplayName) {
        castSounds.play(player);
        Location location;
        if (circlePlacement == CirclePlacement.RELATIVE) {
            location = player.getLocation();
        } else if (circlePlacement == CirclePlacement.TARGET) {
            location = SpellEffectUtil.getSpellLocation(effectDistance, player, requireLivingTarget);
            if (location == null) {
                return false;
            }
        } else location = player.getLocation();
        Location[] locations = SpellEffectUtil.getHorizontalCircleFrom(location.clone().add(0, height, 0), circleRadius);
        new BukkitRunnable() {
            int index = 0;

            @Override
            public void run() {
                for (int i = 0; i < speed; i++) {
                    if (index >= locations.length) {
                        cancel();
                        applyEntityEffects(location, player, wandDisplayName);
                        break;
                    } else {
                        Location loc = locations[index];
                        spellRelativeEffects.accept(loc, loc.getWorld());
                    }
                    index++;
                }
            }
        }.runTaskTimer(plugin, 0, 1);
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
        private boolean requireLivingTarget = false;

        private Builder(CirclePlacement circlePlacement) {
            this.circlePlacement = circlePlacement;
        }

        @Override
        Builder self() {
            return this;
        }

        @Override
        public Behaviour build() {
            return new CircleSpell(this);
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

        public Builder requireLivingTarget(boolean b) {
            this.requireLivingTarget = b;
            return this;
        }
    }
}

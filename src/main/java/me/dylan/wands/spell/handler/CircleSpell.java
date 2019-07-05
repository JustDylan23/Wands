package me.dylan.wands.spell.handler;

import me.dylan.wands.spell.SpellEffectUtil;
import me.dylan.wands.util.EffectUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public final class CircleSpell extends Behaviour {
    private final int circleSpeed, height, effectDistance;
    private final float circleRadius;
    private final CircleType circleType;

    private CircleSpell(Builder builder) {
        super(builder.baseMeta);
        this.circleSpeed = builder.circleSpeed;
        this.height = builder.height;
        this.effectDistance = builder.effectDistance;
        this.circleRadius = builder.circleRadius;
        this.circleType = builder.circleType;
    }

    public static Builder newBuilder(CircleType circleType) {
        return new Builder(circleType);
    }

    @Override
    public boolean cast(Player player) {
        castSounds.play(player);
        Location location;
        if (circleType == CircleType.RELATIVE) {
            location = player.getLocation();
        } else if (circleType == CircleType.TARGET) {
            location = SpellEffectUtil.getSpellLocation(effectDistance, player);
        } else location = player.getLocation();
        Location[] locations = SpellEffectUtil.getCircleFrom(location.clone().add(0, height, 0), circleRadius);
        new BukkitRunnable() {
            int index = 0;

            @Override
            public void run() {
                for (int i = 0; i < circleSpeed; i++) {
                    index++;
                    if (index >= locations.length) {
                        cancel();
                        EffectUtil.getNearbyLivingEntities(player, location, spellEffectRadius)
                                .forEach(entity -> {
                                    if (affectedEntityDamage != 0) entity.damage(affectedEntityDamage);
                                    affectedEntityEffects.accept(entity);
                                });
                    } else {
                        Location loc = locations[index];
                        spellRelativeEffects.accept(loc);
                    }
                }
            }
        }.runTaskTimer(plugin, 1, 1);
        return true;
    }

    @Override
    public String toString() {
        return super.toString() + "Radius: " + circleRadius
                + "\nHeight: " + height
                + "\nTickSkip: " + circleSpeed + " ticks";
    }

    public enum CircleType {
        TARGET,
        RELATIVE
    }

    public static final class Builder extends AbstractBuilder<Builder> {
        private int circleRadius, circleSpeed = 1;
        private int effectDistance, height = 0;
        private final CircleType circleType;

        private Builder(CircleType circleType) {
            this.circleType = circleType;
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

        public Builder setCircleSpeedPerTick(int meters) {
            this.circleSpeed = Math.max(1, meters);
            return this;
        }

        public Builder setEffectDistance(int distance) {
            this.effectDistance = distance;
            return this;
        }
    }
}

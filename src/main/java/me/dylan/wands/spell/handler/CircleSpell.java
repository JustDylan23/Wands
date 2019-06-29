package me.dylan.wands.spell.handler;

import me.dylan.wands.spell.SpellEffectUtil;
import me.dylan.wands.util.EffectUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public final class CircleSpell extends Behaviour {
    private final int tickSkip;
    private final int height;
    private final float circleRadius;
    private final CircleType circleType;

    private CircleSpell(Builder builder) {
        super(builder.baseMeta);
        this.tickSkip = builder.tickSkip;
        this.height = builder.hight;
        this.circleRadius = builder.circleRadius;
        this.circleType = builder.circleType;
    }

    public static Builder newBuilder(CircleType circleType) {
        return new Builder(circleType);
    }

    @Override
    public boolean cast(Player player) {
        Location pLoc = player.getLocation();
        castEffects.accept(pLoc);
        Location location;
        if (circleType == CircleType.RELATIVE) {
            location = pLoc.clone().add(0, height, 0);
        } else if (circleType == CircleType.TARGET) {
            location = SpellEffectUtil.getSpellLocation()
        }
        Location[] locations = SpellEffectUtil.getCircleFrom(pLoc.clone().add(0, height, 0), circleRadius);
        new BukkitRunnable() {
            int index = 0;
            @Override
            public void run() {
                for (int i = 0; i < tickSkip; i++) {
                    index++;
                    if (index >= locations.length) {
                        cancel();
                        EffectUtil.getNearbyLivingEntities(player, pLoc, effectRadius)
                                .forEach(entiy -> {
                                    if (entityDamage != 0) entiy.damage(entityDamage);
                                    entityEffects.accept(entiy);
                                });
                    } else {
                        Location loc = locations[index];
                        visualEffects.accept(loc);
                    }
                }
            }
        }.runTaskTimer(plugin, 1, 1);
        return true;
    }

    @Override
    public String toString() {
        return super.toString() + "Radius: " + circleRadius
                + "\nHightt: " + height
                + "\nTickSkip: " + tickSkip + " ticks";
    }

    public static class Builder extends AbstractBuilder<Builder> {
        private int circleRadius = 0;
        private int hight = 0;
        private int tickSkip = 1;
        private CircleType circleType = CircleType.RELATIVE;

        private Builder() {
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
            return self();
        }

        public Builder setCircleHight(int height) {
            this.hight = height;
            return self();
        }

        public Builder setTickSkip(int tickSkip) {
            this.tickSkip = Math.max(1, tickSkip);
            return self();
        }

        public static final class Builder2 extends Builder {
            private Builder2() {
                this.
            }
            @Override
            Builder self() {
                return super.self();
            }

            @Override
            public Behaviour build() {
                return super.build();
            }
        }
    }
    public enum CircleType {
        RELATIVE,
        TARGET
    }
}

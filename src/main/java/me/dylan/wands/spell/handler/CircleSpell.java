package me.dylan.wands.spell.handler;

import me.dylan.wands.util.EffectUtil;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public final class CircleSpell extends Behaviour {
    private final int tickSkip;
    private final int height;
    private final float circleRadius;
    private final int density;
    private final double increment;

    private CircleSpell(Builder builder) {
        super(builder.baseMeta);
        this.tickSkip = builder.tickSkip;
        this.height = builder.hight;
        this.circleRadius = builder.circleRadius;
        this.density = (int) Math.ceil(circleRadius * 2 * Math.PI);
        this.increment = (2 * Math.PI) / density;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public boolean cast(Player player) {
        Location pLoc = player.getLocation();
        castEffects.accept(pLoc);
        Location[] locations = getCircleFrom(pLoc.clone().add(0, height, 0));
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

    private Location[] getCircleFrom(Location location) {
        World world = location.getWorld();
        Location[] locations = new Location[density];
        double angle = 0;
        double originX = location.getX();
        double originY = location.getY();
        double originZ = location.getZ();
        for (int i = 0; i < density; i++) {
            angle += increment;
            double newX = originX + (circleRadius * Math.cos(angle));
            double newZ = originZ + (circleRadius * Math.sin(angle));
            locations[i] = new Location(world, newX, originY, newZ);
        }
        return locations;
    }

    @Override
    public String toString() {
        return super.toString() + "Radius: " + circleRadius
                +"\nHightt: " + height
                + "\nTickSkip: " + tickSkip + " ticks";
    }


    public static final class Builder extends AbstractBuilder<Builder> {
        private int circleRadius;
        private int hight;
        private int tickSkip = 1;

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
            return this;
        }

        public Builder setCircleHight(int height) {
            this.hight = height;
            return this;
        }


        public Builder setTickSkip(int tickSkip) {
            this.tickSkip = Math.max(1, tickSkip);
            return this;
        }
    }
}

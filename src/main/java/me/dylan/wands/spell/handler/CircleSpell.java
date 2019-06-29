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

    private CircleSpell(Builder builder) {
        super(builder.baseMeta);
        this.tickSkip = builder.tickSkip;
        this.height = builder.height;
        this.circleRadius = builder.circleRadius;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public boolean cast(Player player) {
        Location pLoc = player.getLocation();
        castEffects.accept(pLoc);
        Location location = pLoc.clone().add(0, height, 0);
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
                                .forEach(entity -> {
                                    if (entityDamage != 0) entity.damage(entityDamage);
                                    entityEffects.accept(entity);
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
                + "\nHeight: " + height
                + "\nTickSkip: " + tickSkip + " ticks";
    }

    public static class Builder extends AbstractBuilder<Builder> {
        private int circleRadius = 0;
        private int height = 0;
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
            return self();
        }

        public Builder setCircleHeight(int height) {
            this.height = height;
            return self();
        }

        public Builder setTickSkip(int tickSkip) {
            this.tickSkip = Math.max(1, tickSkip);
            return self();
        }
    }
}

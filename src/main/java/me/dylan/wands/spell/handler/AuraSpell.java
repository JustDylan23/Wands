package me.dylan.wands.spell.handler;

import me.dylan.wands.Main;
import me.dylan.wands.util.EffectUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public final class AuraSpell extends Behaviour {
    private final int effectDistance;
    private final int effectDuration;

    private AuraSpell(Builder builder) {
        super(builder.baseMeta);
        this.effectDistance = builder.effectDistance;
        this.effectDuration = builder.effectDuration;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public boolean cast(Player player) {
        castEffects.accept(player.getLocation());
        new BukkitRunnable() {
            int count = 0;

            @Override
            public void run() {
                if (++count > effectDuration) cancel();
                else {
                    Location loc = player.getLocation();
                    visualEffects.accept(player.getLocation());
                    EffectUtil.getNearbyLivingEntities(player, loc, effectRadius).forEach(entity -> {
                        entity.damage(entityDamage);
                        EffectUtil.removeVelocity(entity);
                        entityEffects.accept(entity);
                    });
                }
            }
        }.runTaskTimer(Main.getPlugin(), 1, 1);
        return true;
    }

    @Override
    public String toString() {
        return super.toString() + "Effect distance: " + effectDistance
                + "\nEffect duration: " + effectDuration + " ticks";
    }


    public static class Builder extends AbstractBuilder<Builder> {

        private int effectDistance;
        private int effectDuration;

        private Builder() {
        }

        @Override
        Builder self() {
            return this;
        }

        @Override
        public Behaviour build() {
            return new AuraSpell(this);
        }

        public Builder setEffectDistance(int effectDistance) {
            this.effectDistance = effectDistance;
            return self();
        }

        public Builder setEffectDuration(int effectDuration) {
            this.effectDuration = effectDuration;
            return self();
        }
    }
}

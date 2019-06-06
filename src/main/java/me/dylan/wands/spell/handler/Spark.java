package me.dylan.wands.spell.handler;

import me.dylan.wands.util.EffectUtil;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public final class Spark extends Behaviour {
    private final int effectDistance;

    private Spark(Builder builder) {
        super(builder.baseMeta);
        this.effectDistance = builder.effectDistance;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public boolean cast(Player player) {
        Location loc = getSpellLocation(player);
        castEffects.accept(player.getLocation());
        visualEffects.accept(loc);
        EffectUtil.getNearbyDamageables(player, loc, effectAreaRange).forEach(entity -> {
            EffectUtil.damage(entityDamage, player, entity);
            EffectUtil.removeVelocity(entity);
            entityEffects.accept(entity);
        });
        return true;
    }

    private Location getSpellLocation(Player player) {
        Entity entity = player.getTargetEntity(effectDistance);
        if (entity != null) {
            return entity.getLocation().add(0, 0.5, 0);
        }
        Block block = player.getTargetBlock(effectDistance);
        if (block != null) {
            return block.getLocation().toCenterLocation().subtract(player.getLocation().getDirection().normalize());
        }
        return player.getLocation();
    }

    public static class Builder extends AbstractBuilder<Builder> {

        private int effectDistance;

        private Builder() {
        }

        @Override
        Builder self() {
            return this;
        }

        @Override
        public Behaviour build() {
            return new Spark(this);
        }

        public Builder setEffectDistance(int effectDistance) {
            this.effectDistance = effectDistance;
            return self();
        }
    }
}
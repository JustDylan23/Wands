package me.dylan.wands.spell.handler;

import me.dylan.wands.util.EffectUtil;
import me.dylan.wands.util.ShorthandUtil;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public final class BurstAuraSpell extends Behaviour {

    private final Consumer<LivingEntity> playerEffects;

    private BurstAuraSpell(Builder builder) {
        super(builder.baseMeta);
        this.playerEffects = builder.playerEffects;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public boolean cast(Player player) {
        Location loc = player.getLocation();
        playerEffects.accept(player);
        castEffects.accept(loc);
        visualEffects.accept(loc);
        EffectUtil.getNearbyLivingEntities(player, loc, effectRadius).forEach(entity -> {
            if (entityDamage != 0) entity.damage(entityDamage);
            entityEffects.accept(entity);
        });
        return true;
    }

    public static final class Builder extends AbstractBuilder<Builder> {
        private Consumer<LivingEntity> playerEffects = ShorthandUtil.emptyConsumer();

        private Builder() {
        }

        @Override
        Builder self() {
            return this;
        }

        @Override
        public Behaviour build() {
            return new BurstAuraSpell(this);
        }

        public Builder setPlayerEffects(Consumer<LivingEntity> playerEffects) {
            this.playerEffects = playerEffects;
            return this;
        }
    }
}

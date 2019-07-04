package me.dylan.wands.spell.handler;

import me.dylan.wands.spell.SpellEffectUtil;
import me.dylan.wands.util.EffectUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public final class SparkSpell extends Behaviour {
    private final int effectDistance;

    private SparkSpell(Builder builder) {
        super(builder.baseMeta);
        this.effectDistance = builder.effectDistance;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public boolean cast(Player player) {
        Location loc = SpellEffectUtil.getSpellLocation(effectDistance, player);
        castSounds.play(player);
        spellRelativeEffects.accept(loc);
        EffectUtil.getNearbyLivingEntities(player, loc, spellEffectRadius)
                .forEach(entity -> {
                    if (affectedEntityDamage != 0) entity.damage(affectedEntityDamage);
                    affectedEntityEffects.accept(entity);
                });
        return true;
    }

    @Override
    public String toString() {
        return super.toString() + "Effect distance: " + effectDistance;
    }

    public static final class Builder extends AbstractBuilder<Builder> {

        private int effectDistance;

        private Builder() {
        }

        @Override
        Builder self() {
            return this;
        }

        @Override
        public Behaviour build() {
            return new SparkSpell(this);
        }

        public Builder setEffectDistance(int effectDistance) {
            this.effectDistance = effectDistance;
            return this;
        }
    }
}

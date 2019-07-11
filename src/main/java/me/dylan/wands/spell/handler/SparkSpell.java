package me.dylan.wands.spell.handler;

import me.dylan.wands.spell.SpellEffectUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public final class SparkSpell extends Behaviour {
    private final int effectDistance;

    private SparkSpell(Builder builder) {
        super(builder.baseMeta);
        this.effectDistance = builder.effectDistance;
        addStringProperty("Effect distance", effectDistance, "meters");
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public boolean cast(Player player, String wandDisplayName) {
        Location loc = SpellEffectUtil.getSpellLocation(effectDistance, player);
        castSounds.play(player);
        spellRelativeEffects.accept(loc);
        spellRelativeEffects2.accept(loc, player);
        applyEntityEffects(loc, player, wandDisplayName);
        return true;
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

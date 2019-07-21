package me.dylan.wands.spell.handler;

import me.dylan.wands.spell.SpellEffectUtil;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public final class SparkSpell extends Behaviour {
    private final int effectDistance;
    private final boolean requireLivingTarget;
    private final Target target;

    private SparkSpell(Builder builder) {
        super(builder.baseMeta);
        this.effectDistance = builder.effectDistance;
        this.requireLivingTarget = builder.requireLivingTarget;
        this.target = builder.target;


        addStringProperty("Effect distance", effectDistance, "meters");
        addStringProperty("Require Living Target", requireLivingTarget);
        addStringProperty("Target", target, "meters");

    }

    public static Builder newBuilder(Target target) {
        return new Builder(target);
    }

    @Override
    public boolean cast(Player player, String wandDisplayName) {
        Location loc = SpellEffectUtil.getSpellLocation(effectDistance, player, requireLivingTarget);
        if (loc == null) {
            return false;
        }
        castSounds.play(player);
        spellRelativeEffects.accept(loc, loc.getWorld());
        spellRelativeEffects2.accept(loc, player);
        for (LivingEntity entity : SpellEffectUtil.getNearbyLivingEntities(player, loc, spellEffectRadius)) {
            push(entity, loc, player);
            SpellEffectUtil.damageEffect(player, entity, affectedEntityDamage, wandDisplayName);
            entityEffects.accept(entity);
            if (target == Target.SINGLE) {
                break;
            }
        }
        return true;
    }

    public enum Target {
        SINGLE,
        MULTI
    }

    public static final class Builder extends AbstractBuilder<Builder> {

        private final Target target;
        private int effectDistance;
        private boolean requireLivingTarget;

        private Builder(Target target) {
            this.target = target;
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

        public Builder requireLivingTarget(boolean b) {
            this.requireLivingTarget = b;
            return this;
        }
    }
}

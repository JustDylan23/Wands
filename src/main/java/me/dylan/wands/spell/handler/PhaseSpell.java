package me.dylan.wands.spell.handler;

import me.dylan.wands.spell.SpellEffectUtil;
import me.dylan.wands.util.Common;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

public final class PhaseSpell extends Behaviour {
    private final int effectDistance;
    private final String tagPhaseSpell;
    private final Target target;
    private final Predicate<LivingEntity> condition;
    private final Consumer<LivingEntity> duringPhaseEffect;
    private final BiConsumer<LivingEntity, Player> afterPhaseEffect;
    private final boolean requireLivingTarget;

    private PhaseSpell(Builder builder) {
        super(builder.baseMeta);
        this.effectDistance = builder.effectDistance;
        this.condition = builder.condition;
        this.duringPhaseEffect = builder.duringPhaseEffect;
        this.afterPhaseEffect = builder.afterPhaseEffect;
        this.target = builder.target;
        this.tagPhaseSpell = UUID.randomUUID().toString();
        this.requireLivingTarget = builder.requireLivingTarget;

        addStringProperty("Effect distance", effectDistance, "meters");
        addStringProperty("Target", target, "meters");
        addStringProperty("Require Living Target", requireLivingTarget);
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
        for (LivingEntity entity : SpellEffectUtil.getNearbyLivingEntities(player, loc, entity -> !entity.hasMetadata(tagPhaseSpell), spellEffectRadius)) {
            entity.setMetadata(tagPhaseSpell, Common.METADATA_VALUE_TRUE);
            push(entity, loc, player);
            entityEffects.accept(entity);
            SpellEffectUtil.damageEffect(player, entity, affectedEntityDamage, wandDisplayName);
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (!entity.isValid() || condition.test(entity)) {
                        afterPhaseEffect.accept(entity, player);
                        entity.removeMetadata(tagPhaseSpell, plugin);
                        cancel();
                    } else {
                        duringPhaseEffect.accept(entity);
                    }
                }
            }.runTaskTimer(plugin, 2, 1);
            if (target == Target.SINGLE) break;

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
        private Predicate<LivingEntity> condition = (entity) -> true;
        private Consumer<LivingEntity> duringPhaseEffect = Common.emptyConsumer();
        private BiConsumer<LivingEntity, Player> afterPhaseEffect = (livingEntity, player) -> {
        };
        private boolean requireLivingTarget = false;

        private Builder(Target target) {
            this.target = target;
        }

        @Override
        Builder self() {
            return this;
        }

        @Override
        public Behaviour build() {
            return new PhaseSpell(this);
        }

        public Builder setEffectDistance(int effectDistance) {
            this.effectDistance = effectDistance;
            return this;
        }

        public Builder setStagePassCondition(Predicate<LivingEntity> condition) {
            this.condition = condition;
            return this;
        }

        public Builder setEffectsDuringPhase(Consumer<LivingEntity> effect) {
            this.duringPhaseEffect = effect;
            return this;
        }

        public Builder setEffectAfterPhase(BiConsumer<LivingEntity, Player> effect) {
            this.afterPhaseEffect = effect;
            return this;
        }

        public Builder requireLivingTarget(boolean b) {
            this.requireLivingTarget = b;
            return this;
        }
    }
}


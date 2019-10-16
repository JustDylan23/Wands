package me.dylan.wands.spell.spellbuilders;

import me.dylan.wands.spell.accessories.SpellInfo;
import me.dylan.wands.spell.util.SpellEffectUtil;
import me.dylan.wands.utils.Common;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Has 3 stages of execution.
 * <p>
 * Stages:
 * 1. Effects entities like {@link Spark}.
 * 2. Applies effects to entity until {@link #condition} is met.
 * 3. After {@link #condition} is met applies effects to entity
 * <p>
 * Configurable:
 * - Condition to pass stage.
 * - Effects constantly applied during phase.
 * - Effects applied after stage.
 */
public final class Phase extends BuildableBehaviour {
    private final Target target;
    private final String tagPhaseSpell;
    private final Predicate<LivingEntity> condition;
    private final Consumer<LivingEntity> duringPhaseEffect;
    private final BiConsumer<LivingEntity, Player> afterPhaseEffect;
    private final KnockBackDirection knockBackFrom;
    private final int effectDistance;

    private Phase(@NotNull Builder builder) {
        super(builder.baseProps);
        this.target = builder.target;
        this.effectDistance = builder.effectDistance;
        this.condition = builder.condition;
        this.duringPhaseEffect = builder.duringPhaseEffect;
        this.afterPhaseEffect = builder.afterPhaseEffect;
        this.knockBackFrom = builder.knockBackFrom;
        this.tagPhaseSpell = UUID.randomUUID().toString();

        addPropertyInfo("Effect distance", effectDistance, "meters");
        addPropertyInfo("Target", target);
    }

    public static @NotNull Builder newBuilder(Target target) {
        return new Builder(target);
    }

    @Override
    public boolean cast(@NotNull Player player, @NotNull String weaponName) {
        Location targetLoc = SpellEffectUtil.getSpellLocation(player, effectDistance);
        SpellInfo spellInfo = new SpellInfo(player, player.getLocation(), targetLoc);
        castSounds.play(player);
        spellRelativeEffects.accept(targetLoc, spellInfo);
        Location pushFrom = (knockBackFrom == KnockBackDirection.SPELL) ? targetLoc : player.getLocation();
        for (LivingEntity entity : SpellEffectUtil.getNearbyLivingEntities(player, targetLoc, entity -> !entity.hasMetadata(tagPhaseSpell), spellEffectRadius)) {
            entity.setMetadata(tagPhaseSpell, Common.getMetadataValueTrue());
            knockBack.apply(entity, pushFrom);
            entityEffects.accept(entity, spellInfo);
            for (PotionEffect potionEffect : potionEffects) {
                entity.addPotionEffect(potionEffect, true);
            }
            SpellEffectUtil.damageEffect(player, entity, entityDamage, weaponName);
            BukkitRunnable bukkitRunnable = new BukkitRunnable() {
                @Override
                public void run() {
                    if (!entity.isValid() || condition.test(entity)) {
                        afterPhaseEffect.accept(entity, player);
                        Common.removeMetaData(entity, tagPhaseSpell);
                        cancel();
                    } else {
                        duringPhaseEffect.accept(entity);
                    }
                }
            };
            Common.runTaskTimer(bukkitRunnable, 2, 1);
            if (target == Target.SINGLE) break;

        }
        return true;
    }

    public static final class Builder extends AbstractBuilder<Builder> {
        private final Target target;
        private int effectDistance;
        private Predicate<LivingEntity> condition = Common.emptyPredicate();
        private Consumer<LivingEntity> duringPhaseEffect = Common.emptyConsumer();
        private BiConsumer<LivingEntity, Player> afterPhaseEffect = Common.emptyBiConsumer();
        private KnockBackDirection knockBackFrom = KnockBackDirection.SPELL;

        private Builder(Target target) {
            this.target = target;
        }

        @Override
        Builder self() {
            return this;
        }

        @Override
        public @NotNull Behavior build() {
            return new Phase(this);
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

        public Builder setEffectsAfterPhase(BiConsumer<LivingEntity, Player> effect) {
            this.afterPhaseEffect = effect;
            return this;
        }

        public Builder knockBackFrom(KnockBackDirection direction) {
            this.knockBackFrom = direction;
            return this;
        }
    }
}


package me.dylan.wands.spell.spellbuilders;

import me.dylan.wands.spell.accessories.SpellInfo;
import me.dylan.wands.spell.util.SpellEffectUtil;
import me.dylan.wands.utils.PlayerUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Deals direct effects to entities near target
 * <p>
 * Configurable:
 * - Distance of target
 * - Require living target to execute
 * - Can effect single or multiple entities
 */

public final class Spark extends BuildableBehaviour {
    private final int effectDistance;
    private final Target target;

    private Spark(@NotNull Builder builder) {
        super(builder);
        this.effectDistance = builder.effectDistance;
        this.target = builder.target;

        addPropertyInfo("Effect distance", effectDistance, "meters");
        addPropertyInfo("Target", target);
    }

    public static @NotNull Builder newBuilder(Target target) {
        return new Builder(target);
    }

    @Override
    public boolean cast(@NotNull Player player, @NotNull String weapon) {
        if (target == Target.MULTI) {
            Location targetLoc = SpellEffectUtil.getSpellLocation(player, effectDistance);
            SpellInfo spellInfo = new SpellInfo(player, player.getLocation(), targetLoc);
            spellRelativeEffects.accept(targetLoc, spellInfo);
            for (LivingEntity entity : SpellEffectUtil.getNearbyLivingEntities(player, targetLoc, spellEffectRadius)) {
                applyEffects(entity, spellInfo, targetLoc, weapon);
            }
        } else {
            Entity entity = PlayerUtil.getTargetEntity(player, effectDistance, e -> true);
            if (!(entity instanceof LivingEntity)) {
                PlayerUtil.sendActionBar(player, "§cselect an entity");
                Location loc = PlayerUtil.getTargetLocation(player, 10);
                player.spawnParticle(Particle.FIREWORKS_SPARK, loc, 5, 0, 0, 0, 0.1);
                return false;
            }
            LivingEntity livingEntity = (LivingEntity) entity;
            Location entityLoc = livingEntity.getLocation();
            SpellInfo spellInfo = new SpellInfo(player, player.getLocation(), entityLoc);
            spellRelativeEffects.accept(entityLoc, spellInfo);
            applyEffects(livingEntity, spellInfo, spellInfo.origin(), weapon);

        }
        castSounds.play(player);
        return true;
    }

    private void applyEffects(LivingEntity livingEntity, @NotNull SpellInfo spellInfo, Location knockbackFrom, String weapon) {
        knockBack.apply(livingEntity, knockbackFrom);
        SpellEffectUtil.damageEffect(spellInfo.caster(), livingEntity, entityDamage, weapon);
        entityEffects.accept(livingEntity, spellInfo);
        applyPotionEffects(livingEntity);
    }

    public enum Target {
        MULTI,
        SINGLE_REQUIRED
    }

    public static final class Builder extends AbstractBuilder<Builder> {
        private final Target target;

        private int effectDistance = 10;

        private Builder(Target target) {
            this.target = target;
        }

        @Override
        Builder self() {
            return this;
        }

        @Override
        public @NotNull Behavior build() {
            return new Spark(this);
        }

        public Builder setEffectDistance(int effectDistance) {
            this.effectDistance = effectDistance;
            return this;
        }

    }
}

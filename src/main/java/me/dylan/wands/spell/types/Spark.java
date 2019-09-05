package me.dylan.wands.spell.types;

import me.dylan.wands.spell.tools.SpellInfo;
import me.dylan.wands.spell.util.SpellEffectUtil;
import org.bukkit.Location;
import org.bukkit.Sound;
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

public final class Spark extends Behavior {
    private final int effectDistance;
    private final Target target;


    private Spark(@NotNull Builder builder) {
        super(builder.baseProps);
        this.effectDistance = builder.effectDistance;
        this.target = builder.target;


        addPropertyInfo("Effect distance", effectDistance, "meters");
        addPropertyInfo("Target", target);

    }

    public static @NotNull Builder newBuilder(Target target) {
        new Builder(Target.MULTI).setCastSound(Sound.ENTITY_ARROW_HIT_PLAYER).setEffectDistance(3);
        return new Builder(target);
    }

    @Override
    public boolean cast(@NotNull Player player, @NotNull String weaponName) {
        Location loc = SpellEffectUtil.getSpellLocation(effectDistance, player);
        if (loc == null) {
            return false;
        }
        SpellInfo spellInfo = new SpellInfo(player, player.getLocation(), loc);
        castSounds.play(player);
        spellRelativeEffects.accept(loc, loc.getWorld());
        extendedSpellRelativeEffects.accept(loc, spellInfo);
        for (LivingEntity entity : SpellEffectUtil.getNearbyLivingEntities(player, loc, spellEffectRadius)) {
            knockBack.apply(entity, loc);
            SpellEffectUtil.damageEffect(player, entity, entityDamage, weaponName);
            entityEffects.accept(entity);
            if (target == Target.SINGLE) {
                break;
            }
        }
        return true;
    }

    public static final class Builder extends AbstractBuilder<Builder> {
        private final Target target;
        private int effectDistance;

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

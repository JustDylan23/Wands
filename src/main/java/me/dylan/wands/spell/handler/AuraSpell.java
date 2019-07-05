package me.dylan.wands.spell.handler;

import me.dylan.wands.Main;
import me.dylan.wands.util.Common;
import me.dylan.wands.util.EffectUtil;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.Consumer;

/**
 * AuraSpell is a type of Behaviour which is executed relative to the player.
 * The distance in which entities will be affected can vary. The player itself
 * can also be given effects upon executing the spell that has implemented this behaviour.
 * <p>
 * setEffectDuration is meant for setting the amount of ticks
 * {@link Builder#setEffectDuration(int)}
 */

//todo add enum EffectRate ONCE, REPEATEDLY

public final class AuraSpell extends Behaviour {
    private static int instanceCount = 0;
    private final EffectRate effectRate;
    private final int effectDuration;
    private final String hasAura;
    private final Consumer<LivingEntity> playerEffects, reverseAuraEffects;

    private AuraSpell(Builder builder) {
        super(builder.baseMeta);
        instanceCount++;
        this.effectRate = builder.effectRate;
        this.effectDuration = builder.effectDuration;
        this.hasAura = "HAS_AURA;ID#" + instanceCount;
        this.playerEffects = builder.playerEffects;
        this.reverseAuraEffects = builder.reverseAuraEffects;
    }

    public static Builder newBuilder(EffectRate effectRate) {
        return new Builder(effectRate);
    }

    @Override
    public boolean cast(Player player) {
        castSounds.play(player);
        if (player.hasMetadata(hasAura)) return false;
        player.setMetadata(hasAura, Common.METADATA_VALUE_TRUE);
        playerEffects.accept(player);
        new BukkitRunnable() {
            int count = 0;
            boolean repeat = true;
            boolean hasAffected = false;

            @Override
            public void run() {
                if (++count > effectDuration) {
                    cancel();
                    player.removeMetadata(hasAura, plugin);
                    if (!hasAffected) {
                        reverseAuraEffects.accept(player);
                    }
                } else {
                    Location loc = player.getLocation();
                    spellRelativeEffects.accept(loc);
                    if (repeat) {
                        EffectUtil.getNearbyLivingEntities(player, loc, spellEffectRadius)
                                .forEach(entity -> {
                                    if (effectRate == EffectRate.ONCE) {
                                        repeat = false;
                                    }
                                    if (affectedEntityDamage != 0) {
                                        entity.damage(affectedEntityDamage);
                                    }
                                    hasAffected = true;
                                    affectedEntityEffects.accept(entity);
                                });
                    }
                }
            }
        }.runTaskTimer(Main.getPlugin(), 1, 1);
        return true;
    }

    @Override
    public String toString() {
        return super.toString() + "Effect duration: " + effectDuration + " ticks";
    }

    public enum EffectRate {
        ONCE,
        REPEATEDLY
    }

    public static final class Builder extends AbstractBuilder<Builder> {
        private Consumer<LivingEntity> reverseAuraEffects = Common.emptyConsumer();
        private Consumer<LivingEntity> playerEffects = Common.emptyConsumer();
        private int effectDuration;
        private final EffectRate effectRate;

        private Builder(EffectRate effectRate) {
            this.effectRate = effectRate;
        }

        @Override
        Builder self() {
            return this;
        }

        @Override
        public Behaviour build() {
            return new AuraSpell(this);
        }

        public Builder setEffectDuration(int ticks) {
            this.effectDuration = ticks;
            return this;
        }

        public Builder setPlayerEffects(Consumer<LivingEntity> playerEffects) {
            this.playerEffects = playerEffects;
            return this;
        }

        public Builder setReverseAuraEffects(Consumer<LivingEntity> playerEffects) {
            this.reverseAuraEffects = playerEffects;
            return this;
        }
    }
}

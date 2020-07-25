package me.dylan.wands.spell.spellbuilders;

import me.dylan.wands.spell.accessories.SpellInfo;
import me.dylan.wands.spell.util.SpellEffectUtil;
import me.dylan.wands.utils.Common;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.function.Consumer;

/**
 * Emits an aura around the player for an x amount of time
 * <p>
 * configurable:
 * - Duration of aura
 * - Frequency of which aura effect is applied
 * - Effects which player receives on activation
 * - Effects which player receives if aura effected nothing excluding the player
 */
public final class Aura extends BuildableBehaviour {
    private final EffectFrequency effectFrequency;
    private final int effectDuration;
    private final String auraUUID;
    private final Consumer<LivingEntity> playerEffects;
    private final AuraParticleType auraParticleType;

    private Aura(@NotNull Builder builder) {
        super(builder);
        this.effectFrequency = builder.effectFrequency;
        this.effectDuration = builder.effectDuration;
        this.auraUUID = UUID.randomUUID().toString();
        this.playerEffects = builder.playerEffects;
        this.auraParticleType = builder.auraParticleType;

        addPropertyInfo("Aura duration", effectDuration, "ticks");
        addPropertyInfo("Effect quantity", effectFrequency);
        addPropertyInfo("Aura particle type", auraParticleType);
    }

    public static @NotNull Builder newBuilder(EffectFrequency effectFrequency) {
        return new Builder(effectFrequency);
    }

    @Override
    public boolean cast(@NotNull Player player, @NotNull String weapon) {
        if (player.hasMetadata(auraUUID)) {
            return false;
        }
        player.setMetadata(auraUUID, Common.getMetadataValueTrue());
        castSounds.play(player);
        playerEffects.accept(player);

        SpellInfo spellInfo = new SpellInfo(player, player.getLocation(), null) {
            @Override
            public Location spellLocation() {
                return player.getLocation();
            }
        };

        BukkitRunnable bukkitRunnable = new BukkitRunnable() {
            int count = 0;
            boolean repeat = true;

            @Override
            public void run() {
                ++count;
                if (count > effectDuration) {
                    cancel();
                    Common.removeMetaData(player, auraUUID);
                } else {
                    Location loc = player.getLocation();
                    if (auraParticleType == AuraParticleType.EMIT_AROUND_CENTER) {
                        spellRelativeEffects.accept(loc, spellInfo);
                    } else {
                        for (Location location : SpellEffectUtil.getHorizontalCircleFrom(loc, spellEffectRadius, 0, 1)) {
                            spellRelativeEffects.accept(location, spellInfo);
                        }
                    }
                    if (repeat) {
                        for (LivingEntity livingEntity : SpellEffectUtil.getNearbyLivingEntities(player, loc, spellEffectRadius)) {
                            if (effectFrequency == EffectFrequency.ONCE) {
                                repeat = false;
                            }
                            knockBack.apply(livingEntity, loc);
                            SpellEffectUtil.damageEffect(player, livingEntity, entityDamage, weapon);
                            entityEffects.accept(livingEntity, spellInfo);
                            applyPotionEffects(livingEntity);
                        }
                    }
                }
            }
        };
        Common.runTaskTimer(bukkitRunnable, 1, 1);
        return true;
    }

    public enum EffectFrequency {
        ONCE,
        CONSTANT
    }

    public enum AuraParticleType {
        EMIT_AROUND_CENTER,
        EMIT_AS_CIRCLE
    }

    public static final class Builder extends AbstractBuilder<Builder> {
        private final EffectFrequency effectFrequency;

        private Consumer<LivingEntity> playerEffects = Common.emptyConsumer();
        private int effectDuration = 20;
        private AuraParticleType auraParticleType = AuraParticleType.EMIT_AROUND_CENTER;

        private Builder(EffectFrequency effectFrequency) {
            this.effectFrequency = effectFrequency;
        }

        @Override
        Builder self() {
            return this;
        }

        @Override
        public @NotNull Behavior build() {
            return new Aura(this);
        }

        public Builder setEffectDuration(int ticks) {
            this.effectDuration = ticks;
            return this;
        }

        public Builder setPlayerEffects(Consumer<LivingEntity> playerEffects) {
            this.playerEffects = playerEffects;
            return this;
        }

        public Builder setAuraParticleType(AuraParticleType auraParticles) {
            this.auraParticleType = auraParticles;
            return this;
        }
    }
}

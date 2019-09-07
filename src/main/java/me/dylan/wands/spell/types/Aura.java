package me.dylan.wands.spell.types;

import me.dylan.wands.miscellaneous.utils.Common;
import me.dylan.wands.spell.tools.SpellInfo;
import me.dylan.wands.spell.util.SpellEffectUtil;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
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
public final class Aura extends Behavior {
    private final EffectFrequency effectFrequency;
    private final int effectDuration;
    private final String auraUUID;
    private final Consumer<LivingEntity> playerEffects, reverseAuraEffects;
    private final AuraParticleType auraParticleType;

    private Aura(@NotNull Builder builder) {
        super(builder.baseProps);
        this.effectFrequency = builder.effectFrequency;
        this.effectDuration = builder.effectDuration;
        this.auraUUID = UUID.randomUUID().toString();
        this.playerEffects = builder.playerEffects;
        this.reverseAuraEffects = builder.reverseAuraEffects;
        this.auraParticleType = builder.auraParticleType;

        addPropertyInfo("Aura duration", effectDuration, "ticks");
        addPropertyInfo("Effect quantity", effectFrequency);
        addPropertyInfo("Aura particle type", auraParticleType);
    }

    public static @NotNull Builder newBuilder(EffectFrequency effectFrequency) {
        return new Builder(effectFrequency);
    }

    @Override
    public boolean cast(@NotNull Player player, @NotNull String weaponName) {
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
            int count;
            boolean repeat = true;
            boolean hasAffected;

            @Override
            public void run() {
                ++count;
                if (count > effectDuration) {
                    cancel();
                    Common.removeMetaData(player, auraUUID);
                    if (!hasAffected) {
                        reverseAuraEffects.accept(player);
                    }
                } else {
                    Location loc = player.getLocation();
                    if (auraParticleType == AuraParticleType.CENTER) {
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
                            SpellEffectUtil.damageEffect(player, livingEntity, entityDamage, weaponName);
                            hasAffected = true;
                            entityEffects.accept(livingEntity, spellInfo);
                            for (PotionEffect potionEffect : potionEffects) {
                                livingEntity.addPotionEffect(potionEffect, true);
                            }
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
        CENTER,
        CIRCLE
    }

    public static final class Builder extends AbstractBuilder<Builder> {
        private final EffectFrequency effectFrequency;
        private Consumer<LivingEntity> reverseAuraEffects = Common.emptyConsumer();
        private Consumer<LivingEntity> playerEffects = Common.emptyConsumer();
        private int effectDuration;
        private AuraParticleType auraParticleType = AuraParticleType.CENTER;

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

        public Builder setReverseAuraEffects(Consumer<LivingEntity> playerEffects) {
            this.reverseAuraEffects = playerEffects;
            return this;
        }

        public Builder setAuraParticleType(AuraParticleType auraParticles) {
            this.auraParticleType = auraParticles;
            return this;
        }
    }
}

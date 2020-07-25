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

/**
 * Creates circles with increasing size around the player to simulate a shock wave
 * <p>
 * Configurable:
 * - Radius to which the wave will extend.
 * - The delay before the wave grows half a meter
 */
public final class ShockWave extends BuildableBehaviour {
    private final int waveRadius, delay;
    private final String tagShockWave;

    private ShockWave(@NotNull Builder builder) {
        super(builder);
        this.waveRadius = builder.waveRadius;
        this.delay = builder.delay;
        this.tagShockWave = UUID.randomUUID().toString();

        addPropertyInfo("Radius", waveRadius, "meters");
    }

    public static @NotNull Builder newBuilder() {
        return new Builder();
    }

    @Override
    public boolean cast(@NotNull Player player, @NotNull String weapon) {
        castSounds.play(player);
        Location waveCenter = player.getLocation();
        SpellInfo spellInfo = new SpellInfo(player, waveCenter, waveCenter);
        BukkitRunnable bukkitRunnable = new BukkitRunnable() {
            float currentRadius = 0.0F;

            @Override
            public void run() {
                currentRadius += 0.5f;
                if (currentRadius > waveRadius) {
                    cancel();
                } else {
                    for (Location loc : SpellEffectUtil.getHorizontalCircleFrom(waveCenter, currentRadius, 0, 1)) {
                        spellRelativeEffects.accept(loc, spellInfo);
                    }
                    int cooldownTime = (int) ((waveRadius - currentRadius) * delay + 1);

                    for (LivingEntity entity : SpellEffectUtil.getNearbyLivingEntities(player, waveCenter, entity -> !entity.hasMetadata(tagShockWave), currentRadius, 2.0, currentRadius)) {
                        entity.setMetadata(tagShockWave, Common.getMetadataValueTrue());
                        SpellEffectUtil.damageEffect(player, entity, entityDamage, weapon);
                        entityEffects.accept(entity, spellInfo);
                        applyPotionEffects(entity);
                        knockBack.apply(entity, waveCenter);
                        Common.runTaskLater(() -> Common.removeMetaData(entity, tagShockWave), cooldownTime);
                    }
                }
            }
        };
        Common.runTaskTimer(bukkitRunnable, 0, delay);
        return true;
    }

    public static final class Builder extends AbstractBuilder<Builder> {
        private int waveRadius = 1, delay = 1;

        private Builder() {
        }

        @Override
        Builder self() {
            return this;
        }

        @Override
        public @NotNull Behavior build() {
            return new ShockWave(this);
        }

        public Builder setWaveRadius(int waveRadius) {
            this.waveRadius = waveRadius;
            return this;
        }

        public Builder setExpansionDelay(int ticks) {
            this.delay = ticks;
            return this;
        }
    }
}

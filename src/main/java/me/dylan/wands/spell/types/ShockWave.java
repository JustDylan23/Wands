package me.dylan.wands.spell.types;

import me.dylan.wands.spell.SpellEffectUtil;
import me.dylan.wands.spell.types.Base.AbstractBuilder.SpellInfo;
import me.dylan.wands.util.Common;
import org.bukkit.Bukkit;
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
public final class ShockWave extends Base {
    private final int waveRadius, delay;
    private final String tagShockWave;

    private ShockWave(@NotNull Builder builder) {
        super(builder.baseProps);
        this.waveRadius = builder.waveRadius;
        this.delay = builder.delay;
        this.tagShockWave = UUID.randomUUID().toString();

        addPropertyInfo("Radius", waveRadius, "meters");
    }

    @NotNull
    public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public boolean cast(@NotNull Player player, @NotNull String weaponName) {
        castSounds.play(player);
        Location waveCenter = player.getLocation();
        SpellInfo spellInfo = new SpellInfo(player, waveCenter, () -> waveCenter);
        new BukkitRunnable() {
            float currentRadius = 0;

            @Override
            public void run() {
                currentRadius += 0.5f;
                if (currentRadius > waveRadius) {
                    cancel();
                } else {
                    for (Location loc : SpellEffectUtil.getHorizontalCircleFrom(waveCenter, currentRadius, 0, 1)) {
                        spellRelativeEffects.accept(loc, loc.getWorld());
                        extendedSpellRelativeEffects.accept(loc, spellInfo);
                    }
                    for (LivingEntity entity : SpellEffectUtil.getNearbyLivingEntities(player, waveCenter, entity -> !entity.hasMetadata(tagShockWave), currentRadius, 2.0, currentRadius)) {
                        entity.setMetadata(tagShockWave, Common.METADATA_VALUE_TRUE);
                        SpellEffectUtil.damageEffect(player, entity, entityDamage, weaponName);
                        entityEffects.accept(entity);
                        knockBack.apply(entity, waveCenter);
                        Bukkit.getScheduler().runTaskLater(plugin, () -> entity.removeMetadata(tagShockWave, plugin), Math.round((waveRadius - currentRadius) * 2.0 * delay) + delay);
                    }
                }
            }
        }.runTaskTimer(plugin, 0, delay);
        return true;
    }

    public static final class Builder extends AbstractBuilder<Builder> {
        private int waveRadius, delay = 1;

        private Builder() {
        }

        @Override
        Builder self() {
            return this;
        }

        @Override
        public Base build() {
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

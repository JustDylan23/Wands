package me.dylan.wands.spell.handler;

import me.dylan.wands.spell.SpellEffectUtil;
import me.dylan.wands.util.Common;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

public final class ShockWaveSpell extends Behaviour {
    private final int waveRadius, delay;
    private final String tagShockWave;

    private ShockWaveSpell(Builder builder) {
        super(builder.baseMeta);
        this.waveRadius = builder.waveRadius;
        this.delay = builder.delay;
        this.tagShockWave = UUID.randomUUID().toString();

        addStringProperty("Radius", waveRadius, "meters");
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public boolean cast(Player player, String wandDisplayName) {
        castSounds.play(player);
        Location origin = player.getLocation();
        new BukkitRunnable() {
            float currentRadius = 0;

            @Override
            public void run() {
                currentRadius += 0.5f;
                if (currentRadius > waveRadius) {
                    cancel();
                } else {
                    SpellEffectUtil.getCircleFrom(origin, currentRadius).forEach(loc -> spellRelativeEffects.accept(loc, loc.getWorld()));
                    for (LivingEntity entity : SpellEffectUtil.getNearbyLivingEntities(player, origin, entity -> !entity.hasMetadata(tagShockWave), currentRadius)) {
                        entity.setMetadata(tagShockWave, Common.METADATA_VALUE_TRUE);
                        SpellEffectUtil.damageEffect(player, entity, affectedEntityDamage, wandDisplayName);
                        affectedEntityEffects.accept(entity);
                        push(entity, player.getLocation(), player);
                        Bukkit.getScheduler().runTaskLater(plugin, () -> {
                            entity.removeMetadata(tagShockWave, plugin);
                        }, Math.round((waveRadius - currentRadius) * 2D * delay) + delay);
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
        public Behaviour build() {
            return new ShockWaveSpell(this);
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

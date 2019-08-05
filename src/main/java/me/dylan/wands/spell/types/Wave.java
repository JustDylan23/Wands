package me.dylan.wands.spell.types;

import me.dylan.wands.spell.SpellEffectUtil;
import me.dylan.wands.util.Common;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Shoots a ray which goes through entities
 * <p>
 * Configurable:
 * - Speed in which the wave moves forward
 * - The maximum distance the wave can travel.
 */
public final class Wave extends Base {
    private final int effectDistance;
    private final int speed;
    private final String tagWaveSpell;

    private Wave(@NotNull Builder builder) {
        super(builder.baseProps);
        this.effectDistance = builder.effectDistance;
        this.speed = builder.speed;
        this.tagWaveSpell = UUID.randomUUID().toString();

        addPropertyInfo("Effect distance", effectDistance, "meters");
        addPropertyInfo("Meters per tick", speed, "meters");
    }

    @NotNull
    public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public boolean cast(@NotNull Player player, @NotNull String weaponName) {
        Vector direction = player.getLocation().getDirection().normalize();
        castSounds.play(player.getLocation().add(direction.clone().multiply(5)));
        Location origin = player.getEyeLocation();
        new BukkitRunnable() {
            int count = 0;

            @Override
            public void run() {
                for (int i = 0; i < speed; i++) {
                    count++;
                    if (count >= effectDistance) cancel();
                    Location loc = origin.add(direction).clone();
                    if (!loc.getBlock().isPassable()) {
                        cancel();
                        return;
                    }
                    spellRelativeEffects.accept(loc, loc.getWorld());
                    SpellEffectUtil.getNearbyLivingEntities(player, loc, entity -> !entity.hasMetadata(tagWaveSpell), spellEffectRadius).forEach(entity -> {
                        entity.setMetadata(tagWaveSpell, Common.METADATA_VALUE_TRUE);
                        SpellEffectUtil.damageEffect(player, entity, entityDamage, weaponName);
                        entityEffects.accept(entity);
                        Bukkit.getScheduler().runTaskLater(plugin, () -> {
                            if (entity.isValid()) {
                                entity.removeMetadata(tagWaveSpell, plugin);
                            }
                        }, effectDistance - count);
                    });
                }
            }
        }.runTaskTimer(plugin, 1, 1);
        return true;
    }

    public static final class Builder extends AbstractBuilder<Builder> {
        private int effectDistance;
        private int speed = 1;

        private Builder() {
        }

        @Override
        Builder self() {
            return this;
        }

        @NotNull
        public Wave build() {
            return new Wave(this);
        }

        public Builder setEffectDistance(int effectDistance) {
            this.effectDistance = effectDistance;
            return this;
        }

        public Builder setMetersPerTick(int speed) {
            this.speed = Math.max(1, speed);
            return this;
        }
    }
}

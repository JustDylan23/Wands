package me.dylan.wands.spell.types;

import me.dylan.wands.miscellaneous.utils.Common;
import me.dylan.wands.spell.util.SpellEffectUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
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
public final class Wave extends Behavior {
    private final int effectDistance;
    private final String tagWaveSpell;

    private Wave(@NotNull Builder builder) {
        super(builder.baseProps);
        this.effectDistance = builder.effectDistance;
        this.tagWaveSpell = UUID.randomUUID().toString();

        this.addPropertyInfo("Effect distance", this.effectDistance, "meters");
    }

    public static @NotNull Builder newBuilder() {
        return new Builder();
    }

    @Override
    public boolean cast(@NotNull Player player, @NotNull String weaponName) {
        Vector direction = player.getLocation().getDirection().normalize();
        castSounds.play(player.getLocation().add(direction.clone().multiply(5)));
        Location origin = player.getEyeLocation();
        new BukkitRunnable() {
            private int count;

            @Override
            public void run() {
                count++;
                if (count >= effectDistance) this.cancel();
                Location loc = origin.add(direction).clone();
                if (!loc.getBlock().isPassable()) {
                    this.cancel();
                    return;
                }
                spellRelativeEffects.accept(loc, loc.getWorld());
                for (LivingEntity livingEntity : SpellEffectUtil.getNearbyLivingEntities(player, loc, entity -> !entity.hasMetadata(tagWaveSpell), spellEffectRadius)) {
                    livingEntity.setMetadata(tagWaveSpell, Common.METADATA_VALUE_TRUE);
                    SpellEffectUtil.damageEffect(player, livingEntity, entityDamage, weaponName);
                    entityEffects.accept(livingEntity);
                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        if (livingEntity.isValid()) {
                            livingEntity.removeMetadata(tagWaveSpell, plugin);
                        }
                    }, effectDistance - count);
                }
            }
        }.runTaskTimer(plugin, 1, 1);
        return true;
    }

    public static final class Builder extends AbstractBuilder<Builder> {
        private int effectDistance;

        private Builder() {
        }

        @Override
        Builder self() {
            return this;
        }

        public @NotNull Wave build() {
            return new Wave(this);
        }

        public Builder setEffectDistance(int effectDistance) {
            this.effectDistance = effectDistance;
            return this;
        }

    }
}

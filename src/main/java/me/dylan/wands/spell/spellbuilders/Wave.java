package me.dylan.wands.spell.spellbuilders;

import me.dylan.wands.spell.accessories.SpellInfo;
import me.dylan.wands.spell.util.SpellEffectUtil;
import me.dylan.wands.utils.Common;
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
public final class Wave extends BuildableBehaviour {
    private final int effectDistance;
    private final String tagWaveSpell;

    private Wave(@NotNull Builder builder) {
        super(builder);
        this.effectDistance = builder.effectDistance;
        this.tagWaveSpell = UUID.randomUUID().toString();

        this.addPropertyInfo("Effect distance", this.effectDistance, "meters");
    }

    public static @NotNull Builder newBuilder() {
        return new Builder();
    }

    @Override
    public boolean cast(@NotNull Player player, @NotNull String weapon) {
        Vector direction = player.getLocation().getDirection().normalize();
        castSounds.play(player.getLocation().add(direction.clone().multiply(5)));
        Location origin = player.getEyeLocation();
        Location spellLoc = origin.clone();
        SpellInfo spellInfo = new SpellInfo(player, origin, spellLoc);
        BukkitRunnable bukkitRunnable = new BukkitRunnable() {
            private int count = 0;

            @Override
            public void run() {
                count++;
                if (count >= effectDistance) this.cancel();
                spellLoc.add(direction);
                if (!spellLoc.getBlock().isPassable()) {
                    this.cancel();
                    return;
                }
                spellRelativeEffects.accept(spellLoc, spellInfo);
                for (LivingEntity livingEntity : SpellEffectUtil.getNearbyLivingEntities(player, spellLoc, entity -> !entity.hasMetadata(tagWaveSpell), spellEffectRadius)) {
                    livingEntity.setMetadata(tagWaveSpell, Common.getMetadataValueTrue());
                    SpellEffectUtil.damageEffect(player, livingEntity, entityDamage, weapon);
                    entityEffects.accept(livingEntity, spellInfo);
                    applyPotionEffects(livingEntity);
                    Common.runTaskLater(() -> {
                        if (livingEntity.isValid())
                            Common.removeMetaData(livingEntity, tagWaveSpell);
                    }, effectDistance - count);
                }
            }
        };
        Common.runTaskTimer(bukkitRunnable, 1, 1);
        return true;
    }

    public static final class Builder extends AbstractBuilder<Builder> {
        private int effectDistance = 25;

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

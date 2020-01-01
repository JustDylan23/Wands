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

import java.util.List;
import java.util.function.BiConsumer;

/**
 * Fires a ray that stops at entities/walls
 * <p>
 * Configurable:
 * - The width of the ray (more width = more range around ray so hits entities easier).
 * - Whether the ray hits one target or multiple targets around the impact point.
 * - The speed at which the ray moves forward.
 * - The maximum distance the ray can travel.
 */
public final class Ray extends BuildableBehaviour {
    private final int effectDistance, speed;
    private final float rayWidth;
    private final Target target;
    private final BiConsumer<Location, SpellInfo> hitEffects;

    private Ray(@NotNull Builder builder) {
        super(builder.baseProps);
        this.effectDistance = builder.effectDistance;
        this.speed = builder.speed;
        this.rayWidth = builder.rayWidth;
        this.target = builder.target;
        this.hitEffects = builder.hitEffects;

        addPropertyInfo("Effect distance", effectDistance, "meters");
        addPropertyInfo("Ray width", rayWidth, "meters");
        addPropertyInfo("Meters per tick", speed, "meters");
        addPropertyInfo("Target", target);
    }

    public static @NotNull Builder newBuilder(Target target) {
        return new Builder(target);
    }

    @Override
    public boolean cast(@NotNull Player player, @NotNull String weapon) {
        Vector direction = player.getLocation().getDirection().normalize();
        castSounds.play(player);
        Location origin = player.getEyeLocation();
        Location spellLoc = origin.clone();
        SpellInfo spellInfo = new SpellInfo(player, origin, spellLoc);
        BukkitRunnable bukkitRunnable = new BukkitRunnable() {
            int count = 0;

            @Override
            public void run() {
                for (int i = 0; i < speed; ++i) {
                    spellLoc.add(direction);
                    spellRelativeEffects.accept(spellLoc, spellInfo);
                    if (shouldContinue()) {
                        continue;
                    }
                    cancel();
                    effectEntities(spellInfo, weapon);
                    break;
                }
            }

            boolean shouldContinue() {
                ++count;
                if (count >= effectDistance || !spellLoc.getBlock().isPassable())
                    return false;
                List<LivingEntity> entities = SpellEffectUtil.getNearbyLivingEntities(player, spellLoc, rayWidth);
                return entities.isEmpty();
            }

            void effectEntities(SpellInfo spellInfo, String wandDisplayName) {
                Location spellLocation = spellInfo.spellLocation();
                hitEffects.accept(spellLocation, spellInfo);
                for (LivingEntity entity : SpellEffectUtil.getNearbyLivingEntities(spellInfo.caster(), spellLocation, (target == Target.SINGLE) ? rayWidth : spellEffectRadius)) {
                    knockBack.apply(entity, spellLocation);
                    entityEffects.accept(entity, spellInfo);
                    applyPotionEffects(entity);
                    SpellEffectUtil.damageEffect(spellInfo.caster(), entity, entityDamage, wandDisplayName);
                    if (target == Target.SINGLE) break;
                }
            }
        };
        Common.runTaskTimer(bukkitRunnable, 1, 1);
        return true;
    }

    public static final class Builder extends AbstractBuilder<Builder> {
        private final Target target;
        private int effectDistance = 0;
        private int speed = 1;
        private float rayWidth = 0.0F;
        private BiConsumer<Location, SpellInfo> hitEffects = Common.emptyBiConsumer();

        private Builder(Target target) {
            this.target = target;
        }

        @Override
        Builder self() {
            return this;
        }

        @Override
        public @NotNull Behavior build() {
            return new Ray(this);
        }

        public Builder setEffectDistance(int effectDistance) {
            this.effectDistance = effectDistance;
            return this;
        }

        public Builder setMetersPerTick(int meters) {
            this.speed = Math.max(1, meters);
            return this;
        }

        public Builder setRayWidth(int width) {
            this.rayWidth = width;
            return this;
        }

        public Builder setHitEffects(BiConsumer<Location, SpellInfo> hitEffects) {
            this.hitEffects = hitEffects;
            return this;
        }
    }
}

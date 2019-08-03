package me.dylan.wands.spell.types;

import me.dylan.wands.spell.SpellEffectUtil;
import me.dylan.wands.util.Common;
import org.bukkit.Location;
import org.bukkit.World;
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
public final class Ray extends Base {
    private final int effectDistance, speed;
    private final float rayWidth;
    private final Target target;
    private final BiConsumer<Location, World> hitEffects;

    private Ray(@NotNull Builder builder) {
        super(builder.baseProps);
        this.effectDistance = builder.effectDistance;
        this.speed = builder.speed;
        this.rayWidth = builder.rayWidth;
        this.target = builder.target;
        this.hitEffects = builder.hitEffects;

        addPropertyInfo("Effect distance", effectDistance, "meters");
        addPropertyInfo("Ray width", rayWidth, "meters");
        addPropertyInfo("Meters per tick", rayWidth, "meters");
        addPropertyInfo("Target", target);
    }

    @NotNull
    public static Builder newBuilder(Target target) {
        return new Builder(target);
    }

    @Override
    public boolean cast(@NotNull Player player, @NotNull String weaponName) {
        Vector direction = player.getLocation().getDirection().normalize();
        castSounds.play(player);
        Location origin = player.getEyeLocation();
        new BukkitRunnable() {
            int count = 0;

            @Override
            public void run() {
                for (int i = 0; i < speed; i++) {
                    count++;
                    Location loc = origin.add(direction).clone();
                    if (count >= effectDistance || !loc.getBlock().isPassable()) {
                        cancel();
                        effectEntities(loc, player, weaponName);
                        return;
                    }
                    spellRelativeEffects.accept(loc, loc.getWorld());
                    List<LivingEntity> entities = SpellEffectUtil.getNearbyLivingEntities(player, loc, rayWidth);
                    if (entities.size() != 0) {
                        effectEntities(loc, player, weaponName);
                        cancel();
                        return;
                    }
                }
            }
        }.runTaskTimer(plugin, 1, 1);
        return true;
    }

    private void effectEntities(Location loc, Player player, String wandDisplayName) {
        hitEffects.accept(loc, loc.getWorld());
        for (LivingEntity entity : SpellEffectUtil.getNearbyLivingEntities(player, loc, (target == Target.SINGLE) ? rayWidth : spellEffectRadius)) {
            knockBack.apply(entity, loc);
            entityEffects.accept(entity);
            SpellEffectUtil.damageEffect(player, entity, entityDamage, wandDisplayName);
            if (target == Target.SINGLE) break;
        }
    }

    public static final class Builder extends AbstractBuilder<Builder> {
        private final Target target;

        private int effectDistance;
        private int speed = 1;
        private float rayWidth;
        private BiConsumer<Location, World> hitEffects = Common.emptyBiConsumer();

        private Builder(Target target) {
            this.target = target;
        }

        @Override
        Builder self() {
            return this;
        }

        @Override
        public Base build() {
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

        public Builder setHitEffects(BiConsumer<Location, World> hitEffects) {
            this.hitEffects = hitEffects;
            return this;
        }
    }
}

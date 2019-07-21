package me.dylan.wands.spell.handler;

import me.dylan.wands.spell.SpellEffectUtil;
import me.dylan.wands.util.Common;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.function.BiConsumer;

public final class RaySpell extends Behaviour {
    private final int effectDistance, speed;
    private final float rayWidth;
    private final Target target;
    private final BiConsumer<Location, World> hitEffects;

    private RaySpell(Builder builder) {
        super(builder.baseMeta);
        this.effectDistance = builder.effectDistance;
        this.speed = builder.speed;
        this.rayWidth = builder.rayWidth;
        this.target = builder.target;
        this.hitEffects = builder.hitEffects;

        addStringProperty("Effect distance", effectDistance, "meters");
        addStringProperty("Ray width", rayWidth, "meters");
        addStringProperty("Meters per tick", rayWidth, "meters");
        addStringProperty("Target", target);
    }

    public static Builder newBuilder(Target target) {
        return new Builder(target);
    }

    @Override
    public boolean cast(Player player, String wandDisplayName) {
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
                        effectEntities(loc, player, wandDisplayName);
                        return;
                    }
                    spellRelativeEffects.accept(loc, loc.getWorld());
                    List<LivingEntity> entities = SpellEffectUtil.getNearbyLivingEntities(player, loc, rayWidth);
                    if (entities.size() != 0) {
                        effectEntities(loc, player, wandDisplayName);
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
            push(entity, loc, player);
            entityEffects.accept(entity);
            SpellEffectUtil.damageEffect(player, entity, affectedEntityDamage, wandDisplayName);
            if (target == Target.SINGLE) break;
        }
    }

    public enum Target {
        SINGLE,
        MULTI
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
        public Behaviour build() {
            return new RaySpell(this);
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

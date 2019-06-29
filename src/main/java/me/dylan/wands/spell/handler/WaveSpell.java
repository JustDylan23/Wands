package me.dylan.wands.spell.handler;

import me.dylan.wands.util.EffectUtil;
import me.dylan.wands.util.Common;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public final class WaveSpell extends Behaviour {
    private final int effectDistance;
    private final boolean stopAtEntity;
    private final int tickSkip;
    private static int instanceCount = 0;
    private final String tagWaveSpell;

    private WaveSpell(Builder builder) {
        super(builder.baseMeta);
        this.effectDistance = builder.effectDistance;
        this.stopAtEntity = builder.stopAtEntity;
        this.tickSkip = builder.tickSkip;
        this.tagWaveSpell = "WAVE_SPELL;ID#" + instanceCount++;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public boolean cast(Player player) {
        Vector direction = player.getLocation().getDirection().normalize();
        castEffects.accept(direction.clone().multiply(10).toLocation(player.getWorld()).add(player.getEyeLocation()));
        Location currentLoc = player.getEyeLocation();
        new BukkitRunnable() {
            int count = 0;

            @Override
            public void run() {
                outer:
                for (int i = 0; i < tickSkip; i++) {
                    count++;
                    if (count >= effectDistance) cancel();
                    Location loc = currentLoc.add(direction).clone();
                    if (!loc.getBlock().isPassable()) {
                        cancel();
                        return;
                    }
                    visualEffects.accept(loc);
                    for (LivingEntity entity : EffectUtil.getNearbyLivingEntities(player, loc, effectRadius)) {
                        if (stopAtEntity) {
                            entity.damage(entityDamage);
                            entityEffects.accept(entity);
                            cancel();
                            break outer;
                        } else if (!entity.hasMetadata(tagWaveSpell)) {
                            entity.setMetadata(tagWaveSpell, Common.METADATA_VALUE_TRUE);
                            if (entityDamage != 0) entity.damage(entityDamage);
                            entityEffects.accept(entity);
                            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                if (entity.isValid()) {
                                    entity.removeMetadata(tagWaveSpell, plugin);
                                }
                            }, effectDistance - count);
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 1, 1);
        return true;
    }

    @Override
    public String toString() {
        return super.toString() + "Effect distance: " + effectDistance
                + "\nStop at entity: " + stopAtEntity;
    }

    public static final class Builder extends AbstractBuilder<Builder> {
        private int effectDistance;
        private boolean stopAtEntity = false;
        private int tickSkip = 1;

        private Builder() {
        }

        @Override
        Builder self() {
            return this;
        }

        public Builder setEffectDistance(int effectDistance) {
            this.effectDistance = effectDistance;
            return this;
        }

        public Builder stopAtEntity(boolean stopAtEntity) {
            this.stopAtEntity = stopAtEntity;
            return this;
        }

        public Builder setTickSkip(int tickSkip) {
            this.tickSkip = Math.max(1, tickSkip);
            return this;
        }

        public WaveSpell build() {
            return new WaveSpell(this);
        }
    }
}

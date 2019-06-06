package me.dylan.wands.spell.handler;

import me.dylan.wands.util.EffectUtil;
import me.dylan.wands.util.ShorthandUtil;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public final class Wave extends Behaviour {
    private final int effectDistance;
    private final boolean stopAtEntity;

    //can be accessed via builder
    private Wave(Builder builder) {
        super(builder.baseMeta);
        this.effectDistance = builder.effectDistance;
        this.stopAtEntity = builder.stopAtEntity;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public boolean cast(Player player) {
        Vector direction = player.getLocation().getDirection().normalize();
        //id should prevent the entity from being affected twice by the wave
        String randomID = RandomStringUtils.random(5, true, false);
        castEffects.accept(direction.clone().multiply(10).toLocation(player.getWorld()).add(player.getEyeLocation()));
        Location currentLoc = player.getEyeLocation();
        new BukkitRunnable() {
            int count = 0;

            @Override
            public void run() {
                if (count++ >= effectDistance) cancel();
                Location loc = currentLoc.add(direction).clone();
                visualEffects.accept(loc);
                for (Damageable entity : EffectUtil.getNearbyDamageables(player, loc, effectAreaRange)) {
                    if (!entity.hasMetadata(randomID)) {
                        entity.setMetadata(randomID, ShorthandUtil.METADATA_VALUE_TRUE);
                        EffectUtil.damage(entityDamage, player, entity);
                        entityEffects.accept(entity);
                        Bukkit.getScheduler().runTaskLater(plugin, () -> {
                            if (entity.isValid()) {
                                entity.removeMetadata(randomID, plugin);
                            }
                        }, effectDistance - count);
                        if (stopAtEntity) {
                            cancel();
                            break;
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 1, 1);
        return true;
    }

    public static class Builder extends AbstractBuilder<Builder> {
        private int effectDistance;
        private boolean stopAtEntity = false;

        private Builder() {
        }

        @Override
        Builder self() {
            return this;
        }

        public Builder setEffectDistance(int effectDistance) {
            this.effectDistance = effectDistance;
            return self();
        }

        public Builder stopAtEntity(boolean stopAtEntity) {
            this.stopAtEntity = stopAtEntity;
            return self();
        }

        public Wave build() {
            return new Wave(this);
        }
    }
}

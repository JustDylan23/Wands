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

public final class WaveSpell extends Behaviour {
    private final int effectDistance;
    private final boolean stopAtEntity;
    private final int tickSkip;

    //can be accessed via builder
    private WaveSpell(Builder builder) {
        super(builder.baseMeta);
        this.effectDistance = builder.effectDistance;
        this.stopAtEntity = builder.stopAtEntity;
        this.tickSkip = builder.tickSkip;
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
                outer:
                for (int i = 1; i <= tickSkip; i++) {
                    count++;
                    if (count >= effectDistance) cancel();
                    Location loc = currentLoc.add(direction).clone();
                    visualEffects.accept(loc);
                    for (Damageable entity : EffectUtil.getNearbyLivingEntities(player, loc, effectRadius)) {
                        if (stopAtEntity) {
                            entity.damage(entityDamage);
                            entityEffects.accept(entity);
                            cancel();
                            break outer;
                        } else if (!entity.hasMetadata(randomID)) {
                            entity.setMetadata(randomID, ShorthandUtil.METADATA_VALUE_TRUE);
                            entity.damage(entityDamage);
                            entityEffects.accept(entity);
                            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                if (entity.isValid()) {
                                    entity.removeMetadata(randomID, plugin);
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

    public static class Builder extends AbstractBuilder<Builder> {
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
            return self();
        }

        public Builder stopAtEntity(boolean stopAtEntity) {
            this.stopAtEntity = stopAtEntity;
            return self();
        }

        public Builder setTickSkip(int tickSkip) {
            this.tickSkip = Math.max(1, tickSkip);
            return self();
        }

        public WaveSpell build() {
            return new WaveSpell(this);
        }
    }
}

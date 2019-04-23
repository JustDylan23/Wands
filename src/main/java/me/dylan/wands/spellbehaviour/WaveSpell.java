package me.dylan.wands.spellbehaviour;

import me.dylan.wands.WandUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

public class WaveSpell extends SpellBehaviour {
    private final int effectDistance;
    private final boolean stopAtEntity;

    //can be accessed via builder
    private WaveSpell(SpellBehaviour.BaseProperties baseProperties, int effectDistance, boolean stopAtEntity) {
        super(baseProperties);
        this.effectDistance = effectDistance;
        this.stopAtEntity = stopAtEntity;
    }

    @Override
    public void cast(Player player) {
        Vector direction = player.getLocation().getDirection().normalize();
        //id should prevent the entity from being affected twice by the wave
        String randomID = RandomStringUtils.random(5, true, false);
        castEffects.accept(direction.clone().multiply(10).toLocation(player.getWorld()).add(player.getEyeLocation()));
        Location currentLoc = player.getEyeLocation();
        for (int i = 1; i <= effectDistance; i++) {
            int index = i - 1;
            Location loc = currentLoc.add(direction).clone();
            if (!currentLoc.getBlock().isPassable()) {
                return;
            }
            WandUtils.runTaskLater(() -> {
                visualEffects.accept(loc);
                WandUtils.getNearbyDamageables(player, loc, effectAreaRange).forEach(entity -> {
                    if (!entity.hasMetadata(randomID)) {
                        player.setMetadata(randomID, new FixedMetadataValue(plugin, true));
                        WandUtils.damage(entityDamage, player, entity);
                        entityEffects.accept(entity);
                        Bukkit.getScheduler().runTaskLater(plugin, () -> {
                            if (entity.isValid()) {
                                entity.removeMetadata(randomID, plugin);
                            }
                        }, effectDistance - index);
                    }
                });
            }, i);
        }
    }

    public static Builder getBuilder(BaseProperties baseProperties) {
        return new Builder(baseProperties);
    }

    public static class Builder {
        private final SpellBehaviour.BaseProperties baseProperties;
        private int effectDistance;
        private boolean stopAtEntity = false;

        private Builder(SpellBehaviour.BaseProperties basePropperties) {
            this.baseProperties = basePropperties;
        }

        public Builder setEffectDistance(int effectDistance) {
            this.effectDistance = effectDistance;
            return this;
        }

        public Builder stopAtEntity(boolean stopAtEntity) {
            this.stopAtEntity = stopAtEntity;
            return this;
        }

        public WaveSpell build() {
            return new WaveSpell(baseProperties, effectDistance, stopAtEntity);
        }
    }
}

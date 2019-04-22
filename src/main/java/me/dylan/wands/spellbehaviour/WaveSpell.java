package me.dylan.wands.spellbehaviour;

import me.dylan.wands.WandUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

public class WaveSpell extends SpellBehaviour {
    private final int effectDistance;

    //can be accessed via builder
    private WaveSpell(SpellBehaviour.BaseProperties baseProperties, int effectDistance) {
        super(baseProperties);
        this.effectDistance = effectDistance;
    }

    @Override
    public void cast(Player player) {
        Vector direction = player.getLocation().getDirection().normalize();
        String metaId = System.currentTimeMillis() + "";
        castEffects.accept(direction.clone().multiply(10).toLocation(player.getWorld()).add(player.getEyeLocation()));
        for (int i = 1; i <= effectDistance; i++) {
            Location loc = direction.clone().multiply(i).toLocation(player.getWorld()).add(player.getEyeLocation());
            if (!loc.getBlock().isPassable()) {
                return;
            }
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                visualEffects.accept(loc);
                WandUtils.getNearbyDamageables(player, loc, effectAreaRange).forEach(entity -> {
                    if (!entity.hasMetadata(metaId)) {
                        player.setMetadata(metaId, new FixedMetadataValue(plugin, true));
                        WandUtils.damage(entityDamage, player, entity);
                        entityEffects.accept(entity);
                        Bukkit.getScheduler().runTaskLater(plugin, () -> {
                            if (entity.isValid()) {
                                entity.removeMetadata(metaId, plugin);
                            }
                        }, effectDistance);
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

        private Builder(SpellBehaviour.BaseProperties basePropperties) {
            this.baseProperties = basePropperties;
        }

        public Builder setEffectDistance(int effectDistance) {
            this.effectDistance = effectDistance;
            return this;
        }

        public WaveSpell build() {
            return new WaveSpell(baseProperties, effectDistance);
        }
    }
}

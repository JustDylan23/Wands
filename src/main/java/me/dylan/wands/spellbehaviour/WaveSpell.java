package me.dylan.wands.spellbehaviour;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.util.Vector;

import java.util.function.Consumer;

public class WaveSpell extends SpellBehaviour {

    private final int effectDistance;

    private WaveSpell(int entityDamage, float effectAreaRange, Consumer<Location> castEffects, Consumer<Location> visualEffects, Consumer<Entity> entityEffects, int effectDistance) {
        super(entityDamage, effectAreaRange, castEffects, visualEffects, entityEffects);
        this.effectDistance = effectDistance;
    }

    @Override
    public void executeFrom(Player player) {
        Vector direction = player.getLocation().getDirection().normalize();
        String metaID = System.currentTimeMillis() + "";
        for (int i = 1; i <= effectDistance; i++) {
            Location location = direction.clone().multiply(i).toLocation(player.getWorld()).add(player.getEyeLocation());
            if (!location.getBlock().isPassable()) {
                return;
            }
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                visualEffects.accept(location);
                getNearbyDamageables(player, location, effectAreaRange).forEach(entity -> {
                    if (!entity.hasMetadata(metaID)) {
                        player.setMetadata(metaID, new FixedMetadataValue(plugin, true));
                        entityEffects.accept(entity);
                        damage(entityDamage, player, entity);
                        Bukkit.getScheduler().runTaskLater(plugin, () -> {
                            if (entity.isValid()) {
                                entity.removeMetadata(metaID, plugin);
                            }
                        }, effectDistance);
                    }
                });

            }, i);
        }
    }

    public static class Builder extends SpellBuilder<Builder, WaveSpell> {

        private int effectDistance = 30;

        @Override
        public WaveSpell build() {
            return new WaveSpell(effectDistance, effectAreaRange, castEffects, visualEffects, entityEffects, entityDamage);
        }

        @Override
        protected Builder getInstance() {
            return this;
        }

        public Builder setEffectDistance(int effectDistance) {
            this.effectDistance = Math.max(effectDistance, 0);
            return this;
        }
    }
}

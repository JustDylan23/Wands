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

    private WaveSpell(int entityDamage, float effectAreaRange, float pushSpeed, Consumer<Location> castEffects, Consumer<Location> visualEffects, Consumer<Entity> entityEffects, int effectDistance) {
        super(entityDamage, effectAreaRange, pushSpeed, castEffects, visualEffects, entityEffects);
        this.effectDistance = effectDistance;
    }

    @Override
    public void executeFrom(Player player) {
        Vector direction = player.getLocation().getDirection().normalize();
        String metaId = System.currentTimeMillis() + "";
        castEffects.accept(player.getLocation());
        for (int i = 1; i <= effectDistance; i++) {
            Location loc = direction.clone().multiply(i).toLocation(player.getWorld()).add(player.getEyeLocation());
            if (!loc.getBlock().isPassable()) {
                return;
            }
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                visualEffects.accept(loc);
                getNearbyDamageables(player, loc, effectAreaRange).forEach(entity -> {
                    if (!entity.hasMetadata(metaId)) {
                        player.setMetadata(metaId, new FixedMetadataValue(plugin, true));
                        damage(entityDamage, player, entity);
                        entityEffects.accept(entity);
                        pushFrom(loc, entity, pushSpeed);
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

    public static class Builder extends SpellBuilder<Builder, WaveSpell> {

        private int effectDistance = 30;

        @Override
        public WaveSpell build() {
            return new WaveSpell(entityDamage, effectAreaRange, pushSpeed, castEffects, visualEffects, entityEffects, effectDistance);
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

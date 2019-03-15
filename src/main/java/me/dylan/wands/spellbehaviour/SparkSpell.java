package me.dylan.wands.spellbehaviour;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.function.Consumer;

public class SparkSpell extends SpellBehaviour {

    private final int effectDistance;

    private SparkSpell(int entityDamage, float effectAreaRange, float pushSpeed, Consumer<Location> castEffects, Consumer<Location> visualEffects, Consumer<Entity> entityEffects, int effectDistance) {
        super(entityDamage, effectAreaRange, pushSpeed, castEffects, visualEffects, entityEffects);
        this.effectDistance = effectDistance;
    }

    @Override
    public void executeFrom(Player player) {
        Location loc = getSpellLocation(player);
        Iterable<Damageable> effectedEntities = getNearbyDamageables(player, loc, effectAreaRange);
        castEffects.accept(player.getLocation());
        effectedEntities.forEach(entity -> {
            entity.damage(entityDamage, player);
            entity.setVelocity(new Vector(0, 0, 0));
            entityEffects.accept(entity);
            pushFrom(loc, entity, pushSpeed);
        });
        visualEffects.accept(loc);

    }

    private Location getSpellLocation(Player player) {
        Entity entity = player.getTargetEntity(effectDistance);
        if (entity != null) {
            return entity.getLocation();
        }
        Block block = player.getTargetBlock(effectDistance);
        if (block != null) {
            return block.getLocation().toCenterLocation();
        }
        return player.getLocation().getDirection().normalize().multiply(effectDistance).toLocation(player.getWorld()).add(player.getLocation());
    }

    @SuppressWarnings({"unused"})
    public static class Builder extends SpellBuilder<Builder, SparkSpell> {

        private int effectDistance = 30;

        public SparkSpell build() {
            return new SparkSpell(entityDamage, effectAreaRange, pushSpeed, castEffects, visualEffects, entityEffects, effectDistance);
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

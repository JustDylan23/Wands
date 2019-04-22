package me.dylan.wands.spellbehaviour;

import me.dylan.wands.WandUtils;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class SparkSpell extends SpellBehaviour {
    private final int effectDistance;

    //can be accessed via builder
    private SparkSpell(SpellBehaviour.BaseProperties basePropperties, int effectDistance) {
        super(basePropperties);
        this.effectDistance = effectDistance;
    }

    @Override
    public void cast(Player player) {
        Location loc = getSpellLocation(player);
        Iterable<Damageable> effectedEntities = WandUtils.getNearbyDamageables(player, loc, effectAreaRange);
        castEffects.accept(player.getLocation());
        effectedEntities.forEach(entity -> {
            WandUtils.damage(entityDamage, player, entity);
            entity.setVelocity(new Vector(0, 0, 0));
            entityEffects.accept(entity);
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

        public SparkSpell build() {
            return new SparkSpell(baseProperties, effectDistance);
        }
    }
}

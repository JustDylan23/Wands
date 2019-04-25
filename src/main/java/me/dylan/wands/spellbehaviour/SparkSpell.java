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
    private SparkSpell(SpellBehaviour.Builder.BuilderWrapper builderWrapper, int effectDistance) {
        super(builderWrapper);
        this.effectDistance = effectDistance;
    }

    public static Builder newBuilder() {
        return new Builder();
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
            return entity.getLocation().add(0, 0.5, 0);
        }
        Block block = player.getTargetBlock(effectDistance);
        if (block != null) {
            return block.getLocation().toCenterLocation().subtract(player.getLocation().getDirection().normalize());
        }
        return player.getLocation().getDirection().normalize().multiply(effectDistance).toLocation(player.getWorld()).add(player.getLocation());
    }

    public static class Builder extends SpellBehaviour.Builder<Builder> {
        private int effectDistance;

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

        public SparkSpell build() {
            return new SparkSpell(createBuilderWrapper(), effectDistance);
        }
    }
}

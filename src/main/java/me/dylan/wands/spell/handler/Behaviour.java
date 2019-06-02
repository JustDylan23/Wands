package me.dylan.wands.spell.handler;

import me.dylan.wands.Main;
import me.dylan.wands.util.DataUtil;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public abstract class Behaviour implements Listener {

    final static Main plugin = Main.getPlugin();

    final int entityDamage;
    final float effectAreaRange;
    final Consumer<Location> castEffects;
    final Consumer<Location> visualEffects;
    final Consumer<Entity> entityEffects;

    Behaviour(@Nonnull AbstractBuilder.BaseMeta baseMeta) {
        this.entityDamage = baseMeta.entityDamage;
        this.effectAreaRange = baseMeta.effectRadius;
        this.castEffects = baseMeta.castEffects;
        this.visualEffects = baseMeta.relativeEffects;
        this.entityEffects = baseMeta.entityEffects;
    }

    public abstract boolean cast(Player player);


    public static abstract class AbstractBuilder<T extends AbstractBuilder<T>> {

        final BaseMeta baseMeta = new BaseMeta();

        abstract T self();
        public  abstract Behaviour build();

        /**
         * Sets the damage that is applied to the Damageable effected by the spell.
         *
         * @param damage The amount of damage
         * @return this
         */

        public T setEntityDamage(int damage) {
            baseMeta.entityDamage = damage;
            return self();
        }

        /**
         * Sets the effects which will effect the Damageables in the spell's effect range.
         *
         * @param effects Effects applied to the affected Damageables
         * @return this
         */

        public T setEntityEffects(Consumer<Entity> effects) {
            baseMeta.entityEffects = effects;
            return self();
        }

        /**
         * Sets the radius of the affected Damageables after the spell concludes.
         *
         * @param radius The radius
         * @return this
         */

        public T setEffectRadius(float radius) {
            baseMeta.effectRadius = radius;
            return self();
        }

        /**
         * Sets the effects that will be executed relative to the player.
         *
         * @param castEffects Effects relative to the player
         * @return this
         */

        public T setCastEffects(Consumer<Location> castEffects) {
            baseMeta.castEffects = castEffects;
            return self();
        }

        /**
         * Sets the visual effects that the spell shows, whether it is a trail of particles
         * or is executed relative to where you look is up to the spell handler BaseProps is used in.
         *
         * @param effects Effects relative to the spell
         * @return this
         */

        public T setRelativeEffects(Consumer<Location> effects) {
            baseMeta.relativeEffects = effects;
            return self();
        }

        static class BaseMeta {
            private int entityDamage = 0;
            private float effectRadius = 0;
            private Consumer<Location> castEffects = DataUtil.emptyConsumer();
            private Consumer<Location> relativeEffects = DataUtil.emptyConsumer();
            private Consumer<Entity> entityEffects = DataUtil.emptyConsumer();
        }
    }
}

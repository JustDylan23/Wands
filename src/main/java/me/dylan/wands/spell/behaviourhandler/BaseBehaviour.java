package me.dylan.wands.spell.behaviourhandler;

import me.dylan.wands.Wands;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.function.Consumer;

public abstract class BaseBehaviour implements Listener {
    final static Wands plugin = Wands.getPlugin();
    final int entityDamage;
    final float effectAreaRange;
    final Consumer<Location> castEffects;
    final Consumer<Location> visualEffects;
    final Consumer<Entity> entityEffects;

    BaseBehaviour(AbstractBuilder.BaseMeta baseMeta) {
        this.entityDamage = baseMeta.entityDamage;
        this.effectAreaRange = baseMeta.effectAreaRange;
        this.castEffects = baseMeta.castEffects;
        this.visualEffects = baseMeta.visualEffects;
        this.entityEffects = baseMeta.entityEffects;
    }

    public abstract void cast(Player player);


    public static abstract class AbstractBuilder<T extends AbstractBuilder<T>> {

        final BaseMeta baseMeta = new BaseMeta();

        abstract T self();

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
         * Sets the radius of the affected Damageables after the spell concludes.
         *
         * @param radius The radius
         * @return this
         */

        public T setEffectRadius(float radius) {
            baseMeta.effectAreaRange = radius;
            return self();
        }

        /**
         * Sets the effect that will be executed relative to the player.
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
         * or is executed relative to where you look is up to the spell type BasePropperties is used in.
         *
         * @param effects Effects relative to the spell
         * @return this
         */

        public T setVisualEffects(Consumer<Location> effects) {
            baseMeta.visualEffects = effects;
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

        BaseMeta getMeta() {
            return baseMeta;
        }

        static class BaseMeta {
            private final static Consumer<?> EMPTY_CONSUMER = e -> {
            };

            private int entityDamage = 3;
            private float effectAreaRange = 2;
            private Consumer<Location> castEffects = emptyConsumer();
            private Consumer<Location> visualEffects = emptyConsumer();
            private Consumer<Entity> entityEffects = emptyConsumer();

            @SuppressWarnings("unchecked")
            private <T> Consumer<T> emptyConsumer() {
                return (Consumer<T>) EMPTY_CONSUMER;
            }
        }
    }
}

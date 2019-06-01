package me.dylan.wands.spell.behaviourhandler;

import me.dylan.wands.Main;
import me.dylan.wands.util.DataUtil;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public abstract class BaseBehaviour implements Listener {

    final static Main plugin = Main.getPlugin();

    final int entityDamage;
    final float effectAreaRange;
    final Consumer<Location> castEffects;
    final Consumer<Location> visualEffects;
    final Consumer<Entity> entityEffects;

    BaseBehaviour(@Nonnull AbstractBuilder.BaseMeta baseMeta) {
        this.entityDamage = baseMeta.entityDamage;
        this.effectAreaRange = baseMeta.effectRadius;
        this.castEffects = baseMeta.castEffects;
        this.visualEffects = baseMeta.relativeEffects;
        this.entityEffects = baseMeta.entityEffects;
    }

    public abstract void cast(Player player);


    public static abstract class AbstractBuilder<T extends AbstractBuilder<T>> {

        final BaseMeta baseMeta = new BaseMeta();

        abstract T self();
        public  abstract BaseBehaviour build();

        /**
         * Sets the damage that is applied to the Damageable effected by the baseSpell.
         *
         * @param damage The amount of damage
         * @return this
         */

        public T setEntityDamage(int damage) {
            baseMeta.entityDamage = damage;
            return self();
        }

        /**
         * Sets the effects which will effect the Damageables in the baseSpell's effect range.
         *
         * @param effects Effects applied to the affected Damageables
         * @return this
         */

        public T setEntityEffects(Consumer<Entity> effects) {
            baseMeta.entityEffects = effects;
            return self();
        }

        /**
         * Sets the radius of the affected Damageables after the baseSpell concludes.
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
         * Sets the visual effects that the baseSpell shows, whether it is a trail of particles
         * or is executed relative to where you look is up to the baseSpell type BaseProps is used in.
         *
         * @param effects Effects relative to the baseSpell
         * @return this
         */

        public T setRelativeEffects(Consumer<Location> effects) {
            baseMeta.relativeEffects = effects;
            return self();
        }

        BaseMeta getMeta() {
            return baseMeta;
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

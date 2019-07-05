package me.dylan.wands.spell.handler;

import me.dylan.wands.Main;
import me.dylan.wands.spell.spelleffect.sound.SingularSound;
import me.dylan.wands.spell.spelleffect.sound.SoundEffect;
import me.dylan.wands.util.Common;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

public abstract class Behaviour implements Listener {

    final static Main plugin = Main.getPlugin();

    final int affectedEntityDamage;
    final float spellEffectRadius;
    final SoundEffect castSounds;
    final Consumer<Location> spellRelativeEffects;
    final Consumer<LivingEntity> affectedEntityEffects;

    Behaviour(@Nonnull AbstractBuilder.BaseMeta baseMeta) {
        this.affectedEntityDamage = baseMeta.affectedEntityDamage;
        this.spellEffectRadius = baseMeta.spellEffectRadius;
        this.castSounds = baseMeta.castSounds;
        this.spellRelativeEffects = baseMeta.spellRelativeEffects;
        this.affectedEntityEffects = baseMeta.affectedEntityEffects;
    }

    @Override
    public String toString() {
        return "Entity damage: " + affectedEntityDamage +
                "\nEffect radius: " + spellEffectRadius + "\n";
    }

    public abstract boolean cast(Player player);

    public static abstract class AbstractBuilder<T extends AbstractBuilder<T>> {

        final BaseMeta baseMeta = new BaseMeta();

        abstract T self();

        public abstract Behaviour build();

        /**
         * Sets the damage that is applied to the Damageable effected by the implementations.
         *
         * @param damage The amount of damage
         * @return this
         */

        public T setAffectedEntityDamage(int damage) {
            baseMeta.affectedEntityDamage = damage;
            return self();
        }

        /**
         * Sets the effects which will effect the Damageables in the implementations's effect range.
         *
         * @param effects Effects applied to the affected Damageables
         * @return this
         */

        public T setAffectedEntityEffects(Consumer<LivingEntity> effects) {
            baseMeta.affectedEntityEffects = effects;
            return self();
        }

        /**
         * Sets the radius of the affected Damageables after the implementations concludes.
         *
         * @param radius The radius
         * @return this
         */

        public T setSpellEffectRadius(float radius) {
            baseMeta.spellEffectRadius = radius;
            return self();
        }

        /**
         * Sets the sounds that will be played relative to the player upon casting a spell
         *
         * @param soundPlayer Interface for playing sound relative to a location
         * @return this
         */

        public T setCastSound(SoundEffect soundPlayer) {
            baseMeta.castSounds = soundPlayer;
         return self();
        }

        public T setCastSound(Sound sound) {
            baseMeta.castSounds = SingularSound.from(sound, 1);
            return self();
        }

        public T setCastSound(Sound sound, float pitch) {
            baseMeta.castSounds = SingularSound.from(sound, pitch);
            return self();
        }

        /**
         * Sets the visual effects that the implementations shows, whether it is a trail of particles
         * or is executed relative to where you look is up to the implementations handler BaseProps is used in.
         *
         * @param effects Effects relative to the implementations
         * @return this
         */

        public T setSpellRelativeEffects(Consumer<Location> effects) {
            baseMeta.spellRelativeEffects = effects;
            return self();
        }

        static class BaseMeta {
            private int affectedEntityDamage = 0;
            private float spellEffectRadius = 0;
            private SoundEffect castSounds = SoundEffect.EMPTY;
            private Consumer<Location> spellRelativeEffects = Common.emptyConsumer();
            private Consumer<LivingEntity> affectedEntityEffects = Common.emptyConsumer();
        }
    }
}

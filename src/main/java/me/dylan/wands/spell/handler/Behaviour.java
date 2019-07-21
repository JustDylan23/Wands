package me.dylan.wands.spell.handler;

import me.dylan.wands.Main;
import me.dylan.wands.spell.SpellEffectUtil;
import me.dylan.wands.spell.spelleffect.sound.SingularSound;
import me.dylan.wands.spell.spelleffect.sound.SoundEffect;
import me.dylan.wands.util.Common;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class Behaviour implements Listener {

    final static Main plugin = Main.getPlugin();

    final int affectedEntityDamage;
    final float spellEffectRadius;
    final SoundEffect castSounds;
    final BiConsumer<Location, World> spellRelativeEffects;
    final BiConsumer<Location, Player> spellRelativeEffects2;
    final Consumer<LivingEntity> entityEffects;

    private final List<String> props = new ArrayList<>();
    private final ImpactCourse impactCourse;
    private final float impactSpeed;

    Behaviour(@Nonnull AbstractBuilder.BaseMeta baseMeta) {
        this.affectedEntityDamage = baseMeta.entityDamage;
        this.spellEffectRadius = baseMeta.spellEffectRadius;
        this.castSounds = baseMeta.castSounds;
        this.spellRelativeEffects = baseMeta.spellRelativeEffects;
        this.spellRelativeEffects2 = baseMeta.spellRelativeEffects2;
        this.entityEffects = baseMeta.entityEffects;
        this.impactSpeed = baseMeta.impactSpeed;
        this.impactCourse = baseMeta.impactCourse;

        addStringProperty("Entity damage", affectedEntityDamage);
        addStringProperty("Effect radius", spellEffectRadius);
        addStringProperty("Impact speed", impactSpeed);
        addStringProperty("Impact direction", impactCourse);
    }

    void addStringProperty(String key, Object value) {
        addStringProperty(key, value, "");
    }

    void addStringProperty(String key, Object value, String unit) {
        props.add("§6" + key.substring(0, 1).toUpperCase() + key.substring(1).toLowerCase() + ":§r " + value.toString().toLowerCase() + " " + unit);
    }

    public abstract boolean cast(Player player, String wandDisplayName);

    void push(Entity entity, Location from, Player player) {
        if (impactSpeed != 0) {
            Location location = entity.getLocation();
            if (impactCourse == ImpactCourse.PLAYER) {
                location.subtract(player.getLocation());
            } else {
                location.subtract(from);
            }
            Vector vector = location.toVector().normalize().multiply(impactSpeed);
            try {
                vector.checkFinite();
                entity.setVelocity(vector);
            } catch (IllegalArgumentException ignored) {
            }
        }
    }

    void applyEntityEffects(Location center, Player player, String wandDisplayName) {
        SpellEffectUtil.getNearbyLivingEntities(player, center, spellEffectRadius).forEach(entity -> {
            push(entity, center, player);
            SpellEffectUtil.damageEffect(player, entity, affectedEntityDamage, wandDisplayName);
            entityEffects.accept(entity);
        });
    }

    @Override
    public final String toString() {
        StringJoiner sj = new StringJoiner("\n");
        Collections.sort(props);
        for (String string : props) {
            sj.add(string);
        }
        return sj.toString();
    }

    public enum ImpactCourse {
        SPELL,
        PLAYER
    }

    public static abstract class AbstractBuilder<T extends AbstractBuilder<T>> {

        final BaseMeta baseMeta = new BaseMeta();

        AbstractBuilder() {
        }

        abstract T self();

        public abstract Behaviour build();

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
         * Sets the damage that will ne dealt to the entities that are hit by the spell.
         *
         * @param damage The amount of damage
         * @return this
         */

        public T setEntityDamage(int damage) {
            baseMeta.entityDamage = damage;
            return self();
        }

        /**
         * Sets the effects which will effect the Damageables in the implementations's effect range.
         *
         * @param effects Effects applied to the affected Damageables
         * @return this
         */

        public T setEntityEffects(Consumer<LivingEntity> effects) {
            baseMeta.entityEffects = effects;
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
         * Sets the visual effects that the implementations shows, whether it is a trail of particles
         * or is executed relative to where you look is up to the implementations handler BaseProps is used in.
         *
         * @param effects Effects relative to the implementations
         * @return this
         */

        public T setSpellRelativeEffects(BiConsumer<Location, World> effects) {
            baseMeta.spellRelativeEffects = effects;
            return self();
        }

        public T setSpellRelativeEffects2(BiConsumer<Location, Player> effects) {
            baseMeta.spellRelativeEffects2 = effects;
            return self();
        }

        public T setImpactSpeed(float speed) {
            baseMeta.impactSpeed = speed;
            return self();
        }

        public T setImpactCourse(ImpactCourse impactCourse) {
            baseMeta.impactCourse = impactCourse;
            return self();
        }

        static class BaseMeta {
            private int entityDamage = 0;
            private float spellEffectRadius, impactSpeed = 0;
            private SoundEffect castSounds = SoundEffect.EMPTY;
            private BiConsumer<Location, World> spellRelativeEffects = Common.emptyBiConsumer();
            private BiConsumer<Location, Player> spellRelativeEffects2 = Common.emptyBiConsumer();
            private Consumer<LivingEntity> entityEffects = Common.emptyConsumer();
            private ImpactCourse impactCourse = ImpactCourse.SPELL;
        }
    }
}

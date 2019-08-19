package me.dylan.wands.spell.types;

import me.dylan.wands.Main;
import me.dylan.wands.knockback.KnockBack;
import me.dylan.wands.sound.SingularSound;
import me.dylan.wands.sound.SoundEffect;
import me.dylan.wands.spell.SpellEffectUtil;
import me.dylan.wands.spell.types.Behaviour.AbstractBuilder.BaseProps;
import me.dylan.wands.spell.types.Behaviour.AbstractBuilder.SpellInfo;
import me.dylan.wands.util.Common;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * All spell types inherit the basic properties of the {@link Behaviour}.
 * <p>
 * Configurable:
 * - Damage direct dealt to affected entities.
 * - KnockBack received by affected entities.
 * - Radius in which entities will be affected.
 * - Effects i.e. potion effects received by affected entities.
 * - Effects i.e. particles relative to the spell / spell's trajectory.
 * - Sound which is played on casting the spell.
 * <p>
 * BaseMeta's constructor requires {@link BaseProps}.
 * {@link BaseProps} can be acquired by implementing {@link AbstractBuilder}.
 */
public abstract class Behaviour implements Listener {
    protected final static Main plugin = Main.getPlugin();

    final int entityDamage;
    final float spellEffectRadius;
    final SoundEffect castSounds;
    final BiConsumer<Location, World> spellRelativeEffects;
    final Consumer<LivingEntity> entityEffects;
    final KnockBack knockBack;
    final BiConsumer<LivingEntity, SpellInfo> extendedEntityEffects;
    final BiConsumer<Location, SpellInfo> extendedSpellRelativeEffects;

    private final List<String> props = new ArrayList<>();

    Behaviour(@NotNull BaseProps baseProps) {
        this.entityDamage = baseProps.entityDamage;
        this.spellEffectRadius = baseProps.spellEffectRadius;
        this.castSounds = baseProps.castSounds;
        this.spellRelativeEffects = baseProps.spellRelativeEffects;
        this.entityEffects = baseProps.entityEffects;
        this.knockBack = baseProps.knockBack;
        this.extendedEntityEffects = baseProps.extendedEntityEffects;
        this.extendedSpellRelativeEffects = baseProps.extendedSpellRelativeEffects;

        if (!baseProps.isEmpty) {
            addPropertyInfo("Entity damage", entityDamage);
            addPropertyInfo("Effect radius", spellEffectRadius, "meters");
            addPropertyInfo("Knock Back xy", knockBack.getXz());
            addPropertyInfo("Knock Back y", knockBack.getY());
        }
    }

    /**
     * Use {@link #Behaviour(BaseProps)} when possible.
     */
    protected Behaviour() {
        this(BaseProps.EMPTY);
    }

    void addPropertyInfo(String key, Object value) {
        addPropertyInfo(key, value, "");
    }

    void addPropertyInfo(@NotNull String key, @NotNull Object value, String unit) {
        props.add("§6" + key.substring(0, 1).toUpperCase() + key.substring(1).toLowerCase() + ":§r " + value.toString().toLowerCase() + " " + unit);
    }

    public abstract boolean cast(@NotNull Player player, @NotNull String weaponName);

    void applyEntityEffects(Player caster, Location from, String weaponName) {
        for (LivingEntity entity : SpellEffectUtil.getNearbyLivingEntities(caster, from, spellEffectRadius)) {
            knockBack.apply(entity, from);
            SpellEffectUtil.damageEffect(caster, entity, entityDamage, weaponName);
            entityEffects.accept(entity);
        }
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

    public enum Target {
        SINGLE,
        MULTI
    }

    public enum KnockBackFrom {
        SPELL,
        PLAYER
    }

    @SuppressWarnings("unused")
    public static abstract class AbstractBuilder<T extends AbstractBuilder<T>> {

        final BaseProps baseProps = new BaseProps();

        AbstractBuilder() {
        }

        abstract T self();

        public abstract Behaviour build();

        public T setCastSound(SoundEffect soundPlayer) {
            baseProps.castSounds = soundPlayer;
            return self();
        }

        public T setCastSound(Sound sound) {
            baseProps.castSounds = SingularSound.from(sound, 1);
            return self();
        }

        public T setCastSound(Sound sound, float pitch) {
            baseProps.castSounds = SingularSound.from(sound, pitch);
            return self();
        }

        public T setEntityDamage(int damage) {
            baseProps.entityDamage = damage;
            return self();
        }

        public T setEntityEffects(Consumer<LivingEntity> effects) {
            baseProps.entityEffects = effects;
            return self();
        }

        public T extendedSetEntityEffects(BiConsumer<LivingEntity, SpellInfo> effects) {
            baseProps.extendedEntityEffects = effects;
            return self();
        }

        public T setSpellEffectRadius(float radius) {
            baseProps.spellEffectRadius = radius;
            return self();
        }

        public T setSpellRelativeEffects(BiConsumer<Location, World> effects) {
            baseProps.spellRelativeEffects = effects;
            return self();
        }

        public T extendedSetSpellRelativeEffects(BiConsumer<Location, SpellInfo> effects) {
            baseProps.extendedSpellRelativeEffects = effects;
            return self();

        }

        public T setKnockBack(float xz, float y) {
            baseProps.knockBack = KnockBack.from(xz, y);
            return self();
        }

        public T setKnockBack(float xz) {
            baseProps.knockBack = KnockBack.from(xz);
            return self();
        }

        public T setKnockBack(KnockBack knockBack) {
            baseProps.knockBack = knockBack;
            return self();
        }

        public static class SpellInfo {
            public final Player caster;
            public final Location origination;
            public final World world;
            public final Supplier<Location> spellLocation;

            public SpellInfo(@NotNull Player caster, @NotNull Location origination, @NotNull Supplier<Location> spellLocation) {
                this.caster = caster;
                this.origination = origination;
                this.world = origination.getWorld();
                this.spellLocation = spellLocation;
            }
        }

        static class BaseProps {
            private final static BaseProps EMPTY = new BaseProps(true);
            private final boolean isEmpty;
            private int entityDamage = 0;
            private float spellEffectRadius;
            private SoundEffect castSounds = SoundEffect.NONE;
            private BiConsumer<Location, World> spellRelativeEffects = Common.emptyBiConsumer();
            private BiConsumer<Location, SpellInfo> extendedSpellRelativeEffects = Common.emptyBiConsumer();
            private Consumer<LivingEntity> entityEffects = Common.emptyConsumer();
            private BiConsumer<LivingEntity, SpellInfo> extendedEntityEffects = Common.emptyBiConsumer();
            private KnockBack knockBack = KnockBack.NONE;

            private BaseProps() {
                this.isEmpty = false;
            }

            private BaseProps(boolean isEmpty) {
                this.isEmpty = isEmpty;
            }
        }
    }
}

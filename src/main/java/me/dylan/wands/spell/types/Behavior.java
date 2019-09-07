package me.dylan.wands.spell.types;

import me.dylan.wands.miscellaneous.utils.Common;
import me.dylan.wands.spell.tools.KnockBack;
import me.dylan.wands.spell.tools.SpellInfo;
import me.dylan.wands.spell.tools.sound.SingularSound;
import me.dylan.wands.spell.tools.sound.SoundEffect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;
import java.util.function.BiConsumer;

/**
 * All spell types inherit the basic properties of the {@link Behavior}.
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
public abstract class Behavior {
    final int entityDamage;
    final float spellEffectRadius;
    final SoundEffect castSounds;
    final BiConsumer<Location, SpellInfo> spellRelativeEffects;
    final BiConsumer<LivingEntity, SpellInfo> entityEffects;
    final KnockBack knockBack;
    final PotionEffect[] potionEffects;

    private final List<String> props = new ArrayList<>();

    Behavior(@NotNull BaseProps baseProps) {
        this.entityDamage = baseProps.entityDamage;
        this.spellEffectRadius = baseProps.spellEffectRadius;
        this.castSounds = baseProps.castSounds;
        this.spellRelativeEffects = baseProps.spellRelativeEffects;
        this.entityEffects = baseProps.entityEffects;
        this.knockBack = baseProps.knockBack;
        this.potionEffects = baseProps.potionEffects;

        if (!baseProps.isEmpty()) {
            addPropertyInfo("Entity damage", entityDamage);
            addPropertyInfo("Effect radius", spellEffectRadius, "meters");
            addPropertyInfo("Knock Back xy", knockBack.getXz());
            addPropertyInfo("Knock Back y", knockBack.getY());
        }
    }

    /**
     * Use {@link #Behavior(BaseProps)} when possible.
     */
    protected Behavior() {
        this(BaseProps.EMPTY);
    }

    void addPropertyInfo(String key, Object value) {
        addPropertyInfo(key, value, "");
    }

    void addPropertyInfo(@NotNull String key, @NotNull Object value, String unit) {
        props.add("§6" + key.substring(0, 1).toUpperCase() + key.substring(1).toLowerCase() + ":§r " + value.toString().toLowerCase() + " " + unit);
    }

    public abstract boolean cast(@NotNull Player player, @NotNull String weaponName);

    @Override
    public final String toString() {
        StringJoiner stringJoiner = new StringJoiner("\n");
        Collections.sort(props);
        for (String prop : props) {
            stringJoiner.add(prop);
        }
        return stringJoiner.toString();
    }

    public enum Target {
        SINGLE,
        MULTI
    }

    public enum KnockBackFrom {
        SPELL,
        PLAYER
    }

    public abstract static class AbstractBuilder<T extends AbstractBuilder<T>> {

        final Behavior.BaseProps baseProps = new Behavior.BaseProps();

        AbstractBuilder() {
        }

        abstract T self();

        public abstract @NotNull Behavior build();

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

        public T setEntityEffects(BiConsumer<LivingEntity, SpellInfo> effects) {
            baseProps.entityEffects = effects;
            return self();
        }

        public T setPotionEffects(PotionEffect... potionEffects) {
            baseProps.potionEffects = potionEffects;
            return self();
        }

        public T setSpellEffectRadius(float radius) {
            baseProps.spellEffectRadius = radius;
            return self();
        }

        public T setSpellRelativeEffects(BiConsumer<Location, SpellInfo> effects) {
            baseProps.spellRelativeEffects = effects;
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

    }

    private static class BaseProps {
        static final BaseProps EMPTY = new BaseProps() {
            @Override
            public boolean isEmpty() {
                return true;
            }
        };
        int entityDamage;
        float spellEffectRadius;
        SoundEffect castSounds = SoundEffect.NONE;
        BiConsumer<Location, SpellInfo> spellRelativeEffects = Common.emptyBiConsumer();
        BiConsumer<LivingEntity, SpellInfo> entityEffects = Common.emptyBiConsumer();
        KnockBack knockBack = KnockBack.NONE;
        PotionEffect[] potionEffects = new PotionEffect[0];

        boolean isEmpty() {
            return false;
        }
    }
}

package me.dylan.wands.spell.spellbuilders;

import me.dylan.wands.spell.accessories.KnockBack;
import me.dylan.wands.spell.accessories.SpellInfo;
import me.dylan.wands.spell.accessories.sound.SingularSound;
import me.dylan.wands.spell.accessories.sound.SoundEffect;
import me.dylan.wands.utils.Common;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public abstract class BuildableBehaviour extends Behavior {
    final int entityDamage;
    final float spellEffectRadius;
    final SoundEffect castSounds;
    final BiConsumer<Location, SpellInfo> spellRelativeEffects;
    final BiConsumer<LivingEntity, SpellInfo> entityEffects;
    final KnockBack knockBack;
    final PotionEffect[] potionEffects;

    BuildableBehaviour(@NotNull BaseProps baseProps) {
        this.entityDamage = baseProps.entityDamage;
        this.spellEffectRadius = baseProps.spellEffectRadius;
        this.castSounds = baseProps.castSounds;
        this.spellRelativeEffects = baseProps.spellRelativeEffects;
        this.entityEffects = baseProps.entityEffects;
        this.knockBack = baseProps.knockBack;
        this.potionEffects = baseProps.potionEffects;

        addPropertyInfo("Entity damage", entityDamage);
        addPropertyInfo("Effect radius", spellEffectRadius, "meters");
        addPropertyInfo("Knock Back xy", knockBack.getXz());
        addPropertyInfo("Knock Back y", knockBack.getY());
    }

    public enum Target {
        SINGLE,
        MULTI
    }

    public enum KnockBackDirection {
        SPELL,
        PLAYER
    }

    public abstract static class AbstractBuilder<T extends AbstractBuilder<T>> {

        final BuildableBehaviour.BaseProps baseProps = new BuildableBehaviour.BaseProps();

        AbstractBuilder() {
        }

        abstract T self();

        /**
         * @return new instance
         */

        @Contract("-> new")
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
            baseProps.potionEffects = potionEffects.clone();
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
        int entityDamage;
        float spellEffectRadius;
        SoundEffect castSounds = SoundEffect.NONE;
        BiConsumer<Location, SpellInfo> spellRelativeEffects = Common.emptyBiConsumer();
        BiConsumer<LivingEntity, SpellInfo> entityEffects = Common.emptyBiConsumer();
        KnockBack knockBack = KnockBack.NONE;
        PotionEffect[] potionEffects = new PotionEffect[0];
    }
}

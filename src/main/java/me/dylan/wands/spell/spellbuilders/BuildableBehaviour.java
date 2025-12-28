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
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

public abstract class BuildableBehaviour extends Behavior {
    final int entityDamage;
    final float spellEffectRadius;
    final SoundEffect castSounds;
    final BiConsumer<Location, SpellInfo> spellRelativeEffects;
    final BiConsumer<LivingEntity, SpellInfo> entityEffects;
    final KnockBack knockBack;
    private final PotionEffect[] potionEffects;

    <T extends AbstractBuilder<T>> BuildableBehaviour(@NotNull AbstractBuilder<T> abstractBuilder) {
        this.entityDamage = abstractBuilder.entityDamage;
        this.spellEffectRadius = abstractBuilder.spellEffectRadius;
        this.castSounds = abstractBuilder.castSounds;
        this.spellRelativeEffects = abstractBuilder.spellRelativeEffects;
        this.entityEffects = abstractBuilder.entityEffects;
        this.knockBack = abstractBuilder.knockBack;
        this.potionEffects = abstractBuilder.potionEffects;

        addPropertyInfo("Entity damage", entityDamage);
        addPropertyInfo("Effect radius", spellEffectRadius, "meters");
        addPropertyInfo("Knockback xz", knockBack.getXz());
        addPropertyInfo("Knockback y", knockBack.getY());
    }

    public void applyPotionEffects(LivingEntity livingEntity) {
        for (PotionEffect potionEffect : potionEffects) {
            livingEntity.addPotionEffect(potionEffect);
        }
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

        private static final PotionEffect[] POTION_EFFECTS = new PotionEffect[0];
        int entityDamage = 0;
        float spellEffectRadius = 0.0F;
        SoundEffect castSounds = SoundEffect.NONE;
        BiConsumer<Location, SpellInfo> spellRelativeEffects = Common.emptyBiConsumer();
        BiConsumer<LivingEntity, SpellInfo> entityEffects = Common.emptyBiConsumer();
        KnockBack knockBack = KnockBack.NONE;
        PotionEffect[] potionEffects = POTION_EFFECTS;

        AbstractBuilder() {
        }

        abstract T self();

        public abstract @NotNull Behavior build();

        public T setCastSound(SoundEffect soundPlayer) {
            this.castSounds = soundPlayer;
            return self();
        }

        public T setCastSound(Sound sound) {
            this.castSounds = SingularSound.from(sound, 1);
            return self();
        }

        public T setCastSound(Sound sound, float pitch) {
            this.castSounds = SingularSound.from(sound, pitch);
            return self();
        }

        public T setEntityDamage(int damage) {
            this.entityDamage = damage;
            return self();
        }

        public T setEntityEffects(BiConsumer<LivingEntity, SpellInfo> effects) {
            this.entityEffects = effects;
            return self();
        }

        public T setPotionEffects(PotionEffect... potionEffects) {
            this.potionEffects = potionEffects;
            return self();
        }

        public T setSpellEffectRadius(float radius) {
            this.spellEffectRadius = radius;
            return self();
        }

        public T setSpellRelativeEffects(BiConsumer<Location, SpellInfo> effects) {
            this.spellRelativeEffects = effects;
            return self();
        }

        public T setKnockBack(float xz, float y) {
            this.knockBack = KnockBack.from(xz, y);
            return self();
        }

        public T setKnockBack(float xz) {
            this.knockBack = KnockBack.from(xz);
            return self();
        }

        public T setKnockBack(KnockBack knockBack) {
            this.knockBack = knockBack;
            return self();
        }
    }
}

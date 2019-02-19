package me.dylan.wands.spellbehaviour;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.function.Consumer;

public abstract class SpellBuilder<T extends SpellBuilder, S extends SpellBehaviour> {

    int entityDamage = 3;
    float effectAreaRange = 2;
    Consumer<Location> castEffects;
    Consumer<Location> visualEffects;
    Consumer<Entity> entityEffects;

    protected abstract T getInstance();

    public abstract S build();

    public final T setEffectAreaRange(float range) {
        this.effectAreaRange = range;
        return getInstance();
    }

    public final T onCast(Consumer<Location> castEffects) {
        this.castEffects = castEffects;
        return getInstance();
    }

    public final T setVisualEffects(Consumer<Location> effects) {
        this.visualEffects = effects;
        return getInstance();
    }

    public final T setEntityEffects(Consumer<Entity> effects) {
        this.entityEffects = effects;
        return getInstance();
    }

    public final T setEntityDamage(int damage) {
        this.entityDamage = damage;
        return getInstance();
    }
}
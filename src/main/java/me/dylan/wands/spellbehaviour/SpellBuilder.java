package me.dylan.wands.spellbehaviour;

import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.function.Consumer;

public abstract class SpellBuilder<T extends SpellBuilder, S extends SpellBehaviour> {

    private static final Consumer<?> EMPTY_CONSUMER = e -> {
    };

    @SuppressWarnings("unchecked")
    <V> Consumer<V> emptyConsumer() {
        return (Consumer<V>) EMPTY_CONSUMER;
    }

    int entityDamage = 3;
    float effectAreaRange = 2;
    float pushSpeed = 0;
    Consumer<Location> castEffects = emptyConsumer();
    Consumer<Location> visualEffects = emptyConsumer();
    Consumer<Entity> entityEffects = emptyConsumer();

    protected abstract T getInstance();

    public abstract S build();

    public final T setEntityDamage(int damage) {
        this.entityDamage = damage;
        return getInstance();
    }

    public final T setEffectAreaRange(float range) {
        this.effectAreaRange = range;
        return getInstance();
    }

    public final T setPushSpeed(float multiplier) {
        this.pushSpeed = multiplier;
        return getInstance();
    }

    public final T setCastEffects(Consumer<Location> castEffects) {
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
}
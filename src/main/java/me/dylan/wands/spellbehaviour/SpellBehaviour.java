package me.dylan.wands.spellbehaviour;

import me.dylan.wands.Wands;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class SpellBehaviour implements Listener {
    final static Wands plugin = Wands.getPlugin();
    private static final Map<Player, Long> lastUsed = new HashMap<>();
    final int entityDamage;
    final float effectAreaRange;
    final Consumer<Location> castEffects;
    final Consumer<Location> visualEffects;
    final Consumer<Entity> entityEffects;

    SpellBehaviour(BaseProperties basePropperties) {
        this.entityDamage = basePropperties.entityDamage;
        this.effectAreaRange = basePropperties.effectAreaRange;
        this.castEffects = basePropperties.castEffects;
        this.visualEffects = basePropperties.visualEffects;
        this.entityEffects = basePropperties.entityEffects;
    }

    abstract void cast(Player player);

    public static BaseProperties createEmptyBaseProperties() {
        return new BaseProperties();
    }

    @EventHandler
    public static void onQuit(PlayerQuitEvent event) {
        lastUsed.remove(event.getPlayer());
    }

    public void castWithCoolDownFrom(Player player) {
        if (handleCoolDown(player)) {
            cast(player);
        }
    }

    private boolean handleCoolDown(Player player) {
        Long previous = lastUsed.get(player);
        long now = System.currentTimeMillis();
        int coolDownTime = Wands.getPlugin().getCoolDownTime();
        if (previous == null) {
            lastUsed.put(player, now);
            return true;
        } else if (now - previous > coolDownTime * 1000) {
            lastUsed.put(player, now);
            return true;
        } else {
            long i = coolDownTime - ((now - previous) / 1000);
            player.sendActionBar("§6Wait §7" + i + " §6second" + ((i != 1) ? "s" : ""));
            player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_AMBIENT, 0.3F, 1);
            return false;
        }
    }

    public static class BaseProperties {
        private final static Consumer<?> EMPTY_CONSUMER = e -> {
        };
        private int entityDamage = 3;
        private float effectAreaRange = 2;
        private Consumer<Location> castEffects = emptyConsumer();
        private Consumer<Location> visualEffects = emptyConsumer();
        private Consumer<Entity> entityEffects = emptyConsumer();

        private BaseProperties() {
        }

        @SuppressWarnings("unchecked")
        <T> Consumer<T> emptyConsumer() {
            return (Consumer<T>) EMPTY_CONSUMER;
        }

        /**
         * Sets the damage that is applied to the Damageable effected by the spell.
         * @param damage The amount of damage
         * @return this
         */

        public BaseProperties setEntityDamage(int damage) {
            this.entityDamage = damage;
            return this;
        }

        /**
         * Sets the radius of the affected Damageables after the spell concludes.
         * @param radius The radius
         * @return this
         */

        public BaseProperties setEffectRadius(float radius) {
            this.effectAreaRange = radius;
            return this;
        }

        /**
         * Sets the effect that will be executed relative to the player.
         * @param castEffects Effects relative to the player
         * @return this
         */

        public BaseProperties setCastEffects(Consumer<Location> castEffects) {
            this.castEffects = castEffects;
            return this;
        }

        /**
         * Sets the visual effects that the spell shows, whether it is a trail of particles
         * or is executed relative to where you look is up to the spell type BasePropperties is used in.
         * @param effects Effects relative to the spell
         * @return this
         */

        public BaseProperties setVisualEffects(Consumer<Location> effects) {
            this.visualEffects = effects;
            return this;
        }

        /**
         * Sets the effects which will effect the Damageables in the spell's effect range.
         * @param effects Effects applied to the affected Damageables
         * @return this
         */

        public BaseProperties setEntityEffects(Consumer<Entity> effects) {
            this.entityEffects = effects;
            return this;
        }
    }
}

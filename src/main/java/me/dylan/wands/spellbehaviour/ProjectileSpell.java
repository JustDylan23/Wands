package me.dylan.wands.spellbehaviour;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.util.Vector;

import java.util.function.Consumer;

public class ProjectileSpell<T extends Projectile> extends SpellBehaviour {

    private final Class<T> projectile;
    private final Consumer<T> projectilePropperties;
    private final int lifeTime;

    private ProjectileSpell(int entityDamage, float effectAreaRange, Consumer<Location> castEffects, Consumer<Location> visualEffects,
                            Consumer<Entity> entityEffects, Class<T> projectile, Consumer<T> projectilePropperties, int lifeTime) {
        super(entityDamage, effectAreaRange, castEffects, visualEffects, entityEffects);
        this.projectile = projectile;
        this.projectilePropperties = projectilePropperties;
        this.lifeTime = lifeTime;
    }

    @Override
    public void executeFrom(Player player) {
        Vector velocity = player.getLocation().getDirection().multiply(2);
        T projectile = player.launchProjectile(this.projectile, velocity);
        projectilePropperties.accept(projectile);
    }

    public static class Builder<T extends Projectile> extends SpellBuilder<Builder<T>, ProjectileSpell> {

        private Class<T> projectileClass;
        private Consumer<T> projectilePropperties;
        private int lifeTime;

        public Builder(Class<T> projectileClass) {
            this.projectileClass = projectileClass;
        }

        @Override
        public ProjectileSpell build() {
            return new ProjectileSpell<>(entityDamage, effectAreaRange, castEffects, visualEffects, entityEffects, projectileClass, projectilePropperties, lifeTime);
        }

        @Override
        protected Builder<T> getInstance() {
            return this;
        }

        public Builder<? extends Projectile> setProjectilePropperties(Consumer<T> propperties) {
            this.projectilePropperties = propperties;
            return this;
        }

        public Builder<? extends Projectile> setLifeTime(int ticks) {
            this.lifeTime = ticks;
            return this;
        }
    }
}

package me.dylan.wands.spellbehaviour;

import me.dylan.wands.Wands;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.function.Consumer;

public final class ProjectileSpell<T extends Projectile> extends SpellBehaviour implements Listener {

    private final Class<T> projectile;
    private final Consumer<T> projectilePropperties;
    private final Consumer<Location> hitEffects;
    private final float speed;
    private final int lifeTime;
    private final String metadataTag;

    private ProjectileSpell(int entityDamage, float effectAreaRange, float pushSpeed, Consumer<Location> castEffects, Consumer<Location> visualEffects, Consumer<Entity> entityEffects, Class<T> projectile, Consumer<T> projectilePropperties, Consumer<Location> hitEffects, float speed, int lifeTime, String tag) {
        super(entityDamage, effectAreaRange, pushSpeed, castEffects, visualEffects, entityEffects);
        plugin.registerListener(this);
        this.projectile = projectile;
        this.projectilePropperties = projectilePropperties;
        this.hitEffects = hitEffects;
        this.speed = speed;
        this.lifeTime = lifeTime;
        this.metadataTag = tag;
    }

    @Override
    public void executeFrom(Player player) {
        Vector velocity = player.getLocation().getDirection().multiply(speed);
        T projectile = player.launchProjectile(this.projectile, velocity);
        trail(projectile);
        projectilePropperties.accept(projectile);
        projectile.setMetadata(metadataTag, new FixedMetadataValue(plugin, true));
        activateLifeTimer(projectile);
        castEffects.accept(player.getLocation());
    }

    private void hit(Player player, Projectile projectile) {
        projectile.remove();
        Location loc = projectile.getLocation();
        getNearbyDamageables(player, loc, effectAreaRange).forEach(entity -> {
            entityEffects.accept(entity);

            entity.damage(entityDamage, player);
            entity.setVelocity(new Vector(0, 0, 0));
            pushFrom(loc, entity, pushSpeed);
        });
        hitEffects.accept(loc);
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();
        if (projectile.hasMetadata(metadataTag)) {
            hit((Player) projectile.getShooter(), projectile);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE && event.getDamager().hasMetadata(metadataTag)) {
            event.setCancelled(true);
        }
    }

    private void activateLifeTimer(Projectile projectile) {
        Bukkit.getScheduler().runTaskLater(Wands.getPlugin(), () -> {
            if (projectile.isValid()) {
                hit((Player) projectile.getShooter(), projectile);
            }
        }, lifeTime);
    }

    private void trail(Projectile projectile) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (projectile.isValid()) {
                    visualEffects.accept(projectile.getLocation());

                } else cancel();
            }
        }.runTaskTimer(Wands.getPlugin(), 0, 1);
    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        if (event.getEntity().hasMetadata(metadataTag)) {
            event.setCancelled(true);
        }
    }

    public static class Builder<T extends Projectile> extends SpellBuilder<Builder<T>, ProjectileSpell> {

        private Class<T> projectileClass;
        private Consumer<T> projectilePropperties = emptyConsumer();
        private Consumer<Location> hitEffects = emptyConsumer();
        private float speed;
        private int lifeTime;
        private String metadataTag;

        public Builder(Class<T> projectileClass, String tag, float speed) {
            this.projectileClass = projectileClass;
            this.speed = speed;
            this.metadataTag = tag;
        }

        @Override
        public ProjectileSpell build() {
            return new ProjectileSpell<>(entityDamage, effectAreaRange, pushSpeed, castEffects, visualEffects, entityEffects, projectileClass, projectilePropperties, hitEffects, speed, lifeTime, metadataTag);
        }

        @Override
        protected Builder<T> getInstance() {
            return this;
        }

        public Builder<? extends Projectile> setProjectilePropperties(Consumer<T> propperties) {
            this.projectilePropperties = propperties;
            return this;
        }

        public Builder<? extends Projectile> setHitEffects(Consumer<Location> effects) {
            this.hitEffects = effects;
            return this;
        }

        public Builder<? extends Projectile> setLifeTime(int ticks) {
            this.lifeTime = ticks;
            return this;
        }
    }
}

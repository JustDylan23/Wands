package me.dylan.wands.spell.handler;

import me.dylan.wands.pluginmeta.ListenerRegistry;
import me.dylan.wands.util.EffectUtil;
import me.dylan.wands.util.ShorthandUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.function.Consumer;

public final class ProjectileSpell<T extends org.bukkit.entity.Projectile> extends Behaviour implements Listener {
    private static int idCount;
    private final Class<T> projectile;
    private final Consumer<T> projectileProps;
    private final Consumer<Location> hitEffects;
    private final float speed;
    private final int lifeTime;
    private final float pushSpeed;
    private final String metadataTag;

    //can be accessed via builder
    private ProjectileSpell(Builder<T> builder) {
        super(builder.baseMeta);
        this.projectile = builder.projectile;
        this.projectileProps = builder.projectileProps;
        this.hitEffects = builder.hitEffects;
        this.speed = builder.speed;
        this.lifeTime = builder.lifeTime;
        this.pushSpeed = builder.pushSpeed;
        this.metadataTag = "ProjectileSpell_" + ++idCount;
        ListenerRegistry.addListener(this);
    }

    public static <T extends org.bukkit.entity.Projectile> Builder<T> newBuilder(Class<T> projectileClass, float speed) {
        return new Builder<>(projectileClass, speed);
    }

    @Override
    public boolean cast(Player player) {
        Vector velocity = player.getLocation().getDirection().multiply(speed);
        T projectile = player.launchProjectile(this.projectile, velocity);
        trail(projectile);
        projectileProps.accept(projectile);
        projectile.setMetadata(metadataTag, ShorthandUtil.METADATA_VALUE_TRUE);
        activateLifeTimer(projectile);
        castEffects.accept(player.getLocation());
        return true;
    }

    private void hit(Player player, org.bukkit.entity.Projectile projectile) {
        projectile.remove();
        Location loc = projectile.getLocation();
        hitEffects.accept(loc);
        EffectUtil.getNearbyDamageables(player, loc, effectRadius).forEach(entity -> {
            entityEffects.accept(entity);
            entity.damage(entityDamage);
            EffectUtil.removeVelocity(entity);
            pushFrom(loc, entity, pushSpeed);
        });
    }

    @EventHandler
    private void onProjectileHit(ProjectileHitEvent event) {
        org.bukkit.entity.Projectile projectile = event.getEntity();
        if (projectile.hasMetadata(metadataTag)) {
            hit((Player) projectile.getShooter(), projectile);
        }
    }

    @EventHandler
    private void onDamage(EntityDamageByEntityEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE && event.getDamager().hasMetadata(metadataTag)) {
            event.setCancelled(true);
        }
    }

    private void activateLifeTimer(org.bukkit.entity.Projectile projectile) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (projectile.isValid()) {
                hit((Player) projectile.getShooter(), projectile);
            }
        }, lifeTime);
    }

    private void trail(Entity entity) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity.isValid()) {
                    visualEffects.accept(entity.getLocation());
                } else cancel();
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    @EventHandler
    private void onEntityExplode(EntityExplodeEvent event) {
        if (event.getEntity().hasMetadata(metadataTag)) {
            event.setCancelled(true);
        }
    }

    private void pushFrom(Location origin, Entity entity, float speed) {
        if (speed != 0) {
            Location location = entity.getLocation().subtract(origin);
            Vector vector = location.toVector().normalize().multiply(speed);
            if (!Double.isFinite(vector.getX()) || !Double.isFinite(vector.getY()) || !Double.isFinite(vector.getZ())) {
                vector = new Vector(0, 0.2, 0);
            }
            entity.setVelocity(vector);
        }
    }

    @Override
    public String toString() {
        return super.toString() + "ID count: " + idCount
                + "\nSpeed: " + speed
                + "\nLife time: " + lifeTime + " ticks"
                + "\nPush speed: " + pushSpeed;
    }

    public static class Builder<T extends org.bukkit.entity.Projectile> extends AbstractBuilder<Builder<T>> {

        private final Class<T> projectile;
        private final float speed;

        private Consumer<T> projectileProps = ShorthandUtil.emptyConsumer();
        private Consumer<Location> hitEffects = ShorthandUtil.emptyConsumer();
        private int lifeTime = 20;
        private int pushSpeed;

        private Builder(Class<T> projectileClass, float speed) throws NullPointerException {
            this.projectile = projectileClass;
            this.speed = speed;
        }

        @Override
        Builder<T> self() {
            return this;
        }

        @Override
        public Behaviour build() {
            return new ProjectileSpell<>(this);
        }

        public Builder<T> setProjectileProps(Consumer<T> projectileProps) {
            this.projectileProps = projectileProps;
            return self();
        }

        public Builder<T> setHitEffects(Consumer<Location> hitEffects) {
            this.hitEffects = hitEffects;
            return self();
        }

        public Builder<T> setLifeTime(int ticks) {
            this.lifeTime = ticks;
            return self();
        }

        public Builder<T> setPushSpeed(int pushSpeed) {
            this.pushSpeed = pushSpeed;
            return self();
        }
    }
}

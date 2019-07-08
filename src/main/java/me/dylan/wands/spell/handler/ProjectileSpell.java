package me.dylan.wands.spell.handler;

import me.dylan.wands.pluginmeta.ListenerRegistry;
import me.dylan.wands.util.Common;
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

import java.util.UUID;
import java.util.function.Consumer;

public final class ProjectileSpell<T extends Projectile> extends Behaviour implements Listener {
    private final Class<T> projectile;
    private final Consumer<T> projectileProps;
    private final Consumer<Location> hitEffects;
    private final float speed;
    private final int lifeTime;
    private final String tagProjectileSpell;

    private ProjectileSpell(Builder<T> builder) {
        super(builder.baseMeta);
        this.projectile = builder.projectile;
        this.projectileProps = builder.projectileProps;
        this.hitEffects = builder.hitEffects;
        this.speed = builder.speed;
        this.lifeTime = builder.lifeTime;
        this.tagProjectileSpell = UUID.randomUUID().toString();
        ListenerRegistry.addListener(this);

        addStringProperty("Projectile", projectile.getSimpleName());
        addStringProperty("Speed", speed);
        addStringProperty("Life time", lifeTime, "ticks");
    }

    public static <T extends Projectile> Builder<T> newBuilder(Class<T> projectileClass, float speed) {
        return new Builder<>(projectileClass, speed);
    }

    @Override
    public boolean cast(Player player, String wandDisplayName) {
        castSounds.play(player);
        Vector velocity = player.getLocation().getDirection().multiply(speed);
        T projectile = player.launchProjectile(this.projectile, velocity);
        trail(projectile);
        projectileProps.accept(projectile);
        projectile.setMetadata(tagProjectileSpell, new FixedMetadataValue(plugin, wandDisplayName));
        activateLifeTimer(projectile);
        return true;
    }

    private void hit(Player player, Projectile projectile) {
        projectile.remove();
        Location loc = projectile.getLocation();
        hitEffects.accept(loc);
        applyEntityEffects(loc, player, projectile.getMetadata(tagProjectileSpell).get(0).asString());
    }

    @EventHandler
    private void onProjectileHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();
        if (projectile.hasMetadata(tagProjectileSpell)) {
            hit((Player) projectile.getShooter(), projectile);
        }
    }

    @EventHandler
    private void onDamage(EntityDamageByEntityEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE && event.getDamager().hasMetadata(tagProjectileSpell)) {
            event.setCancelled(true);
        }
    }

    private void activateLifeTimer(Projectile projectile) {
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
                    spellRelativeEffects.accept(entity.getLocation());
                } else cancel();
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    @EventHandler
    private void onEntityExplode(EntityExplodeEvent event) {
        if (event.getEntity().hasMetadata(tagProjectileSpell)) {
            event.setCancelled(true);
        }
    }

    public static final class Builder<T extends Projectile> extends AbstractBuilder<Builder<T>> {

        private final Class<T> projectile;
        private final float speed;

        private Consumer<T> projectileProps = Common.emptyConsumer();
        private Consumer<Location> hitEffects = Common.emptyConsumer();
        private int lifeTime = 20;

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
            return this;
        }

        public Builder<T> setHitEffects(Consumer<Location> hitEffects) {
            this.hitEffects = hitEffects;
            return this;
        }

        public Builder<T> setLifeTime(int ticks) {
            this.lifeTime = ticks;
            return this;
        }
    }
}

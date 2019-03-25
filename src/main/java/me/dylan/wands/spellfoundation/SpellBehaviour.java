package me.dylan.wands.spellfoundation;

import me.dylan.wands.Wands;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public abstract class SpellBehaviour {
    private final static Wands plugin = Wands.getPlugin();

    final int entityDamage;
    final float effectAreaRange;
    final Consumer<Location> castEffects;
    final Consumer<Location> visualEffects;
    final Consumer<Entity> entityEffects;

    private static final Map<Player, Long> lastUsed = new HashMap<>();

    private SpellBehaviour(BaseProperties basePropperties) {
        this.entityDamage = basePropperties.entityDamage;
        this.effectAreaRange = basePropperties.effectAreaRange;
        this.castEffects = basePropperties.castEffects;
        this.visualEffects = basePropperties.visualEffects;
        this.entityEffects = basePropperties.entityEffects;
    }

    public static Iterable<Damageable> getNearbyDamageables(Player player, Location loc, double radius) {
        return loc.getWorld()
                .getNearbyEntities(loc, radius, radius, radius).stream()
                .filter(Damageable.class::isInstance)
                .filter(entity -> !entity.equals(player))
                .map(Damageable.class::cast)
                .collect(Collectors.toList());
    }

    public static void damage(int damage, Entity source, Damageable victim) {
        victim.damage(damage, source);
        victim.setVelocity(new Vector(0, 0, 0));
    }

    public static BaseProperties createEmptyBaseProperties() {
        return new BaseProperties();
    }

    protected abstract void executeFrom(Player player);

    void executeWithCoolDownFrom(Player player) {
        if (handleCoolDown(player)) {
            executeFrom(player);
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

    @EventHandler
    public static void onQuit(PlayerQuitEvent event) {
        lastUsed.remove(event.getPlayer());
    }

    private interface Buildable<T extends SpellBehaviour> {
        T build();
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
        <V> Consumer<V> emptyConsumer() {
            return (Consumer<V>) EMPTY_CONSUMER;
        }

        public BaseProperties setEntityDamage(int damage) {
            this.entityDamage = damage;
            return this;
        }

        public BaseProperties setEffectAreaRange(float range) {
            this.effectAreaRange = range;
            return this;
        }

        public BaseProperties setCastEffects(Consumer<Location> castEffects) {
            this.castEffects = castEffects;
            return this;
        }

        public BaseProperties setVisualEffects(Consumer<Location> effects) {
            this.visualEffects = effects;
            return this;
        }

        public BaseProperties setEntityEffects(Consumer<Entity> effects) {
            this.entityEffects = effects;
            return this;
        }
    }

    /**
     * SparkSpell class
     */

    public static class SparkSpell extends SpellBehaviour {

        private final int effectDistance;

        //can be accessed via builder
        private SparkSpell(BaseProperties basePropperties, int effectDistance) {
            super(basePropperties);
            this.effectDistance = effectDistance;
        }

        @Override
        public void executeFrom(Player player) {
            Location loc = getSpellLocation(player);
            Iterable<Damageable> effectedEntities = getNearbyDamageables(player, loc, effectAreaRange);
            castEffects.accept(player.getLocation());
            effectedEntities.forEach(entity -> {
                entity.damage(entityDamage, player);
                entity.setVelocity(new Vector(0, 0, 0));
                entityEffects.accept(entity);
            });
            visualEffects.accept(loc);

        }

        private Location getSpellLocation(Player player) {
            Entity entity = player.getTargetEntity(effectDistance);
            if (entity != null) {
                return entity.getLocation();
            }
            Block block = player.getTargetBlock(effectDistance);
            if (block != null) {
                return block.getLocation().toCenterLocation();
            }
            return player.getLocation().getDirection().normalize().multiply(effectDistance).toLocation(player.getWorld()).add(player.getLocation());
        }

        public static class Builder implements Buildable {
            private final BaseProperties baseProperties;
            private int effectDistance;

            public Builder(BaseProperties basePropperties) {
                this.baseProperties = basePropperties;
            }

            public Builder setEffectDistance(int effectDistance) {
                this.effectDistance = effectDistance;
                return this;
            }

            @Override
            public SparkSpell build() {
                return new SparkSpell(baseProperties, effectDistance);
            }
        }
    }

    /**
     * WaveSpell class
     */

    public static class WaveSpell extends SpellBehaviour {

        private final int effectDistance;

        //can be accessed via builder
        private WaveSpell(BaseProperties baseProperties, int effectDistance) {
            super(baseProperties);
            this.effectDistance = effectDistance;
        }

        @Override
        public void executeFrom(Player player) {
            Vector direction = player.getLocation().getDirection().normalize();
            String metaId = System.currentTimeMillis() + "";
            castEffects.accept(direction.clone().multiply(10).toLocation(player.getWorld()).add(player.getEyeLocation()));
            for (int i = 1; i <= effectDistance; i++) {
                Location loc = direction.clone().multiply(i).toLocation(player.getWorld()).add(player.getEyeLocation());
                if (!loc.getBlock().isPassable()) {
                    return;
                }
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    visualEffects.accept(loc);
                    getNearbyDamageables(player, loc, effectAreaRange).forEach(entity -> {
                        if (!entity.hasMetadata(metaId)) {
                            player.setMetadata(metaId, new FixedMetadataValue(plugin, true));
                            damage(entityDamage, player, entity);
                            entityEffects.accept(entity);
                            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                if (entity.isValid()) {
                                    entity.removeMetadata(metaId, plugin);
                                }
                            }, effectDistance);
                        }
                    });
                }, i);
            }
        }

        public static class Builder implements Buildable {
            private final BaseProperties baseProperties;
            private int effectDistance;

            public Builder(BaseProperties basePropperties) {
                this.baseProperties = basePropperties;
            }

            public Builder setEffectDistance(int effectDistance) {
                this.effectDistance = effectDistance;
                return this;
            }

            @Override
            public WaveSpell build() {
                return new WaveSpell(baseProperties, effectDistance);
            }
        }
    }

    /**
     * ProjectileSpell
     *
     * @param <T> The projectile that will be fired.
     */

    public static class ProjectileSpell<T extends Projectile> extends SpellBehaviour implements Listener {

        private static int idCount;
        private final Class<T> projectile;
        private final Consumer<T> projectilePropperties;
        private final Consumer<Location> hitEffects;
        private final float speed;
        private final int lifeTime;
        private final float pushSpeed;
        private final String metadataTag;

        //can be accessed via builder
        private ProjectileSpell(BaseProperties baseProperties, Class<T> projectile, Consumer<T> projectilePropperties, Consumer<Location> hitEffects, float speed, int lifeTime, int pushSpeed) {
            super(baseProperties);
            plugin.addListener(this);
            this.projectile = projectile;
            this.projectilePropperties = projectilePropperties;
            this.hitEffects = hitEffects;
            this.speed = speed;
            this.lifeTime = lifeTime;
            this.pushSpeed = pushSpeed;
            this.metadataTag = "ProjectileSpell_" + ++idCount;
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

        public static class Builder<T extends Projectile> implements Buildable {

            private final Class<T> projectile;
            private final BaseProperties baseProperties;
            private final float speed;

            private Consumer<T> projectilePropperties;
            private Consumer<Location> hitEffects;
            private int lifeTime;
            private int pushSpeed;

            public Builder(Class<T> projectileClass, float speed, BaseProperties baseProperties) {
                this.projectile = projectileClass;
                this.speed = speed;
                this.baseProperties = baseProperties;
            }

            public Builder<T> setProjectilePropperties(Consumer<T> projectilePropperties) {
                this.projectilePropperties = projectilePropperties;
                return this;
            }

            public Builder<T> setHitEffects(Consumer<Location> hitEffects) {
                this.hitEffects = hitEffects;
                return this;
            }

            public Builder<T> setLifeTime(int lifeTime) {
                this.lifeTime = lifeTime;
                return this;
            }

            public Builder<T> setPushSpeed(int pushSpeed) {
                this.pushSpeed = pushSpeed;
                return this;
            }

            @Override
            public ProjectileSpell build() {
                return new ProjectileSpell<>(baseProperties, projectile, projectilePropperties, hitEffects, speed, lifeTime, pushSpeed);
            }
        }
    }
}

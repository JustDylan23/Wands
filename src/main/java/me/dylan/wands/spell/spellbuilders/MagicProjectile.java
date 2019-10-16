package me.dylan.wands.spell.spellbuilders;

import me.dylan.wands.ListenerRegistry;
import me.dylan.wands.WandsPlugin;
import me.dylan.wands.spell.accessories.SpellInfo;
import me.dylan.wands.spell.util.SpellEffectUtil;
import me.dylan.wands.utils.Common;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Shoots projectile from the caster
 * <p>
 * Configurable:
 * - Projectile
 * - Projectile's properties
 * - Projectile's speed
 * - Projectile's lifetime
 * - Visual Effects on location the projectile hits
 *
 * @param <T> Type of projectile which gets fired
 */
public final class MagicProjectile<T extends Projectile> extends BuildableBehaviour implements Listener {
    private static final Set<Projectile> projectiles = new HashSet<>();
    private final Map<Projectile, SpellInfo> caster = new HashMap<>();
    private final Class<T> projectile;
    private final Consumer<T> projectileProps;
    private final BiConsumer<Location, SpellInfo> hitEffects;
    private final float speed;
    private final int lifeTime;
    private final String tagProjectileSpell;

    private MagicProjectile(@NotNull Builder<T> builder) {
        super(builder.baseProps);
        this.projectile = builder.projectile;
        this.projectileProps = builder.projectileProps;
        this.hitEffects = builder.hitEffects;
        this.speed = builder.speed;
        this.lifeTime = builder.lifeTime;
        this.tagProjectileSpell = UUID.randomUUID().toString();
        ListenerRegistry.addListener(this);

        addPropertyInfo("Projectile", projectile.getSimpleName());
        addPropertyInfo("Speed", speed);
        addPropertyInfo("Life time", lifeTime, "ticks");

        WandsPlugin.addDisableLogic(() -> projectiles.forEach(Entity::remove));
    }

    public static @NotNull <T extends Projectile> Builder<T> newBuilder(Class<T> projectileClass, float speed) {
        return new Builder<>(projectileClass, speed);
    }

    @Override
    public boolean cast(@NotNull Player player, @NotNull String weaponName) {
        castSounds.play(player);
        Location origin = player.getLocation();
        Vector velocity = origin.getDirection().multiply(speed);
        T firedProjectile = player.launchProjectile(this.projectile, velocity);
        projectiles.add(firedProjectile);
        SpellInfo spellInfo = new SpellInfo(player, origin, null) {
            public Location spellLocation() {
                return firedProjectile.getLocation();
            }
        };
        caster.put(firedProjectile, spellInfo);
        trail(firedProjectile, spellInfo);
        projectileProps.accept(firedProjectile);
        firedProjectile.setMetadata(tagProjectileSpell, Common.metadataValue(weaponName));
        activateLifeTimer(firedProjectile);
        return true;
    }

    private void projectileHitEffect(Player player, @NotNull Projectile projectile) {
        projectiles.remove(projectile);
        projectile.remove();
        SpellInfo spellInfo = caster.remove(projectile);
        if (spellInfo != null) {
            Location loc = projectile.getLocation();
            hitEffects.accept(loc, spellInfo);
            String weaponName = projectile.getMetadata(tagProjectileSpell).get(0).asString();
            for (LivingEntity entity : SpellEffectUtil.getNearbyLivingEntities(player, loc, spellEffectRadius)) {
                knockBack.apply(entity, loc);
                SpellEffectUtil.damageEffect(player, entity, entityDamage, weaponName);
                entityEffects.accept(entity, spellInfo);
                for (PotionEffect potionEffect : potionEffects) {
                    entity.addPotionEffect(potionEffect, true);
                }
            }
        }
    }

    private void activateLifeTimer(Projectile projectile) {
        Common.runTaskLater(() -> {
            if (projectile.isValid()) {
                projectileHitEffect((Player) projectile.getShooter(), projectile);
            }
        }, lifeTime);
    }

    private void trail(Entity entity, SpellInfo spellInfo) {
        BukkitRunnable bukkitRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (entity.isValid()) {
                    spellRelativeEffects.accept(entity.getLocation(), spellInfo);
                } else cancel();
            }
        };
        Common.runTaskTimer(bukkitRunnable, 0, 1);
    }

    @EventHandler
    private void onProjectileHit(ProjectileHitEvent event) {
        Projectile eventProjectile = event.getEntity();
        if (eventProjectile.hasMetadata(tagProjectileSpell)) {
            projectileHitEffect((Player) eventProjectile.getShooter(), eventProjectile);
        }
    }

    @EventHandler
    private void onDamage(EntityDamageByEntityEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE && event.getDamager().hasMetadata(tagProjectileSpell)) {
            event.setCancelled(true);
        }
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
        private BiConsumer<Location, SpellInfo> hitEffects = Common.emptyBiConsumer();
        private int lifeTime = 20;

        private Builder(Class<T> projectileClass, float speed) {
            this.projectile = projectileClass;
            this.speed = speed;
        }

        @Override
        Builder<T> self() {
            return this;
        }

        @Override
        public @NotNull Behavior build() {
            return new MagicProjectile<>(this);
        }

        public Builder<T> setProjectileProps(Consumer<T> projectileProps) {
            this.projectileProps = projectileProps;
            return this;
        }

        public Builder<T> setHitEffects(BiConsumer<Location, SpellInfo> hitEffects) {
            this.hitEffects = hitEffects;
            return this;
        }

        public Builder<T> setLifeTime(int ticks) {
            this.lifeTime = ticks;
            return this;
        }
    }
}

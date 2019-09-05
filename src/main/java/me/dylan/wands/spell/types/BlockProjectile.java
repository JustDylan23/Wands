package me.dylan.wands.spell.types;

import me.dylan.wands.ListenerRegistry;
import me.dylan.wands.miscellaneous.utils.Common;
import me.dylan.wands.spell.util.SpellEffectUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Shoots x amount of blocks from caster's location.
 * <p>
 * configurable:
 * - Amount of blocks
 * - Delay between shooting
 * - Material of blocks
 * - Speed of blocks
 */
public final class BlockProjectile extends Behavior implements Listener {
    private static final Set<Entity> projectiles = new HashSet<>();
    private final Material material;
    private final float speed;
    private final String tagBlockProjectile;
    private final int amount, delay, metaTime;


    private BlockProjectile(@NotNull Builder builder) {
        super(builder.baseProps);
        this.material = builder.material;
        this.speed = builder.speed;
        this.amount = builder.amount;
        this.delay = builder.delay;
        this.metaTime = amount * delay;
        this.tagBlockProjectile = UUID.randomUUID().toString();


        ListenerRegistry.addListener(this);

        addPropertyInfo("Material", material);
        addPropertyInfo("Speed", speed);
        addPropertyInfo("Amount", amount);
        addPropertyInfo("Delay between projectiles", delay);

        plugin.addDisableLogic(() -> projectiles.forEach(Entity::remove));

    }

    public static @NotNull Builder newBuilder(Material material, float speed) {
        return new Builder(material, speed);
    }

    @Override
    public boolean cast(@NotNull Player player, @NotNull String weaponName) {
        new BukkitRunnable() {
            int count;

            @Override
            public void run() {
                ++count;
                if (count > amount) {
                    cancel();
                } else {
                    castSounds.play(player);
                    FallingBlock fallingBlock = shootBlockProjectile(player);
                    handleSpellEffects(fallingBlock, player, weaponName);
                }
            }

            @NotNull FallingBlock shootBlockProjectile(@NotNull LivingEntity player) {
                Vector velocity = player.getLocation().getDirection().multiply(speed);
                Location location = player.getEyeLocation();
                FallingBlock fallingBlock = location.getWorld().spawnFallingBlock(location, Bukkit.createBlockData(material));
                fallingBlock.setVelocity(velocity);
                fallingBlock.setMetadata(tagBlockProjectile, Common.METADATA_VALUE_TRUE);
                fallingBlock.setDropItem(false);
                projectiles.add(fallingBlock);
                return fallingBlock;
            }

            void handleSpellEffects(Entity fallingBlock, Player player, String wandDisplayName) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (fallingBlock.isValid()) {
                            Location location = fallingBlock.getLocation();
                            spellRelativeEffects.accept(location, location.getWorld());
                            SpellEffectUtil.getNearbyLivingEntities(player, location, entity -> !entity.hasMetadata(tagBlockProjectile), spellEffectRadius)
                                    .forEach(entity -> {
                                        entity.setMetadata(tagBlockProjectile, Common.METADATA_VALUE_TRUE);
                                        Bukkit.getScheduler().runTaskLater(plugin, () -> entity.removeMetadata(tagBlockProjectile, plugin), metaTime);
                                        entityEffects.accept(entity);
                                        knockBack.apply(entity, location.toCenterLocation());
                                        SpellEffectUtil.damageEffect(player, entity, entityDamage, wandDisplayName);
                                    });
                        } else cancel();
                    }
                }.runTaskTimer(plugin, 1, 1);
            }

        }.runTaskTimer(plugin, 0, delay);
        return true;
    }

    @EventHandler
    private void onBlockFall(EntityChangeBlockEvent event) {
        if ((event.getEntityType() == EntityType.FALLING_BLOCK) && event.getEntity().hasMetadata(tagBlockProjectile)) {
            event.setCancelled(true);
            projectiles.remove(event.getEntity());
        }
    }

    public static final class Builder extends AbstractBuilder<Builder> {

        private final float speed;
        private final Material material;
        private int delay, amount = 1;

        private Builder(Material material, float speed) {
            this.material = material;
            this.speed = speed;
        }

        @Override
        Builder self() {
            return this;
        }

        @Override
        public @NotNull Behavior build() {
            return new BlockProjectile(this);
        }

        public Builder setProjectileAmount(int amount) {
            this.amount = amount;
            return this;
        }

        public Builder setProjectileShootDelay(int ticks) {
            this.delay = ticks;
            return this;
        }
    }
}

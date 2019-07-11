package me.dylan.wands.spell.handler;

import me.dylan.wands.pluginmeta.ListenerRegistry;
import me.dylan.wands.spell.SpellEffectUtil;
import me.dylan.wands.util.Common;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.UUID;

public final class BlockProjectileSpell extends Behaviour implements Listener {
    private final Material material;
    private final float speed;
    private final String tagBlockProjectile;
    private final int amount, delay;

    private BlockProjectileSpell(Builder builder) {
        super(builder.baseMeta);
        this.material = builder.material;
        this.speed = builder.speed;
        this.amount = builder.amount;
        this.delay = builder.delay;
        this.tagBlockProjectile = UUID.randomUUID().toString();

        ListenerRegistry.addListener(this);

        addStringProperty("Material", material);
        addStringProperty("Speed", speed);
    }

    public static Builder newBuilder(Material material, float speed) {
        return new Builder(material, speed);
    }

    @Override
    public boolean cast(Player player, String wandDisplayName) {
        new BukkitRunnable() {
            int count = 0;

            @Override
            public void run() {
                if (++count > amount) {
                    cancel();
                } else {
                    castSounds.play(player);
                    FallingBlock fallingBlock = shootBlockProjectile(player);
                    handleRelativeEffects(fallingBlock, player, wandDisplayName);
                }
            }
        }.runTaskTimer(plugin, 0, delay);
        return true;
    }

    private FallingBlock shootBlockProjectile(Player player) {
        Vector velocity = player.getLocation().getDirection().multiply(speed);
        Location location = player.getEyeLocation();
        FallingBlock fallingBlock = location.getWorld().spawnFallingBlock(location, Bukkit.createBlockData(material));
        fallingBlock.setVelocity(velocity);
        fallingBlock.setMetadata(tagBlockProjectile, Common.METADATA_VALUE_TRUE);
        fallingBlock.setDropItem(false);
        return fallingBlock;
    }

    private void handleRelativeEffects(FallingBlock fallingBlock, Player player, String wandDisplayName) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (fallingBlock.isValid()) {
                    spellRelativeEffects.accept(fallingBlock.getLocation(), fallingBlock.getWorld());
                    SpellEffectUtil.getNearbyLivingEntities(player, fallingBlock.getLocation(), spellEffectRadius).forEach(entity -> {
                        if (!entity.hasMetadata(tagBlockProjectile)) {
                            entity.setMetadata(tagBlockProjectile, Common.METADATA_VALUE_TRUE);
                            Bukkit.getScheduler().runTaskLater(plugin, () -> entity.removeMetadata(tagBlockProjectile, plugin), 25L);
                            affectedEntityEffects.accept(entity);
                            SpellEffectUtil.damageEffect(player, entity, affectedEntityDamage, wandDisplayName);
                        }
                    });
                } else cancel();
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    @EventHandler
    private void onBlockFall(EntityChangeBlockEvent event) {
        if ((event.getEntityType() == EntityType.FALLING_BLOCK) && event.getEntity().hasMetadata(tagBlockProjectile)) {
            event.setCancelled(true);
        }
    }

    public static final class Builder extends AbstractBuilder<Builder> {

        private final float speed;
        private final Material material;
        private int delay, amount = 1;

        private Builder(Material material, float speed) throws NullPointerException {
            this.material = material;
            this.speed = speed;
        }

        @Override
        Builder self() {
            return this;
        }

        @Override
        public Behaviour build() {
            return new BlockProjectileSpell(this);
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

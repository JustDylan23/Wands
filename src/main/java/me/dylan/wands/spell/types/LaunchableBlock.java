package me.dylan.wands.spell.types;

import me.dylan.wands.ListenerRegistry;
import me.dylan.wands.WandsPlugin;
import me.dylan.wands.spell.accessories.SpellInfo;
import me.dylan.wands.spell.accessories.sound.SoundEffect;
import me.dylan.wands.spell.util.SpellEffectUtil;
import me.dylan.wands.utils.Common;
import me.dylan.wands.utils.LocationUtil;
import me.dylan.wands.utils.PlayerUtil;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.BiConsumer;

/**
 * Caster can select a block withing a radius of 10 blocks by clicking.
 * clicking again results into the block being launched into the direction the caster is looking in.
 * <p>
 * Configurable:
 * - Material of block which gets launched.
 * - Effects displayed when the block lands.
 * - Block relative sounds, unlike {@link Behavior#castSounds} this sound is played at the location of the block when launched.
 */
public final class LaunchableBlock extends Behavior implements Listener {
    private static final Set<Block> effectedBlocks = new HashSet<>();
    private static final Set<BlockRestorer> pending = new HashSet<>();
    private final String tagUnbreakable, tagFallingBlock;
    private final Material material;
    private final BiConsumer<Location, SpellInfo> hitEffects;
    private final Map<Player, BlockRestorer> selectedBlock = new HashMap<>();
    private final Map<FallingBlock, SpellInfo> caster = new HashMap<>();
    private final SoundEffect blockRelativeSounds;

    private LaunchableBlock(@NotNull Builder builder) {
        super(builder.baseProps);
        ListenerRegistry.addListener(this);
        this.material = builder.material;
        this.hitEffects = builder.hitEffects;
        this.blockRelativeSounds = builder.blockRelativeSounds;
        this.tagUnbreakable = UUID.randomUUID().toString();
        this.tagFallingBlock = UUID.randomUUID().toString();

        addPropertyInfo("Material", material);

        WandsPlugin.addDisableLogic(() -> pending.forEach(BlockRestorer::cancel));
    }

    public static @NotNull Builder newBuilder(Material material) {
        return new Builder(material);
    }

    @Override
    public boolean cast(@NotNull Player player, @NotNull String weaponName) {
        return selectedBlock.containsKey(player) ? launchBlock(player) : prepareBlock(player);
    }

    @SuppressWarnings("SameReturnValue")
    private boolean prepareBlock(@NotNull Player player) {
        Block block = player.getTargetBlockExact(10);
        if (block != null && block.getType() != Material.AIR) {
            if (!effectedBlocks.contains(block)) {
                BlockState oldState = block.getState();
                if (oldState instanceof Container) {
                    if (oldState instanceof Chest) {
                        ((Chest) oldState).getBlockInventory().clear();
                        Inventory inventory = ((InventoryHolder) oldState).getInventory();
                        if (inventory.getHolder() instanceof DoubleChest) {
                            PlayerUtil.sendActionBar(player, "§ccan't be used on double chests");
                            return false;
                        } else inventory.clear();
                    } else {
                        ((InventoryHolder) oldState).getInventory().clear();
                    }
                }
                block.setType(material, false);
                block.setMetadata(tagUnbreakable, Common.getMetadataValueTrue());
                block.getState().update();
                effectedBlocks.add(block);
                BlockRestorer blockRestorer = new BlockRestorer(oldState, player, this);
                selectedBlock.put(player, blockRestorer);
                Common.runTaskLater(blockRestorer, 100L);
            } else {
                PlayerUtil.sendActionBar(player, "§cblock is already taken");
            }
        } else {
            PlayerUtil.sendActionBar(player, "§cselect a closer block");
        }
        return false;
    }

    private boolean launchBlock(Player player) {
        castSounds.play(player);
        BlockRestorer blockRestorer = selectedBlock.get(player);
        blockRestorer.earlyRun();
        Location blockLoc = blockRestorer.originLoc;
        FallingBlock fallingBlock = blockLoc.getWorld().spawnFallingBlock(blockLoc, Bukkit.createBlockData(material));
        fallingBlock.setVelocity(new Vector(0, 1, 0));
        fallingBlock.setMetadata(tagFallingBlock, Common.metadataValue(player.getInventory().getItemInMainHand().getItemMeta().getDisplayName()));
        fallingBlock.setDropItem(false);
        blockRelativeSounds.play(fallingBlock);
        caster.put(fallingBlock, new SpellInfo(player, blockLoc, null) {
            @Override
            public Location spellLocation() {
                return fallingBlock.getLocation();
            }
        });
        trail(player, fallingBlock);
        Block block = player.getTargetBlockExact(30);
        if (block != null) {
            Common.runTaskLater(() -> {
                if (fallingBlock.isValid()) {
                    fallingBlock.setVelocity(
                            LocationUtil.toCenterBlock(block)
                            .subtract(fallingBlock.getLocation())
                            .toVector()
                            .normalize()
                            .multiply(1.2)
                    );
                }
            }, 20L);
            return true;
        }
        return false;
    }

    private void blockLand(FallingBlock fallingBlock) {
        SpellInfo spellInfo = caster.get(fallingBlock);
        if (spellInfo != null) {
            Player player = spellInfo.caster();
            caster.remove(fallingBlock);

            Location loc = fallingBlock.getLocation();
            hitEffects.accept(loc, spellInfo);
            String weaponName = fallingBlock.getMetadata(tagFallingBlock).get(0).asString();
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

    @EventHandler
    private void onBlockFall(EntityChangeBlockEvent event) {
        if ((event.getEntityType() == EntityType.FALLING_BLOCK) && event.getEntity().hasMetadata(tagFallingBlock)) {
            event.setCancelled(true);
            blockLand((FallingBlock) event.getEntity());
        }
    }

    @EventHandler
    private void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().hasMetadata(tagUnbreakable)) event.setCancelled(true);
    }

    private void trail(Player player, FallingBlock entity) {
        SpellInfo spellInfo = new SpellInfo(player, player.getLocation(), null) {
            @Override
            public Location spellLocation() {
                return entity.getLocation();
            }
        };
        BukkitRunnable bukkitRunnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (entity.isValid()) {
                    spellRelativeEffects.accept(entity.getLocation(), spellInfo);
                } else {
                    cancel();
                    blockLand(entity);
                }
            }
        };
        Common.runTaskTimer(bukkitRunnable, 0, 1);
    }

    private static class BlockRestorer implements Runnable {
        private final Location originLoc;
        private final LaunchableBlock parent;
        private final Player player;
        private final BlockState state;
        private boolean canRun = true;

        BlockRestorer(@NotNull BlockState state, Player player, @NotNull LaunchableBlock parent) {
            this.state = state;
            this.originLoc = LocationUtil.toCenterLocation(state.getLocation());
            this.parent = parent;
            this.player = player;
            BukkitRunnable bukkitRunnable = new BukkitRunnable() {
                final World world = originLoc.getWorld();
                final BlockData blockData = parent.material.createBlockData();

                @Override
                public void run() {
                    if (canRun) {
                        world.spawnParticle(Particle.BLOCK_CRACK, originLoc, 10, 0.5, 0.5, 0.5, 0.15, blockData, true);
                    } else cancel();
                }
            };
            Common.runTaskTimer(bukkitRunnable, 1, 1);
            pending.add(this);
        }

        void earlyRun() {
            run();
            canRun = false;
        }

        void cancel() {
            state.update(true);
        }

        @Override
        public void run() {
            if (canRun) {
                canRun = false;
                effectedBlocks.remove(state.getBlock());
                Common.removeMetaData(state, parent.tagUnbreakable);
                state.update(true);
                parent.selectedBlock.remove(player);
                pending.remove(this);
            }
        }
    }

    public static final class Builder extends AbstractBuilder<Builder> {

        private final Material material;
        private BiConsumer<Location, SpellInfo> hitEffects = Common.emptyBiConsumer();
        private SoundEffect blockRelativeSounds = SoundEffect.NONE;

        private Builder(Material material) {
            this.material = material;
        }

        @Override
        Builder self() {
            return this;
        }

        @Override
        public @NotNull Behavior build() {
            return new LaunchableBlock(this);
        }

        public Builder setHitEffects(BiConsumer<Location, SpellInfo> hitEffects) {
            this.hitEffects = hitEffects;
            return this;
        }

        public Builder setBlockRelativeSounds(SoundEffect soundEffect) {
            this.blockRelativeSounds = soundEffect;
            return this;
        }
    }
}

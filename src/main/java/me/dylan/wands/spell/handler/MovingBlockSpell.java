package me.dylan.wands.spell.handler;

import me.dylan.wands.pluginmeta.ListenerRegistry;
import me.dylan.wands.spell.spelleffect.sound.SoundEffect;
import me.dylan.wands.util.Common;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.function.Consumer;

public final class MovingBlockSpell extends Behaviour implements Listener {

    private final String tagUnbreakable, tagFallingBlock;
    private final Material material;
    private final Consumer<Location> hitEffects;
    private final Map<Player, BlockRestorer> selectedBlock = new HashMap<>();
    private final Map<FallingBlock, Player> caster = new HashMap<>();
    private static final List<Block> effectedBlocks = new ArrayList<>();
    private static final List<BlockRestorer> PENDING = new ArrayList<>();
    private final SoundEffect blockRelativeSounds;

    private MovingBlockSpell(Builder builder) {
        super(builder.baseMeta);
        ListenerRegistry.addListener(this);
        this.material = builder.material;
        this.hitEffects = builder.hitEffects;
        this.blockRelativeSounds = builder.blockRelativeSounds;
        this.tagUnbreakable = UUID.randomUUID().toString();
        this.tagFallingBlock = UUID.randomUUID().toString();

        addStringProperty("Material", material);
    }

    public static void restorePendingBlocks() {
        MovingBlockSpell.PENDING.forEach(BlockRestorer::cancel);
    }

    public static Builder newBuilder(Material material) {
        return new Builder(material);
    }

    @Override
    public boolean cast(Player player) {
        return selectedBlock.containsKey(player) ? launchBlock(player) : prepareBlock(player);
    }

    private boolean prepareBlock(Player player) {
        Block block = player.getTargetBlock(10);
        if (block != null && block.getType() != Material.AIR) {
            if (!effectedBlocks.contains(block)) {
                BlockState oldState = block.getState();
                if (oldState instanceof Container) {
                    if (oldState instanceof Chest) {
                        ((Chest) oldState).getBlockInventory().clear();
                        Inventory inventory = ((Chest) oldState).getInventory();
                        if (inventory.getHolder() instanceof DoubleChest) {
                            player.sendActionBar("§ccan't be used on double chests");
                            return false;
                        } else inventory.clear();
                    } else {
                        ((Container) oldState).getInventory().clear();
                    }
                }
                block.setType(material, false);
                block.setMetadata(tagUnbreakable, Common.METADATA_VALUE_TRUE);
                block.getState().update();
                effectedBlocks.add(block);
                BlockRestorer blockRestorer = new BlockRestorer(oldState, player, this);
                selectedBlock.put(player, blockRestorer);
                Bukkit.getScheduler().runTaskLater(plugin, blockRestorer, 100L);
            } else {
                player.sendActionBar("§cblock is already taken");
            }
        } else {
            player.sendActionBar("§cselect a closer block");
        }
        return false;
    }

    private boolean launchBlock(Player player) {
        castSounds.play(player);
        BlockRestorer blockRestorer = selectedBlock.get(player);
        blockRestorer.earlyRun();
        Location location = blockRestorer.originLoc;
        FallingBlock fallingBlock = location.getWorld().spawnFallingBlock(location, Bukkit.createBlockData(material));
        fallingBlock.setVelocity(new Vector(0, 1, 0));
        fallingBlock.setMetadata(tagFallingBlock, Common.METADATA_VALUE_TRUE);
        fallingBlock.setDropItem(false);
        blockRelativeSounds.play(fallingBlock);
        caster.put(fallingBlock, player);
        trail(fallingBlock);
        Block block = player.getTargetBlock(30);
        if (block != null) {
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                if (fallingBlock.isValid()) {
                    fallingBlock.setVelocity(block
                            .getLocation()
                            .toCenterLocation()
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
        Player player = caster.get(fallingBlock);
        if (player != null) {
            caster.remove(fallingBlock);
            Location loc = fallingBlock.getLocation();
            hitEffects.accept(loc);
            applyEntityEffects(loc, player);
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

    private void trail(FallingBlock entity) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity.isValid()) {
                    spellRelativeEffects.accept(entity.getLocation());
                } else {
                    cancel();
                    blockLand(entity);
                }
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    public static class BlockRestorer implements Runnable {
        private final Location originLoc;
        private final MovingBlockSpell parent;
        private final Player player;
        private final BlockState state;
        private boolean canRun = true;

        private BlockRestorer(BlockState state, Player player, MovingBlockSpell parent) {
            this.state = state;
            this.originLoc = state.getLocation().toCenterLocation();
            this.parent = parent;
            this.player = player;
            new BukkitRunnable() {
                final World world = originLoc.getWorld();
                final BlockData blockData = parent.material.createBlockData();

                @Override
                public void run() {
                    if (canRun) {
                        world.spawnParticle(Particle.BLOCK_CRACK, originLoc, 10, 0.5, 0.5, 0.5, 0.15, blockData, true);
                    } else cancel();
                }
            }.runTaskTimer(plugin, 1, 1);
            PENDING.add(this);
        }

        void earlyRun() {
            run();
            canRun = false;
        }

        public void cancel() {
            state.update(true);
        }

        @Override
        public void run() {
            if (canRun) {
                canRun = false;
                effectedBlocks.remove(state.getBlock());
                state.removeMetadata(parent.tagUnbreakable, plugin);
                state.update(true);
                parent.selectedBlock.remove(player);
                PENDING.remove(this);
            }
        }
    }

    public static final class Builder extends AbstractBuilder<Builder> {

        private final Material material;
        private Consumer<Location> hitEffects = Common.emptyConsumer();
        private SoundEffect blockRelativeSounds = SoundEffect.EMPTY;

        private Builder(Material material) {
            this.material = material;
        }

        @Override
        Builder self() {
            return this;
        }

        @Override
        public Behaviour build() {
            return new MovingBlockSpell(this);
        }

        public Builder setHitEffects(Consumer<Location> hitEffects) {
            this.hitEffects = hitEffects;
            return this;
        }

        public Builder setBlockRelativeSounds(SoundEffect soundEffect) {
            this.blockRelativeSounds = soundEffect;
            return this;
        }
    }
}

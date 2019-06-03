package me.dylan.wands.spell.handler;

import me.dylan.wands.pluginmeta.ListenerRegistry;
import me.dylan.wands.util.DataUtil;
import me.dylan.wands.util.EffectUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Container;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public final class MovingBlock extends Behaviour implements Listener {

    private final Material material;
    private final Consumer<Location> hitEffects;
    private final Map<Player, BlockReverter> selectedBlock = new HashMap<>();
    private final Map<FallingBlock, Player> caster = new HashMap<>();

    private MovingBlock(Builder builder) {
        super(builder.baseMeta);
        ListenerRegistry.addListener(this);
        this.material = builder.material;
        this.hitEffects = builder.hitEffects;
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
            Location location = block.getLocation().toCenterLocation();
            BlockState oldState = block.getState();
            if (oldState instanceof Container) {
                if (oldState instanceof Chest) {
                    ((Chest) oldState).getBlockInventory().clear();
                } else {
                    ((Container) oldState).getInventory().clear();
                }
            }
            block.setType(material);
            block.setMetadata("unbreakable", new FixedMetadataValue(plugin, true));
            BlockReverter blockReverter = new BlockReverter(oldState, location, player, this);
            selectedBlock.put(player, blockReverter);
            Bukkit.getScheduler().runTaskLater(plugin, blockReverter, 100L);
        } else {
            player.sendActionBar("no blocks in range");
        }
        return false;
    }

    private boolean launchBlock(Player player) {
        castEffects.accept(player.getLocation());
        BlockReverter blockReverter = selectedBlock.get(player);
        blockReverter.earlyRun();
        Location location = blockReverter.originLoc;
        FallingBlock fallingBlock = location.getWorld().spawnFallingBlock(location, Bukkit.createBlockData(material));
        fallingBlock.setVelocity(new Vector(0, 1, 0));
        fallingBlock.setMetadata("isMovingBlockSpell", new FixedMetadataValue(plugin, player));
        fallingBlock.setDropItem(false);
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

    private void applyHitEffects(FallingBlock fallingBlock) {
        Player player = caster.get(fallingBlock);
        if (player != null) {
            caster.remove(fallingBlock);
            Location loc = fallingBlock.getLocation();
            hitEffects.accept(loc);
            EffectUtil.getNearbyDamageables(player, loc, effectAreaRange).forEach(entity -> {
                entityEffects.accept(entity);
                EffectUtil.damage(entityDamage, player, entity);
                entity.setVelocity(new Vector(0, 0, 0));
            });
        }
    }

    @EventHandler
    private void onBlockFall(EntityChangeBlockEvent event) {
        if ((event.getEntityType() == EntityType.FALLING_BLOCK) && event.getEntity().hasMetadata("isMovingBlockSpell")) {
            event.setCancelled(true);
            applyHitEffects((FallingBlock) event.getEntity());
        }
    }

    @EventHandler
    private void onBlockBreak(BlockBreakEvent event) {
        if (event.getBlock().hasMetadata("unbreakable")) event.setCancelled(true);
    }

    private void trail(FallingBlock entity) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity.isValid()) {
                    visualEffects.accept(entity.getLocation());
                } else {
                    cancel();
                    applyHitEffects(entity);
                }
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    static class BlockReverter implements Runnable {

        private final Location originLoc;
        private final MovingBlock parent;
        private final Player player;
        private BlockState state;
        private boolean canRun = true;

        BlockReverter(BlockState state, Location originLoc, Player player, MovingBlock parent) {
            this.state = state;
            this.originLoc = originLoc;
            this.parent = parent;
            this.player = player;
        }

        void earlyRun() {
            run();
            canRun = false;
        }

        @Override
        public void run() {
            if (canRun) {
                state.update(true);
                parent.selectedBlock.remove(player);
            }
        }
    }

    public static class Builder extends AbstractBuilder<Builder> {

        private final Material material;
        private Consumer<Location> hitEffects = DataUtil.emptyConsumer();

        private Builder(Material material) {
            this.material = material;
        }

        @Override
        Builder self() {
            return this;
        }

        @Override
        public Behaviour build() {
            return new MovingBlock(this);
        }

        public Builder setHitEffects(Consumer<Location> hitEffects) {
            this.hitEffects = hitEffects;
            return self();
        }
    }
}

package me.dylan.wands.spell.handler;

import me.dylan.wands.pluginmeta.ListenerRegistry;
import me.dylan.wands.util.DataUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.block.Container;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public final class MovingBlock extends Behaviour implements Listener {

    private final Material material;
    private final Consumer<Location> hitEffects;
    private final Map<Player, BlockReverter> selectedBlock = new HashMap<>();

    private MovingBlock(Builder builder) {
        super(builder.baseMeta);
        ListenerRegistry.addListener(this);
        this.material = builder.material;
        this.hitEffects = builder.hitEffects;
    }

    static class BlockReverter implements Runnable {

        private BlockState state;
        private final Location location;
        private boolean canContinue = true;
        private final MovingBlock parent;
        private final Player player;

        BlockReverter(BlockState state, Location location, Player player, MovingBlock parent) {
            this.state = state;
            this.location = location;
            this.parent = parent;
            this.player = player;
        }

        void earlyRun() {
            run();
            canContinue = false;
        }

        @Override
        public void run() {
            if (canContinue) {
                state.update(true);
                parent.selectedBlock.remove(player);
            }
        }
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
            BlockReverter blockReverter = new BlockReverter(oldState, location, player, this);
            selectedBlock.put(player, blockReverter);
            Bukkit.getScheduler().runTaskLater(plugin, blockReverter, 100L);
        } else {
            player.sendActionBar("no blocks in range");
        }
        return false;
    }

    private boolean launchBlock(Player player) {
        BlockReverter blockReverter = selectedBlock.get(player);
        blockReverter.earlyRun();
        Location location = blockReverter.location;
        FallingBlock fallingBlock = location.getWorld().spawnFallingBlock(location, Bukkit.createBlockData(material));
        fallingBlock.setVelocity(new Vector(0, 1, 0));
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

    public static Builder newBuilder(Material material) {
        return new Builder(material);
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

package me.dylan.wands;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MouseClickListeners implements Listener {
    private final List<LeftClickListener> leftClickListeners = new ArrayList<>();
    private final List<RightClickListener> rightClickListeners = new ArrayList<>();

    public void addLeftClickListener(@NotNull LeftClickListener listener) {
        leftClickListeners.add(listener);
    }

    public void addRightClickListener(@NotNull RightClickListener listener) {
        rightClickListeners.add(listener);
    }

    public <T extends LeftClickListener & RightClickListener> void addLeftAndRightClickListener(@NotNull T t) {
        leftClickListeners.add(t);
        rightClickListeners.add(t);
    }

    @EventHandler
    private void onPlayerInteract(@NotNull PlayerInteractEvent event) {
        Action action = event.getAction();
        Player player = event.getPlayer();
        GameMode gameMode = player.getGameMode();
        if (action == Action.PHYSICAL || gameMode == GameMode.SPECTATOR || event.getHand() == EquipmentSlot.OFF_HAND)
            return;
        if (gameMode != GameMode.ADVENTURE && (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK)) {
            ClickEvent clickEvent = new ClickEvent(player, event);
            leftClickListeners.forEach(listener -> listener.onLeftClick(clickEvent));
        } else if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
            ClickEvent clickEvent = new ClickEvent(player, event);
            rightClickListeners.forEach(listener -> listener.onRightClick(clickEvent));
        }
    }

    @EventHandler
    private void onLeftClick(@NotNull PlayerAnimationEvent event) {
        Player player = event.getPlayer();
        if (player.getGameMode() == GameMode.ADVENTURE) {
            ClickEvent clickEvent = new ClickEvent(player, event);
            leftClickListeners.forEach(listener -> listener.onLeftClick(clickEvent));
        }
    }

    @FunctionalInterface
    public interface LeftClickListener {
        void onLeftClick(ClickEvent event);
    }

    @FunctionalInterface
    public interface RightClickListener {
        void onRightClick(ClickEvent event);
    }

    public static final class ClickEvent {
        private final Player player;
        private final Cancellable cancellable;

        private ClickEvent(Player player, Cancellable cancellable) {
            this.player = player;
            this.cancellable = cancellable;
        }

        public Player getPlayer() {
            return player;
        }

        public void cancel() {
            this.cancellable.setCancelled(true);
        }
    }
}

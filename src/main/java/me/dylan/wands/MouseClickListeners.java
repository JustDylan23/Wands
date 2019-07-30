package me.dylan.wands;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MouseClickListeners implements Listener {
    private final List<@NotNull LeftClickListener> leftClickListeners = new ArrayList<>();
    private final List<@NotNull RightClickListener> rightClickListeners = new ArrayList<>();

    public void addLeftClickListener(LeftClickListener listener) {
        leftClickListeners.add(listener);
    }

    public void addRightClickListener(RightClickListener listener) {
        rightClickListeners.add(listener);
    }

    public <T extends LeftClickListener & RightClickListener> void addBoth(T t) {
        leftClickListeners.add(t);
        rightClickListeners.add(t);
    }

    public interface LeftClickListener {
        void onLeftClick(ClickEvent event);
    }

    public interface RightClickListener {
        void onRightClick(ClickEvent event);
    }

    public static class ClickEvent {
        private final Player player;
        private final Cancellable cancellable;

        @Contract(pure = true)
        private ClickEvent(Player player, Cancellable cancellable) {
            this.player = player;
            this.cancellable = cancellable;
        }

        public Player getPlayer() {
            return player;
        }

        public void setCancelled(boolean cancel) {
            this.cancellable.setCancelled(cancel);
        }
    }

    @EventHandler
    private void onPlayerInteract(@NotNull PlayerInteractEvent event) {
        Action action = event.getAction();
        Player player = event.getPlayer();
        GameMode gameMode = player.getGameMode();
        if (action == Action.PHYSICAL || gameMode == GameMode.SPECTATOR) return;
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
        if (event.getAnimationType() == PlayerAnimationType.ARM_SWING && player.getGameMode() == GameMode.ADVENTURE) {
            ClickEvent clickEvent = new ClickEvent(player, event);
            leftClickListeners.forEach(listener -> listener.onLeftClick(clickEvent));
        }
    }
}

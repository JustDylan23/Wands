package me.dylan.wands.spell;

import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import me.dylan.wands.Main;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

public enum CooldownManager implements Listener {
    INSTANCE;
    private final Object2LongMap<Player> map = new Object2LongOpenHashMap<>();

    CooldownManager() {
        map.defaultReturnValue(0);
    }

    /**
     * This method handles the cooldown which the player
     * has to wait for after casting a implementations before a new
     * implementations may be cast.
     *
     * @param player Player.
     */

    public boolean canCast(Player player) {
        long remainingTime = getRemainingTime(player);
        if (remainingTime <= 0) {
            return true;
        } else {
            sendRemainingTime(player, (int) Math.ceil(remainingTime / 1000D));
            return false;
        }
    }

    public void updateLastUsed(Player player) {
        map.put(player, System.currentTimeMillis());
    }

    /**
     * @param player Player.
     * @return Amount of time since player last tried to cast a @link #ba.
     */

    // todo optimize long values and method
    private int getRemainingTime(Player player) {
        int cooldown = Main.getPlugin().getConfigurableData().getMagicCooldownTime();
        if (cooldown == 0) return 0;
        long elapsed = System.currentTimeMillis() - map.getLong(player);
        return (int) (cooldown - elapsed);
    }

    private void sendRemainingTime(@NotNull Player player, int remaining) {
        player.sendActionBar("§6Wait §7" + remaining + " §6second" + ((remaining != 1) ? "s" : ""));
        player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_AMBIENT, 0.3F, 1);
    }

    /**
     * Clears player from cache on leave.
     *
     * @param event Gets called once the player disconnects to the server.
     */

    @EventHandler
    private void onQuit(PlayerQuitEvent event) {
        map.removeLong(event.getPlayer());
    }
}

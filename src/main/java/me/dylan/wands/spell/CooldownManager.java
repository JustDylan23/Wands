package me.dylan.wands.spell;

import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import me.dylan.wands.ListenerRegistry;
import me.dylan.wands.config.ConfigurableData;
import me.dylan.wands.utils.PlayerUtil;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

public class CooldownManager implements Listener {
    private final Object2LongMap<Player> map = new Object2LongOpenHashMap<>();
    private final ConfigurableData configurableData;

    public CooldownManager(ConfigurableData configurableData) {
        this.configurableData = configurableData;
        map.defaultReturnValue(0);
        ListenerRegistry.addListener(this);
    }

    /**
     * This method handles the cooldown which the player
     * has to wait for after casting a implementations before a new
     * implementations may be cast.
     *
     * @param player Player.
     */
    public boolean canCast(Player player) {
        int remainingTime = getRemainingTime(player);
        if (remainingTime <= 0) {
            return true;
        } else {
            sendRemainingTime(player, (remainingTime - 1) / 1000 + 1);
            return false;
        }
    }

    public void updateLastUsed(Player player) {
        map.put(player, System.currentTimeMillis());
    }

    /**
     * @param player Player.
     * @return Amount of time since last updated cooldown {@link #updateLastUsed(Player)}.
     */
    private int getRemainingTime(Player player) {
        int cooldown = configurableData.getMagicCooldownTime();
        long lastUsed = map.getLong(player);
        if (cooldown == 0 || lastUsed == 0) return 0;
        int elapsed = (int) (System.currentTimeMillis() - lastUsed);
        return cooldown - elapsed;
    }

    private void sendRemainingTime(@NotNull Player player, int remaining) {
        PlayerUtil.sendActionBar(player, "§6Wait§7 " + remaining + " §6second" + ((remaining != 1) ? "s" : ""));
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

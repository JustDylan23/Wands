package me.dylan.wands.spell;

import me.dylan.wands.ListenerRegistry;
import me.dylan.wands.config.ConfigHandler;
import me.dylan.wands.utils.PlayerUtil;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class CooldownManager implements Listener {
    private final Map<Player, Long> playerCooldownHashMap = new HashMap<>();
    private final ConfigHandler configHandler;

    public CooldownManager(ConfigHandler configHandler) {
        this.configHandler = configHandler;
        ListenerRegistry.addListener(this);
    }

    /**
     * This method handles the cooldown which the player has to wait for after casting a spell before a new spell may be cast.
     *
     * @param player    Player.
     * @param spellType SpellType.
     * @return Returns boolean based on whether the cooldown is active or not
     */
    public boolean canCast(Player player, SpellType spellType) {
        int remainingTime = getRemainingTime(player);
        if (remainingTime == 0) return true;
        remainingTime += spellType.behavior.getCooldown() * 1000;
        if (remainingTime <= 0) {
            return true;
        } else {
            sendRemainingTime(player, (remainingTime - 1) / 1000 + 1);
            return false;
        }
    }

    public void updateLastUsed(Player player) {
        playerCooldownHashMap.put(player, System.currentTimeMillis());
    }

    /**
     * @param player Player.
     * @return Amount of time since last updated cooldown {@link #updateLastUsed(Player)}.
     */
    private int getRemainingTime(Player player) {
        int cooldown = configHandler.getGlobalSpellCooldown() * 1000;
        Long lastUsed = playerCooldownHashMap.get(player);
        if (lastUsed != null) {
            if (cooldown == 0) return 0;
            int elapsed = (int) (System.currentTimeMillis() - lastUsed);

            return cooldown - elapsed;
        } else return 0;
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
        playerCooldownHashMap.remove(event.getPlayer());
    }
}

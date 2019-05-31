package me.dylan.wands.spell;

import me.dylan.wands.Main;
import me.dylan.wands.util.WandUtil;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public final class PlayerListener implements Listener {

    private static final Map<Player, Long> lastUsed = new HashMap<>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (action == Action.PHYSICAL) return;
        Player player = event.getPlayer();
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (event.getHand() == EquipmentSlot.HAND && WandUtil.isWand(itemStack)) {
            event.setCancelled(true);
            if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
                castWithCooldown(player, itemStack);
            } else if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
                WandUtil.nextSpell(player, itemStack);
            }
        }
    }

    /**
     * This method handles the cooldown which the player
     * has to wait for after casting a baseSpell before a new
     * baseSpell may be cast.
     *
     * @param player Player.
     * @param itemStack ItemStack which
     */

    private void castWithCooldown(Player player, ItemStack itemStack) {
        int remainingTime = getRemainingTime(player);
        if (remainingTime <= 0) {
            WandUtil.castSpell(player, itemStack);
            lastUsed.put(player, System.currentTimeMillis());
        } else {
            sendRemainingTime(player, remainingTime);
        }
    }

    /**
     * @param player Player.
     * @return Amount of time since player last tried to cast a baseSpell.
     */

    private int getRemainingTime(Player player) {
        int cooldown = Main.getPlugin().getConfigurableData().getMagicCooldownTime() * 1000;
        long now = System.currentTimeMillis();
        Long previous = lastUsed.get(player);
        if (previous == null) {
            return 0;
        }
        long elapsed = now - previous;
        return (int) Math.ceil(cooldown - elapsed) / 1000;
    }

    /**
     * Clears player from cache on leave.
     * @param event Gets called once the player disconnects to the server.
     */

    @EventHandler
    public static void onQuit(PlayerQuitEvent event) {
        lastUsed.remove(event.getPlayer());
    }

    private void sendRemainingTime(Player player, int remaining) {
        player.sendActionBar("§6Wait §7" + remaining + " §6second" + ((remaining != 1) ? "s" : ""));
        player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_AMBIENT, 0.3F, 1);
    }
}


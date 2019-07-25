package me.dylan.wands.spell;

import me.dylan.wands.Main;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.Statistic;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class PlayerListener implements Listener {

    private final Map<Player, Long> lastUsed = new HashMap<>();

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        EntityDamageEvent damageEvent = player.getLastDamageCause();
        Bukkit.getScheduler().runTaskLater(Main.getPlugin(), () -> player.spigot().respawn(), 20L);
        if (damageEvent != null && damageEvent.getCause() == DamageCause.CUSTOM) {
            if (player.hasMetadata("deathMessage")) {
                event.setDeathMessage(event.getEntity().getMetadata("deathMessage").get(0).asString());
            }
            Player killer = event.getEntity().getKiller();
            if (killer != null) {
                killer.incrementStatistic(Statistic.PLAYER_KILLS);
            }
        }
    }

    @EventHandler
    private void onLeftClick(PlayerAnimationEvent event) {
        Player player = event.getPlayer();
        if (event.getAnimationType() == PlayerAnimationType.ARM_SWING && player.getGameMode() == GameMode.ADVENTURE && player.hasPermission("wands.use")) {
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            if (SpellManagementUtil.isWand(itemStack)) {
                SpellManagementUtil.castSpell(player, itemStack);
            }
        }
    }

    @EventHandler
    private void onPlayerInteract(PlayerInteractEvent event) {
        Action action = event.getAction();
        Player player = event.getPlayer();
        GameMode gameMode = player.getGameMode();
        if (action == Action.PHYSICAL || gameMode == GameMode.SPECTATOR) return;
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (event.getHand() == EquipmentSlot.HAND && SpellManagementUtil.isWand(itemStack)) {
            event.setCancelled(true);
            if (gameMode != GameMode.ADVENTURE && (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK)) {
                castWithCooldown(player, itemStack);
            } else if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
                SpellManagementUtil.nextSpell(player, itemStack);
            }
        }
    }

    /**
     * This method handles the cooldown which the player
     * has to wait for after casting a implementations before a new
     * implementations may be cast.
     *
     * @param player    Player.
     * @param itemStack ItemStack which
     */

    private void castWithCooldown(Player player, ItemStack itemStack) {
        double remainingTime = getRemainingTime(player);
        if (remainingTime <= 0) {
            if (SpellManagementUtil.castSpell(player, itemStack)) {
                lastUsed.put(player, System.currentTimeMillis());
            }
        } else {
            sendRemainingTime(player, (int) Math.ceil(remainingTime));
        }
    }

    /**
     * @param player Player.
     * @return Amount of time since player last tried to cast a @link #ba.
     */

    private double getRemainingTime(Player player) {
        int cooldown = Main.getPlugin().getConfigurableData().getMagicCooldownTime() * 1000;
        long now = System.currentTimeMillis();
        Long previous = lastUsed.get(player);
        if (previous == null) {
            return 0;
        }
        long elapsed = now - previous;
        return (cooldown - elapsed) / 1000D;
    }

    /**
     * Clears player from cache on leave.
     *
     * @param event Gets called once the player disconnects to the server.
     */

    @EventHandler
    private void onQuit(PlayerQuitEvent event) {
        lastUsed.remove(event.getPlayer());
    }

    @EventHandler
    private void onBlockBreak(BlockBreakEvent event) {
        if (SpellManagementUtil.isWand(event.getPlayer().getInventory().getItemInMainHand())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onEntityDamageEntity(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        if (damager instanceof Player) {
            Player player = (Player) damager;
            if (SpellManagementUtil.isWand(player.getInventory().getItemInMainHand())) {
                event.setCancelled(true);
            }
        }
    }

    private void sendRemainingTime(Player player, int remaining) {
        player.sendActionBar("§6Wait §7" + remaining + " §6second" + ((remaining != 1) ? "s" : ""));
        player.playSound(player.getLocation(), Sound.ENTITY_BLAZE_AMBIENT, 0.3F, 1);
    }
}


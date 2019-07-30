package me.dylan.wands.spell;

import me.dylan.wands.Main;
import me.dylan.wands.MouseClickListeners.ClickEvent;
import me.dylan.wands.MouseClickListeners.LeftClickListener;
import me.dylan.wands.MouseClickListeners.RightClickListener;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Statistic;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class PlayerListener implements Listener, LeftClickListener, RightClickListener {

    public PlayerListener() {
        Main.getPlugin().getMouseClickListeners().addBoth(this);
    }

    @Override
    public void onLeftClick(@NotNull ClickEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (SpellManagementUtil.isWand(itemStack) && SpellManagementUtil.canUse(player)) {
            event.setCancelled(true);
            SpellManagementUtil.castSpell(player, itemStack);
        }
    }

    @Override
    public void onRightClick(@NotNull ClickEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (SpellManagementUtil.isWand(itemStack) && SpellManagementUtil.canUse(player)) {
            event.setCancelled(true);
            SpellManagementUtil.nextSpell(player, itemStack);
        }
    }

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
    private void onBlockBreak(BlockBreakEvent event) {
        if (SpellManagementUtil.isWand(event.getPlayer().getInventory().getItemInMainHand())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onEntityDamageEntity(EntityDamageByEntityEvent event) {
        Entity attacker = event.getDamager();
        if (attacker instanceof Player) {
            Player player = (Player) attacker;
            if (SpellManagementUtil.isWand(player.getInventory().getItemInMainHand())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void noUproot(PlayerInteractEvent event) {
        if (event.getAction() == Action.PHYSICAL) {
            Block block = event.getClickedBlock();
            if (block != null && block.getType() == Material.FARMLAND) {
                event.setCancelled(true);
            }
        }
    }
}

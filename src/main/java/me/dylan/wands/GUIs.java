package me.dylan.wands;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GUIs implements Listener {

    private String menuName = Wands.PREFIX + "menu";

    private static Inventory WandsGUI = Bukkit.createInventory(null, 27, new GUIs().menuName);

    static {
        ItemStack wandOption = new ItemStack(Material.BLAZE_ROD);
        ItemMeta meta = wandOption.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Available Wands"));
        wandOption.setItemMeta(meta);
        WandsGUI.setItem(13, wandOption);

        ItemStack status = new ItemStack(Material.GREEN_TERRACOTTA);
        meta = status.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Status: &aEnabled"));

        status.setItemMeta(meta);
        WandsGUI.setItem(16, status);
    }

    private void updateGUI(Player player) {
        ItemStack status;
        if (Wands.ACTIVE) {
            status = new ItemStack(Material.GREEN_TERRACOTTA);
            ItemMeta meta = status.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Status: &aEnabled"));
            status.setItemMeta(meta);
            WandsGUI.setItem(16, status);
        } else {
            status = new ItemStack(Material.RED_TERRACOTTA);
            ItemMeta meta = status.getItemMeta();
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&6Status: &eDisabled"));
            status.setItemMeta(meta);
            WandsGUI.setItem(16, status);
        }
        openGUI(player);
    }

    public static void openGUI(Player player) {
        player.openInventory(WandsGUI);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        if (inventory.equals(WandsGUI)) {
            event.setCancelled(true);
            Player player = (Player) event.getWhoClicked();
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.MASTER, 1F, 1F);
            if (event.getSlot() == 13) {
                //open wand menu
            } else if (event.getSlot() == 16) {
                Wands.ACTIVE = !Wands.ACTIVE;
                updateGUI(player);
            }

        }
    }
}

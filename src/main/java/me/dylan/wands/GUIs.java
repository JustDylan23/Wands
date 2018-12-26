package me.dylan.wands;

import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class GUIs implements Listener {

    private static Inventory MainGUI = Bukkit.createInventory(null, 27, Wands.PREFIX + "menu");

    static {
        AdvancedItemStack wandOption = new AdvancedItemStack(Material.BLAZE_ROD);
        wandOption.setName("&6Available Wands");
        MainGUI.setItem(13, wandOption);

        AdvancedItemStack status = new AdvancedItemStack(Material.GREEN_TERRACOTTA);
        status.setName("&6Status: &aEnabled");
        MainGUI.setItem(16, status);
    }

    private static Inventory WandsGUI = Bukkit.createInventory(null, 27, Wands.PREFIX + "wands");

    static {
        AdvancedItemStack empireWand = new AdvancedItemStack(Material.BLAZE_ROD);
        empireWand.setName("&cEmpire Wand");
        WandsGUI.setItem(10, empireWand);

        AdvancedItemStack therosDagger = new AdvancedItemStack(Material.MUSIC_DISC_MALL);
        therosDagger.setName("&8Theros Dagger");
        WandsGUI.setItem(13, therosDagger);
    }

    public static void openGUI(Player player) {
        player.openInventory(MainGUI);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) {
            event.getWhoClicked().closeInventory();
            return;
        }
        if (event.getClickedInventory().getName().equals("Inventory")) {
            return;
        }

        Inventory inventory = event.getInventory();
        Player player = (Player) event.getWhoClicked();
        int clickedSlot = event.getSlot();

        if (inventory.equals(MainGUI) || inventory.equals(WandsGUI)) {
            event.setCancelled(true);
            if (clickedSlot == -999) {
                player.closeInventory();
                return;
            } else if (event.getCurrentItem() != null) {
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.MASTER, 1F, 1F);
            }
        }

        if (inventory.equals(MainGUI)) {
            if (clickedSlot == 13) {
                player.openInventory(WandsGUI);
            } else if (clickedSlot == 16) {
                updateStatus(player);
            }

        } else if (inventory.equals(WandsGUI)) {
            player.getInventory().addItem(event.getCurrentItem());
        }
    }

    private void updateStatus(Player player) {
        Wands.ENABLED = !Wands.ENABLED;
        AdvancedItemStack status;
        if (Wands.ENABLED) {
            status = new AdvancedItemStack(Material.GREEN_TERRACOTTA);
            status.setName("&6Status: &aEnabled");
            MainGUI.setItem(16, status);
        } else {
            status = new AdvancedItemStack(Material.RED_TERRACOTTA);
            status.setName("&6Status: &cDisabled");
            MainGUI.setItem(16, status);
        }
    }
}

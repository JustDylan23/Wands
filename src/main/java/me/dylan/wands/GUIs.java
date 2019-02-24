package me.dylan.wands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public final class GUIs implements Listener {

    private static final Inventory mainGUI;

    static {
        mainGUI = Bukkit.createInventory(null, 27, Wands.PREFIX + "menu");

        ItemUtil wandOption = new ItemUtil(new ItemStack(Material.BLAZE_ROD));
        wandOption.setName("&6Available Wands");
        mainGUI.setItem(13, wandOption.getItemStack());

        ItemUtil status = new ItemUtil(new ItemStack(Material.GREEN_TERRACOTTA));
        status.setName("&6Status: &aEnabled");
        mainGUI.setItem(16, status.getItemStack());
    }

    private static final Inventory wandsGUI;

    static {
        wandsGUI = Bukkit.createInventory(null, 27, Wands.PREFIX + "wands");
        wandsGUI.setItem(10, MagicalItems.EMPIRE_WAND.getItemStack());
        wandsGUI.setItem(13, MagicalItems.THEROS_DAGGER.getItemStack());
        wandsGUI.setItem(16, MagicalItems.EMPIRE_BOW.getItemStack());
    }

    public static void openGUI(Player player) {
        player.openInventory(mainGUI);
    }

    @SuppressWarnings("deprecation")
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

        if (inventory.equals(mainGUI) || inventory.equals(wandsGUI)) {
            event.setCancelled(true);
            if (clickedSlot == -999) {
                player.closeInventory();
                return;
            } else if (event.getCurrentItem() != null) {
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.MASTER, 1F, 1F);
            }
        }

        if (inventory.equals(mainGUI)) {
            if (clickedSlot == 13) {
                player.openInventory(wandsGUI);
            } else if (clickedSlot == 16) {
                updateStatus(player);
            }

        } else if (inventory.equals(wandsGUI)) {
            player.getInventory().addItem(event.getCurrentItem());
        }
    }

    private void updateStatus(Player player) {
        Wands.getInstance().toggleStatus();
        ItemUtil status;
        if (Wands.getInstance().getStatus()) {
            status = new ItemUtil(new ItemStack(Material.GREEN_TERRACOTTA));
            status.setName("&6Status: &aEnabled");
            mainGUI.setItem(16, status.getItemStack());
        } else {
            status = new ItemUtil(new ItemStack(Material.RED_TERRACOTTA));
            status.setName("&6Status: &cDisabled");
            mainGUI.setItem(16, status.getItemStack());
        }
    }
}

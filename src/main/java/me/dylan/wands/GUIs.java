package me.dylan.wands;

import net.minecraft.server.v1_13_R2.NBTTagInt;
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

        ItemBuilder wandOption = new ItemBuilder(new ItemStack(Material.BLAZE_ROD));
        wandOption.setName("&6Available Wands");
        mainGUI.setItem(13, wandOption);

        ItemBuilder status = new ItemBuilder(new ItemStack(Material.GREEN_TERRACOTTA));
        status.setName("&6Status: &aEnabled");
        mainGUI.setItem(16, status);
    }

    private static final Inventory wandsGUI;

    static {
        wandsGUI = Bukkit.createInventory(null, 27, Wands.PREFIX + "wands");

        WandItem empireWand = new WandItem(new ItemStack(Material.BLAZE_ROD));
        empireWand.setName("&cEmpire Wand");
        empireWand.markAsWand().setSpells(1, 2, 3);
        wandsGUI.setItem(10, empireWand);

        ItemBuilder therosDagger = new ItemBuilder(new ItemStack(Material.MUSIC_DISC_MALL));
        therosDagger.setNbtTag("therosdagger", new NBTTagInt(1));
        therosDagger.setName("&8Theros Dagger");
        wandsGUI.setItem(13, therosDagger);
    }

    public static void openGUI(Player player) {
        player.openInventory(mainGUI);
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
        Wands.ENABLED = !Wands.ENABLED;
        ItemBuilder status;
        if (Wands.ENABLED) {
            status = new ItemBuilder(new ItemStack(Material.GREEN_TERRACOTTA));
            status.setName("&6Status: &aEnabled");
            mainGUI.setItem(16, status);
        } else {
            status = new ItemBuilder(new ItemStack(Material.RED_TERRACOTTA));
            status.setName("&6Status: &cDisabled");
            mainGUI.setItem(16, status);
        }
    }
}

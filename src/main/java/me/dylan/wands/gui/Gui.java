package me.dylan.wands.gui;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import java.util.Map;

public class Gui implements Listener {
    private final Inventory inventory;
    private final Map<Integer, ClickableItem<?>> inventoryMap;

    public Gui(InventoryType inventoryType, Map<Integer, ClickableItem<?>> inventoryMap, String title) {
        this.inventoryMap = inventoryMap;
        this.inventory = Bukkit.createInventory(null, inventoryType, title);
        setItems(inventoryMap);
    }

    public void setItems(Map<Integer, ClickableItem<?>> inventoryMap) {
        inventoryMap.forEach(
                (index, clickableItem) -> inventory.setItem(index, clickableItem.getItemStack())
        );
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!inventory.equals(event.getInventory()))
            return;
        event.setCancelled(true);

        ClickableItem<?> clickableItem = inventoryMap.get(event.getSlot());
        if (clickableItem != null) {
            clickableItem.callEvent(event);
        }
    }

    public void show(Player player) {
        player.openInventory(inventory);
    }
}

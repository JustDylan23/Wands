package me.dylan.wands.gui;

import me.dylan.wands.spell.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class ToggleableItem extends ClickableItem<Boolean> {

    private final ItemStack itemEnabled;
    private final ItemStack itemDisabled;

    private boolean enabled;

    public ToggleableItem(boolean enabled, String displayName) {
        this.enabled = enabled;

        this.itemEnabled = ItemBuilder
                .from(Material.GREEN_TERRACOTTA)
                .named(displayName + "&r - &aenabled")
                .withLore("Click to disable")
                .build();

        this.itemDisabled = ItemBuilder
                .from(Material.RED_TERRACOTTA)
                .named(displayName + "&r - &cisalbed")
                .withLore("Click to enable")
                .build();
    }

    @Override
    public void callEvent(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        int slot = event.getSlot();

        enabled = itemEnabled.equals(inventory.getItem(slot));
        submit(enabled);

        if (enabled) {
            inventory.setItem(slot, itemDisabled);
        } else {
            inventory.setItem(slot, itemEnabled);
        }
    }

    @Override
    public ItemStack getItemStack() {
        return enabled ? itemEnabled : itemDisabled;
    }
}

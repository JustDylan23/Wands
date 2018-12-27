package me.dylan.wands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class AdvancedItemStack extends ItemStack {

    public AdvancedItemStack(Material material) {
        super(material);
    }

    public AdvancedItemStack(Material material, String name) {
        super(material);
        this.setName(name);
    }

    public final void setName(String name) {
        ItemMeta meta = getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        setItemMeta(meta);
    }

    public final void setLore(String... lore) {
        ItemMeta meta = getItemMeta();
        meta.setLore(Arrays.asList(lore));
        setItemMeta(meta);
    }
}

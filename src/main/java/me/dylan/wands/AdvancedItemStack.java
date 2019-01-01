package me.dylan.wands;

import net.minecraft.server.v1_13_R2.NBTTagCompound;
import net.minecraft.server.v1_13_R2.NBTTagInt;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class AdvancedItemStack extends ItemStack {

    public AdvancedItemStack(ItemStack itemStack) {
        super(itemStack);
    }

    public AdvancedItemStack(Material material) {
        super(material);
    }

    public AdvancedItemStack(Material material, String name) {
        super(material);
        this.setName(name);
    }

    public AdvancedItemStack setName(String name) {
        ItemMeta meta = getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        setItemMeta(meta);
        return this;
    }

    public AdvancedItemStack setLore(String... lore) {
        ItemMeta meta = getItemMeta();
        meta.setLore(Arrays.asList(lore));
        setItemMeta(meta);
        return this;
    }

    public AdvancedItemStack setNBTTag(String key, int value) {
        NBTTagCompound compound = getNBTTagCompound();
        compound.set(key, new NBTTagInt(value));
        setCompound(compound);
        return this;
    }

    public AdvancedItemStack setNBTTagIntArray(String key, int... values) {
        NBTTagCompound compound = getNBTTagCompound();
        compound.setIntArray(key, values);
        setCompound(compound);
        return this;
    }

    public AdvancedItemStack removeNBTTag(String key) {
        NBTTagCompound compound = getNBTTagCompound();
        if (compound.hasKey(key)) {
            compound.remove(key);
        }
        return this;
    }

    public Integer getNBTTagInt(String key, Integer defaultValue) {
        NBTTagCompound compound = getNBTTagCompound();
        return compound.hasKey(key) ? compound.getInt(key) : defaultValue;
    }

    public int[] getNBTTagIntArray(String key) {
        NBTTagCompound compound = getNBTTagCompound();
        return compound.hasKey(key) ? compound.getIntArray(key) : new int[0];
    }

    public boolean hasNBTTag(String key) {
        NBTTagCompound compound = getNBTTagCompound();
        return compound.hasKey(key);
    }

    private NBTTagCompound getNBTTagCompound() {
        net.minecraft.server.v1_13_R2.ItemStack nmsItem = CraftItemStack.asNMSCopy(this);
        return nmsItem.getTag() != null ? nmsItem.getTag() : new NBTTagCompound();
    }

    private void setCompound(NBTTagCompound compound) {
        net.minecraft.server.v1_13_R2.ItemStack nmsItem = CraftItemStack.asNMSCopy(this);
        nmsItem.setTag(compound);
        ItemMeta meta = CraftItemStack.getItemMeta(nmsItem);
        this.setItemMeta(meta);
    }
}

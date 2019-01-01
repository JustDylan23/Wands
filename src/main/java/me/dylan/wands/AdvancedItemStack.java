package me.dylan.wands;

import net.minecraft.server.v1_13_R2.NBTBase;
import net.minecraft.server.v1_13_R2.NBTTagCompound;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.function.Consumer;

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

    private void applyNBT(Consumer<NBTTagCompound> consumer) {
        net.minecraft.server.v1_13_R2.ItemStack nmsItem = CraftItemStack.asNMSCopy(this);
        NBTTagCompound compound = nmsItem.getTag() != null ? nmsItem.getTag() : new NBTTagCompound();
        consumer.accept(compound);
        nmsItem.setTag(compound);
        ItemMeta meta = CraftItemStack.getItemMeta(nmsItem);
        this.setItemMeta(meta);
    }

    public AdvancedItemStack setNBTTag(String key, NBTBase nbtBase) {
        applyNBT(tag -> tag.set(key, nbtBase));
        return this;
    }

    public AdvancedItemStack setNBTTag(String key, int value) {
        applyNBT(tag -> tag.setInt(key, value));
        return this;
    }

    public AdvancedItemStack setNBTTagIntArray(String key, int... values) {
        applyNBT(tag -> tag.setIntArray(key, values));
        return this;
    }

    public AdvancedItemStack removeNBTTag(String key) {
        if (hasNBTTag(key)) {
            applyNBT(tag -> tag.remove(key));
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
}

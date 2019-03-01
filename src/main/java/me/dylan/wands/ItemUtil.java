package me.dylan.wands;

import net.minecraft.server.v1_13_R2.NBTBase;
import net.minecraft.server.v1_13_R2.NBTTagCompound;
import net.minecraft.server.v1_13_R2.NBTTagInt;
import net.minecraft.server.v1_13_R2.NBTTagIntArray;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;

@SuppressWarnings({"WeakerAccess", "RedundantSuppression"})
public class ItemUtil {

    private final ItemStack itemStack;

    public ItemUtil(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemUtil(Material material) {
        this(new ItemStack(material));
    }

    public ItemUtil builder(Consumer<ItemUtil> consumer) {
        consumer.accept(this);
        return this;
    }

    public void setItemMeta(Consumer<ItemMeta> consumer) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        consumer.accept(itemMeta);
        itemStack.setItemMeta(itemMeta);
    }

    private void modifyNbt(Consumer<NBTTagCompound> consumer) {
        net.minecraft.server.v1_13_R2.ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound compound = nmsItem.getTag() != null ? nmsItem.getTag() : new NBTTagCompound();
        consumer.accept(compound);
        nmsItem.setTag(compound);
        ItemMeta meta = CraftItemStack.getItemMeta(nmsItem);
        itemStack.setItemMeta(meta);
    }

    public void setName(String name) {
        setItemMeta(meta -> meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name)));
    }

    public void setLore(String... lore) {
        setItemMeta(meta -> meta.setLore(Arrays.asList(lore)));
    }

    public void setNbtTag(String key, @Nonnull NBTBase nbtBase) {
        modifyNbt(tag -> tag.set(key, nbtBase));
    }

    public void setNbtTagInt(String key, int i) {
        setNbtTag(key, new NBTTagInt(i));
    }

    public void setNbtTagIntArray(String key, int... i) {
        setNbtTag(key, new NBTTagIntArray(i));
    }

    public <T> T getNbtTag(Function<NBTTagCompound, T> function) {
        net.minecraft.server.v1_13_R2.ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound compound = nmsItem.getTag() != null ? nmsItem.getTag() : new NBTTagCompound();
        return function.apply(compound);
    }

    public void removeNbtTag(String key) {
        modifyNbt(tag -> tag.remove(key));
    }

    public boolean hasNbtTag(String key) {
        return getNbtTag(tag -> tag.hasKey(key));
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}

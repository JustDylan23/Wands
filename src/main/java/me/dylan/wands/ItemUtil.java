package me.dylan.wands;

import me.ialistannen.mininbt.ItemNBTUtil;
import me.ialistannen.mininbt.NBTWrappers;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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

    private void modifyNbt(Consumer<NBTWrappers.NBTTagCompound> consumer) {
        NBTWrappers.NBTTagCompound compound = ItemNBTUtil.getTag(itemStack);
        consumer.accept(compound);

        ItemMeta meta = ItemNBTUtil.setNBTTag(compound, itemStack).getItemMeta();
        itemStack.setItemMeta(meta);
    }

    public void setName(String name) {
        setItemMeta(meta -> meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name)));
    }

    public void setLore(String... lore) {
        setItemMeta(meta -> meta.setLore(Arrays.asList(lore)));
    }

    public void setNbtTagInt(String key, int i) {
        modifyNbt(tag -> tag.setInt(key, i));
    }

    public void setNbtTagIntArray(String key, int... i) {
        modifyNbt(tag -> tag.setIntArray(key, i));
    }

    public <T> T getNbtTag(Function<NBTWrappers.NBTTagCompound, T> function) {
        return function.apply(ItemNBTUtil.getTag(itemStack));
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

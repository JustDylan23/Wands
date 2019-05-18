package me.dylan.wands.wrappers;

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
public class ItemWrapper {

    protected final ItemStack itemStack;

    public ItemWrapper(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemWrapper(Material material) {
        this(new ItemStack(material));
    }

    public ItemWrapper setItemMeta(Consumer<ItemMeta> consumer) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        consumer.accept(itemMeta);
        itemStack.setItemMeta(itemMeta);
        return this;
    }

    private void modifyNbt(Consumer<NBTWrappers.NBTTagCompound> consumer) {
        NBTWrappers.NBTTagCompound compound = ItemNBTUtil.getTag(itemStack);
        consumer.accept(compound);

        ItemMeta meta = ItemNBTUtil.setNBTTag(compound, itemStack).getItemMeta();
        itemStack.setItemMeta(meta);
    }

    public ItemWrapper setName(String name) {
        setItemMeta(meta -> meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name)));
        return this;
    }

    public ItemWrapper setLore(String... lore) {
        setItemMeta(meta -> meta.setLore(Arrays.asList(lore)));
        return this;
    }

    public ItemWrapper setNbtTagInt(String key, int i) {
        modifyNbt(tag -> tag.setInt(key, i));
        return this;
    }

    public void setNbtTagIntArray(String key, int... i) {
        modifyNbt(tag -> tag.setIntArray(key, i));
    }

    public <T> T getNbtTag(Function<NBTWrappers.NBTTagCompound, T> function) {
        return function.apply(ItemNBTUtil.getTag(itemStack));
    }

    public ItemWrapper removeNbtTag(String key) {
        modifyNbt(tag -> tag.remove(key));
        return this;
    }

    public boolean hasNbtTag(String key) {
        return getNbtTag(tag -> tag.hasKey(key));
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}

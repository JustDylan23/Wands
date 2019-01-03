package me.dylan.wands;

import net.minecraft.server.v1_13_R2.NBTBase;
import net.minecraft.server.v1_13_R2.NBTTagCompound;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Dylan
 */

public class ItemBuilder extends ItemStack {

    private ItemStack itemStack;

    public ItemBuilder(ItemStack itemStack) {
        super(itemStack);
        this.itemStack = itemStack;
    }

    /**
     * This method is for renaming the itemStack.
     *
     * @param name The new name of the itemStack.
     * @return Returns an instance of itself.
     */

    public ItemBuilder setName(String name) {
        ItemMeta meta = getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        setItemMeta(meta);
        return this;
    }

    /**
     * This method is for setting the lore of the itemStack.
     *
     * @param lore The new lore of the itemStack.
     * @return Returns an instance of itself.
     */

    public ItemBuilder setLore(String... lore) {
        ItemMeta meta = getItemMeta();
        meta.setLore(Arrays.asList(lore));
        setItemMeta(meta);
        return this;
    }

    /**
     * This method is for setting nbt with a key and value.
     *
     * @param key NBT key.
     * @param nbtBase The value of the key.
     * @return Returns an instance of itself.
     *
     * Example of usage:
     * @<code>
     *     instance.setNbtTag("key", new NBTTagInt(i))
     * </code>
     */

    public ItemBuilder setNbtTag(@Nonnull String key, @Nonnull NBTBase nbtBase) {
        modifyNbt(tag -> tag.set(key, nbtBase));
        return this;
    }

    /**
     * This method gives the user access to all the getter methods of the
     * NBTTagCompound and returns the return value of that method.
     *
     * @return Returns value requested in the converter parameter.
     *
     * Example of usage:
     * @<code>
     *     instance.getTag(tag -> tag.getInt("key"));
     * </code>
     */

    public <T> T getNbtTag(Function<NBTTagCompound, T> converter) {
        net.minecraft.server.v1_13_R2.ItemStack nmsItem = CraftItemStack.asNMSCopy(this);
        NBTTagCompound compound = nmsItem.getTag() != null ? nmsItem.getTag() : new NBTTagCompound();
        return converter.apply(compound);
    }

    /**
     * This method removes a key from the NBTTagCompound.
     *
     * @see NBTTagCompound
     *
     * @param key The key that will be removed from the NBTTagCompound.
     * @return Returns an instance of itself.
     *
     */

    public ItemBuilder removeNbtTag(String key) {
        if (hasNbtTag(key)) {
            modifyNbt(tag -> tag.remove(key));
        }
        return this;
    }

    /**
     * This method checks if the NBTTagCompound contains a key.
     *
     * @see NBTTagCompound
     *
     * @param key The key for the NBTTagCompound
     * @return Returns a boolean based on whether the NBTTagCompound contains
     * the key or not.
     */

    public boolean hasNbtTag(String key) {
        return getNbtTag(tag -> tag.hasKey(key));
    }

    /**
     * This method is for modifying the NBTTagCompound, the Consumer as
     * parameter is supposed to give the user a lot of freedom of which
     * setter method he/she will use.
     *
     * @param consumer Consumer interface for modifying the NBTTagCompound
     *
     * Example of usage:
     * @<code>
     *     instance.modifyNbt(tag -> tag.setInt(key, value));
     * </code>
     *
     */

    private void modifyNbt(@Nonnull Consumer<NBTTagCompound> consumer) {
        net.minecraft.server.v1_13_R2.ItemStack nmsItem = CraftItemStack.asNMSCopy(this);
        NBTTagCompound compound = nmsItem.getTag() != null ? nmsItem.getTag() : new NBTTagCompound();
        consumer.accept(compound);
        nmsItem.setTag(compound);
        ItemMeta meta = CraftItemStack.getItemMeta(nmsItem);
        this.setItemMeta(meta);
    }

    /**
     * This method applies all the changes made in to object to the ItemStack
     * that was used in the constructor.
     */

    public void applyChanges() {
        itemStack.setItemMeta(getItemMeta());
    }
}

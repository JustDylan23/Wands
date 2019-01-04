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
 * Utils for directly modifying {@link ItemStack}
 *
 * @author Dylan
 * @since BETA-1.0
 */

public class ItemUtil {

    private ItemStack itemStack;

    /**
     * Creates ItemUtil for {@link ItemStack} in the parameter.
     *
     * @param itemStack {@link ItemStack}
     */

    public ItemUtil(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    /**
     * Sets name of {@link ItemStack}
     *
     * @param name Name of {@link ItemStack}
     * @return Instance of itself.
     */

    public ItemUtil setName(String name) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        itemStack.setItemMeta(meta);
        return this;
    }

    /**
     * Sets lore of {@link ItemStack}
     *
     * @param lore Lore of {@link ItemStack}
     * @return Instance of itself.
     */

    public ItemUtil setLore(String... lore) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.setLore(Arrays.asList(lore));
        itemStack.setItemMeta(meta);
        return this;
    }

    /*
     * This method is for modifying the NBTTagCompound, the Consumer as
     * parameter is supposed to give the user a lot of freedom of which
     * setter method he/she will use.
     */

    private void modifyNbt(Consumer<NBTTagCompound> consumer) {
        net.minecraft.server.v1_13_R2.ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound compound = nmsItem.getTag() != null ? nmsItem.getTag() : new NBTTagCompound();
        consumer.accept(compound);
        nmsItem.setTag(compound);
        ItemMeta meta = CraftItemStack.getItemMeta(nmsItem);
        itemStack.setItemMeta(meta);
    }

    /**
     * <p>Sets any NBT tag.
     *
     * <p><i>Example of usage:</i><br>
     * {@code
     * instance.setNbtTag("key", new NBTTagInt(i));}
     *
     * @param key     The key the {@link NBTBase} will be stored under.
     * @param nbtBase The {@link NBTBase} that will be stored under key.
     * @return Instance of itself
     */

    public ItemUtil setNbtTag(@Nonnull String key, @Nonnull NBTBase nbtBase) {
        modifyNbt(tag -> tag.set(key, nbtBase));
        return this;
    }

    /**
     * <p>Allows user to use any getter method from {@link NBTTagCompound}.
     *
     * <p><i>Example of usage: </i><br>
     * {@code instance.getTag(tag -> tag.getInt("key"));}
     *
     * @param function Function for getter method.
     * @param <T>      Type of the result of the method in function.
     * @return Result of getter method in function.
     */

    public <T> T getNbtTag(Function<NBTTagCompound, T> function) {
        net.minecraft.server.v1_13_R2.ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound compound = nmsItem.getTag() != null ? nmsItem.getTag() : new NBTTagCompound();
        return function.apply(compound);
    }

    public ItemUtil removeNbtTag(String key) {
        if (hasNbtTag(key)) {
            modifyNbt(tag -> tag.remove(key));
        }
        return this;
    }

    public boolean hasNbtTag(String key) {
        return getNbtTag(tag -> tag.hasKey(key));
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}

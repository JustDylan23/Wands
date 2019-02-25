package me.dylan.wands;

import net.minecraft.server.v1_13_R2.NBTBase;
import net.minecraft.server.v1_13_R2.NBTTagCompound;
import net.minecraft.server.v1_13_R2.NBTTagInt;
import net.minecraft.server.v1_13_R2.NBTTagIntArray;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_13_R2.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
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
 * @since BETA-1.0.0
 */
@SuppressWarnings({"WeakerAccess", "UnusedReturnValue", "RedundantSuppression"})
public class ItemUtil<T extends ItemUtil> {

    private final ItemStack itemStack;

    /**
     * Creates ItemUtil for {@link ItemStack} in the parameter.
     *
     * @param itemStack {@link ItemStack}
     */

    public ItemUtil(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    @SuppressWarnings("unchecked")
    public T getInstance() {
        return (T) this;
    }

    public T setItemMeta(Consumer<ItemMeta> consumer) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        consumer.accept(itemMeta);
        itemStack.setItemMeta(itemMeta);
        return getInstance();
    }

    public T setName(String name) {
        setItemMeta(meta -> meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name)));
        return getInstance();
    }

    public T setLore(String... lore) {
        setItemMeta(meta -> meta.setLore(Arrays.asList(lore)));
        return getInstance();
    }

    public T addEnchant(Enchantment enchantment, int level) {
        itemStack.addEnchantment(enchantment, level);
        return getInstance();
    }

    private void modifyNbt(Consumer<NBTTagCompound> consumer) {
        net.minecraft.server.v1_13_R2.ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound compound = nmsItem.getTag() != null ? nmsItem.getTag() : new NBTTagCompound();
        consumer.accept(compound);
        nmsItem.setTag(compound);
        ItemMeta meta = CraftItemStack.getItemMeta(nmsItem);
        itemStack.setItemMeta(meta);
    }

    /**
     * Sets any NBT tag.
     *
     * Example of usage:
     * {@code
     * instance.setNbtTag("key", new NBTTagInt(i));}
     *
     * @param key     The key the {@link NBTBase} will be stored under.
     * @param nbtBase The {@link NBTBase} that will be stored under key.
     * @return Instance of itself
     */

    public T setNbtTag(String key, @Nonnull NBTBase nbtBase) {
        modifyNbt(tag -> tag.set(key, nbtBase));
        return getInstance();
    }

    public T setNbtTagInt(String key, int i) {
        modifyNbt(tag -> tag.set(key, new NBTTagInt(i)));
        return getInstance();
    }

    public T setNbtTagIntArray(String key, int... i) {
        modifyNbt(tag -> tag.set(key, new NBTTagIntArray(i)));
        return getInstance();
    }

    /**
     * Allows user to use any getter method from {@link NBTTagCompound}.
     *
     * Example of usage:
     * {@code instance.getTag(tag -> tag.getInt("key"));}
     *
     * @param function Function for getter method.
     * @param <S>      Type of the result of the method in function.
     * @return Result of getter method in function.
     */

    public <S> S getNbtTag(Function<NBTTagCompound, S> function) {
        net.minecraft.server.v1_13_R2.ItemStack nmsItem = CraftItemStack.asNMSCopy(itemStack);
        NBTTagCompound compound = nmsItem.getTag() != null ? nmsItem.getTag() : new NBTTagCompound();
        return function.apply(compound);
    }

    public T removeNbtTag(String key) {
        if (hasNbtTag(key)) {
            modifyNbt(tag -> tag.remove(key));
        }
        return getInstance();
    }

    public boolean hasNbtTag(String key) {
        return getNbtTag(tag -> tag.hasKey(key));
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}

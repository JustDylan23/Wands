package me.dylan.wands.util;

import me.dylan.wands.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;

@SuppressWarnings({"WeakerAccess", "unused"})
public class ItemUtil {
    private static final Main plugin = Main.getPlugin();

    private ItemUtil() {
        throw new UnsupportedOperationException();
    }

    public static ItemStack getItemStack(Supplier<ItemStack> stackSupplier) {
        return stackSupplier.get();
    }

    public static void setItemMeta(ItemStack itemStack, Consumer<ItemMeta> consumer) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        consumer.accept(itemMeta);
        itemStack.setItemMeta(itemMeta);
    }

    public static void setName(ItemStack itemStack, String name) {
        setItemMeta(itemStack, meta -> meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name)));
    }

    public static void setLore(ItemStack itemStack, String... lore) {
        setItemMeta(itemStack, meta -> meta.setLore(Arrays.asList(lore)));
    }

    public static <T> void setPersistentData(ItemStack itemStack, String key, PersistentDataType<T, T> type, T t) {
        ItemMeta meta = itemStack.getItemMeta();
        if (itemStack.getType() == Material.AIR) {
            return;
        }
        PersistentDataContainer container = meta.getPersistentDataContainer();
        NamespacedKey namespacedKey = new NamespacedKey(plugin, key);

        container.set(namespacedKey, type, t);
        itemStack.setItemMeta(meta);
    }

    public static <T> Optional<T> getPersistentData(ItemStack itemStack, String key, PersistentDataType<T, T> type) {
        ItemMeta meta = itemStack.getItemMeta();
        if (itemStack.getType() == Material.AIR) {
            return Optional.empty();
        }
        PersistentDataContainer container = meta.getPersistentDataContainer();
        NamespacedKey namespacedKey = new NamespacedKey(plugin, key);

        return container.has(namespacedKey, type) ? Optional.ofNullable(container.get(namespacedKey, type)) : Optional.empty();
    }

    public static <T> boolean hasPersistentData(ItemStack itemStack, String key, PersistentDataType<T, T> type) {
        ItemMeta meta = itemStack.getItemMeta();
        if (itemStack.getType() == Material.AIR) {
            return false;
        }
        PersistentDataContainer container = meta.getPersistentDataContainer();
        NamespacedKey namespacedKey = new NamespacedKey(plugin, key);

        return container.has(namespacedKey, type);
    }

    public static void removePersistentData(ItemStack itemStack, String key) {
        ItemMeta meta = itemStack.getItemMeta();
        if (itemStack.getType() == Material.AIR) {
            return;
        }
        PersistentDataContainer container = meta.getPersistentDataContainer();
        NamespacedKey namespacedKey = new NamespacedKey(plugin, key);

        container.remove(namespacedKey);
        itemStack.setItemMeta(meta);
    }
}

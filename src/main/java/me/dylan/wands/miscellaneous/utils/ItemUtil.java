package me.dylan.wands.miscellaneous.utils;

import me.dylan.wands.Main;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Consumer;

public final class ItemUtil {
    private static final Main plugin = Main.getPlugin();

    private ItemUtil() {
        throw new UnsupportedOperationException("Instantiating util class");
    }

    public static void setName(@NotNull ItemStack itemStack, String name) {
        setItemMeta(itemStack, meta -> meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name)));
    }

    public static @NotNull String getName(@NotNull ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        return (itemMeta == null) ? "" : itemMeta.getDisplayName();
    }

    public static void setItemMeta(@NotNull ItemStack itemStack, @NotNull Consumer<ItemMeta> consumer) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        consumer.accept(itemMeta);
        itemStack.setItemMeta(itemMeta);
    }

    public static <T> void setPersistentData(@NotNull ItemStack itemStack, @NotNull String key, @NotNull PersistentDataType<T, T> type, T t) {
        ItemMeta meta = itemStack.getItemMeta();
        if (itemStack.getType() == Material.AIR) {
            return;
        }
        PersistentDataContainer container = meta.getPersistentDataContainer();
        NamespacedKey namespacedKey = new NamespacedKey(plugin, key);

        container.set(namespacedKey, type, t);
        itemStack.setItemMeta(meta);
    }

    public static <T> Optional<T> getPersistentData(@NotNull ItemStack itemStack, @NotNull String key, @NotNull PersistentDataType<T, T> type) {
        ItemMeta meta = itemStack.getItemMeta();
        if (itemStack.getType() == Material.AIR) {
            return Optional.empty();
        }
        PersistentDataContainer container = meta.getPersistentDataContainer();
        NamespacedKey namespacedKey = new NamespacedKey(plugin, key);

        return container.has(namespacedKey, type) ? Optional.ofNullable(container.get(namespacedKey, type)) : Optional.empty();
    }

    public static <T> boolean hasPersistentData(@NotNull ItemStack itemStack, @NotNull String key, @NotNull PersistentDataType<T, T> type) {
        ItemMeta meta = itemStack.getItemMeta();
        if (itemStack.getType() == Material.AIR) {
            return false;
        }
        PersistentDataContainer container = meta.getPersistentDataContainer();
        NamespacedKey namespacedKey = new NamespacedKey(plugin, key);

        return container.has(namespacedKey, type);
    }

    public static void removePersistentData(@NotNull ItemStack itemStack, @NotNull String key) {
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

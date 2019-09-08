package me.dylan.wands.utils;

import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Consumer;

public final class ItemUtil {
    private ItemUtil() {
        throw new UnsupportedOperationException("Instantiating util class");
    }

    public static void setName(@NotNull ItemStack itemStack, String name) {
        setItemMeta(itemStack, meta -> meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name)));
    }

    public static @NotNull String getName(@NotNull ItemStack itemStack) {
        ItemMeta itemMeta = itemStack.getItemMeta();
        return itemMeta != null ? itemMeta.getDisplayName() : "";
    }

    public static void setItemMeta(@NotNull ItemStack itemStack, @NotNull Consumer<ItemMeta> consumer) {
        if (!itemStack.hasItemMeta()) {
            return;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        consumer.accept(itemMeta);
        itemStack.setItemMeta(itemMeta);
    }

    public static <T> void setPersistentData(@NotNull ItemStack itemStack, @NotNull String key, @NotNull PersistentDataType<T, T> type, T t) {
        setItemMeta(itemStack, meta -> meta.getPersistentDataContainer().set(Common.newNamespacedKey(key), type, t));
    }

    public static <T> Optional<T> getPersistentData(@NotNull ItemStack itemStack, @NotNull String key, @NotNull PersistentDataType<T, T> type) {
        if (!itemStack.hasItemMeta()) {
            return Optional.empty();
        }
        PersistentDataContainer container = itemStack.getItemMeta().getPersistentDataContainer();
        NamespacedKey namespacedKey = Common.newNamespacedKey(key);

        return container.has(namespacedKey, type) ? Optional.ofNullable(container.get(namespacedKey, type)) : Optional.empty();
    }

    public static <T> boolean hasPersistentData(@NotNull ItemStack itemStack, @NotNull String key, @NotNull PersistentDataType<T, T> type) {
        if (!itemStack.hasItemMeta()) {
            return false;
        }
        ItemMeta meta = itemStack.getItemMeta();
        return meta.getPersistentDataContainer().has(Common.newNamespacedKey(key), type);
    }

    public static void removePersistentData(@NotNull ItemStack itemStack, @NotNull String key) {
        setItemMeta(itemStack, meta -> meta.getPersistentDataContainer().remove(Common.newNamespacedKey(key)));
    }
}

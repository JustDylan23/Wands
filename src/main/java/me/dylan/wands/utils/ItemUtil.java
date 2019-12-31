package me.dylan.wands.utils;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.function.Consumer;

public final class ItemUtil {
    private ItemUtil() {
    }

    public static void setItemMeta(@NotNull ItemStack itemStack, @NotNull Consumer<ItemMeta> consumer) {
        if (itemStack.getType() == Material.AIR) {
            return;
        }
        ItemMeta itemMeta = itemStack.getItemMeta();
        consumer.accept(itemMeta);
        itemStack.setItemMeta(itemMeta);
    }

    public static void setName(@NotNull ItemStack itemStack, String name) {
        setItemMeta(itemStack, meta -> meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name)));
    }

    @SuppressWarnings("ConstantConditions")
    public static @NotNull String getName(@NotNull ItemStack itemStack) {
        return (itemStack.hasItemMeta()) ? itemStack.getItemMeta().getDisplayName() : "";
    }

    public static <T> void setPersistentData(@NotNull ItemStack itemStack, @NotNull String key, @NotNull PersistentDataType<T, T> type, T t) {
        setItemMeta(itemStack, meta -> meta.getPersistentDataContainer().set(KeyFactory.getOrCreateKey(key), type, t));
    }

    @SuppressWarnings("ConstantConditions")
    public static <T> Optional<T> getPersistentData(@NotNull ItemStack itemStack, @NotNull String key, @NotNull PersistentDataType<T, T> type) {
        if (itemStack.getType() != Material.AIR) {
            return Optional.ofNullable(itemStack.getItemMeta().getPersistentDataContainer().get(KeyFactory.getOrCreateKey(key), type));
        }
        return Optional.empty();
    }

    @SuppressWarnings("ConstantConditions")
    public static <T> boolean hasPersistentData(@NotNull ItemStack itemStack, @NotNull String key, @NotNull PersistentDataType<T, T> type) {
        if (itemStack.getType() != Material.AIR) {
            return itemStack.getItemMeta().getPersistentDataContainer().has(KeyFactory.getOrCreateKey(key), type);
        }
        return false;
    }

    public static void removePersistentData(@NotNull ItemStack itemStack, @NotNull String key) {
        setItemMeta(itemStack, meta -> meta.getPersistentDataContainer().remove(KeyFactory.getOrCreateKey(key)));
    }
}

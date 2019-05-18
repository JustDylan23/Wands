package me.dylan.wands.utils;

import me.dylan.wands.Wands;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.Optional;

@SuppressWarnings({"WeakerAccess", "unused"})
public class PersistentDataUtil {
    private static final Wands plugin = Wands.getPlugin();

    private PersistentDataUtil() {
        throw new UnsupportedOperationException();
    }

    public static <T> void setPersistentData(ItemStack itemStack, String key, PersistentDataType<T, T> type, T t) {
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        NamespacedKey key_ = new NamespacedKey(plugin, key);

        container.set(key_, type, t);
        itemStack.setItemMeta(meta);
    }

    public static <T> Optional<T> getPersistentData(ItemStack itemStack, String key, PersistentDataType<T, T> type) {
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        NamespacedKey key_ = new NamespacedKey(plugin, key);

        return container.has(key_, type) ? Optional.ofNullable(container.get(key_, type)) : Optional.empty();
    }

    public static <T> boolean hasPersistentData(ItemStack itemStack, String key, PersistentDataType<T, T> type) {
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        NamespacedKey key_ = new NamespacedKey(plugin, key);

        return container.has(key_, type);
    }

    public static void removePersistentData(ItemStack itemStack, String key) {
        ItemMeta meta = itemStack.getItemMeta();
        PersistentDataContainer container = meta.getPersistentDataContainer();
        NamespacedKey key_ = new NamespacedKey(plugin, key);

        container.remove(key_);
    }
}

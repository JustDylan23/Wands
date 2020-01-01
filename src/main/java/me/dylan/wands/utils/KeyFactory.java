package me.dylan.wands.utils;

import me.dylan.wands.WandsPlugin;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.Map;

public final class KeyFactory {
    private static final Map<String, NamespacedKey> namespacedKeyMap = new HashMap<>();
    private static final Plugin plugin = WandsPlugin.getInstance();

    private KeyFactory() {
    }

    public static NamespacedKey getOrCreateKey(String key) {
        return namespacedKeyMap.computeIfAbsent(key, s -> new NamespacedKey(plugin, s));
    }
}

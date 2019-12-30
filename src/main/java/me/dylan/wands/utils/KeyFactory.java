package me.dylan.wands.utils;

import me.dylan.wands.WandsPlugin;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public final class KeyFactory {
    private static Map<String, NamespacedKey> namespacedKeyMap = new HashMap<>();
    private static Plugin plugin = JavaPlugin.getPlugin(WandsPlugin.class);

    private KeyFactory() {
    }

    public static NamespacedKey getOrCreateKey(String key) {
        return namespacedKeyMap.computeIfAbsent(key, s -> new NamespacedKey(plugin, s));
    }
}

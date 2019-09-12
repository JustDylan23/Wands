package me.dylan.wands.config;

import me.dylan.wands.WandsPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

final class ConfigUtil {
    private static final Plugin plugin = JavaPlugin.getPlugin(WandsPlugin.class);
    private static FileConfiguration config = plugin.getConfig();

    private ConfigUtil() {
        throw new UnsupportedOperationException("Instantiating util class");
    }

    static FileConfiguration getConfig() {
        return config;
    }

    static void reloadConfig() {
        plugin.reloadConfig();
        config = plugin.getConfig();
    }

    static int getInt(String key) {
        return config.getInt(key);
    }

    static boolean getBoolean(String key) {
        return config.getBoolean(key);
    }

    static void set(String key, Object value) {
        config.set(key, value);
        plugin.saveConfig();
    }
}

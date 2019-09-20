package me.dylan.wands.config;

import me.dylan.wands.WandsPlugin;
import me.dylan.wands.utils.Common;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

final class ConfigUtil {
    private static final Plugin plugin = JavaPlugin.getPlugin(WandsPlugin.class);
    private static FileConfiguration config = plugin.getConfig();

    private ConfigUtil() {
        throw new UnsupportedOperationException("Instantiating util class");
    }

    static void reloadConfig() {
        plugin.reloadConfig();
        config = plugin.getConfig();
    }

    static int getIntWithCorrectedRange(int max, int min, String key) {
        int a = config.getInt(key);
        int b = Common.getIntInRange(min, max, a);
        if (a != b) {
            WandsPlugin.log("at: " + key);
            WandsPlugin.log("found: " + a);
            WandsPlugin.log("to: " + b);
            set(key, b);
        }
        return b;
    }

    static boolean getAndRenderBoolean(String key, boolean render) {
        boolean bool = config.getBoolean(key);
        if (render) config.set(key, bool);
        return bool;
    }

    static void set(String key, Object value) {
        config.set(key, value);
        plugin.saveConfig();
        config = plugin.getConfig();
    }
}

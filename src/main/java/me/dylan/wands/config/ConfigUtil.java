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

    static int getIntWithCorrectedRange(int max, int min, String path) {
        int a = config.getInt(path);
        int b = Common.getIntInRange(min, max, a);
        if (a != b) {
            WandsPlugin.log("at: " + path);
            WandsPlugin.log("found: " + a);
            WandsPlugin.log("to: " + b);
            set(path, b);
        }
        return b;
    }

    static boolean getAndRenderBoolean(String path, boolean render) {
        boolean bool = config.getBoolean(path);
        if (render) config.set(path, bool);
        return bool;
    }

    static void set(String key, Object value) {
        config.set(key, value);
        plugin.saveConfig();
        config = plugin.getConfig();
    }
}

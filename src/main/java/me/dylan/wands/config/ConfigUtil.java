package me.dylan.wands.config;

import me.dylan.wands.Main;
import org.bukkit.configuration.file.FileConfiguration;

@SuppressWarnings("SameParameterValue")
final class ConfigUtil {
    private static FileConfiguration config = Main.getPlugin().getConfig();


    private ConfigUtil() {
        throw new UnsupportedOperationException("Instantiating util class");
    }

    public static void reloadConfig() {
        Main.getPlugin().reloadConfig();
        config = Main.getPlugin().getConfig();
    }

    static int getInt(String key) {
        return config.getInt(key);
    }

    static boolean getBoolean(String key) {
        return config.getBoolean(key);
    }

    static void set(String key, Object value) {
        config.set(key, value);
    }
}

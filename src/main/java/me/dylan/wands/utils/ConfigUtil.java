package me.dylan.wands.utils;

import me.dylan.wands.Wands;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigUtil {
    private static final FileConfiguration config = Wands.getPlugin().getConfig();

    private ConfigUtil() {
        throw new UnsupportedOperationException();
    }

    public static int getInt(String key) {
        return config.getInt(key);
    }

    public static void setInt(String key, int value) {
        config.set(key, value);
    }

    public static boolean getBoolean(String key) {
        return config.getBoolean(key);
    }

    public static void setBoolean(String key, boolean boolean_) {
        config.set(key, boolean_);
    }
}
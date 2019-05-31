package me.dylan.wands.util;

import me.dylan.wands.Main;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigUtil {
    private static FileConfiguration config = Main.getPlugin().getConfig();

    private ConfigUtil() {
        throw new UnsupportedOperationException();
    }

    public static void reloadConfig() {
        Main.getPlugin().reloadConfig();
        config = Main.getPlugin().getConfig();
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

    public static void setBoolean(String key, boolean v) {
        config.set(key, v);
    }
}

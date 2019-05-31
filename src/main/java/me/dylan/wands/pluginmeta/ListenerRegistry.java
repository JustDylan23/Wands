package me.dylan.wands.pluginmeta;

import me.dylan.wands.Main;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ListenerRegistry {

    private static final Main plugin = Main.getPlugin();
    private final Set<Listener> toggleableListeners = new HashSet<>();

    public static void addListener(Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
        }
    }

    public void addToggleableListener(Listener... listeners) {
        toggleableListeners.addAll(Arrays.asList(listeners));
        if (Main.getPlugin().getConfigurableData().isMagicUseAllowed()) {
            Main.log("magic usage is allowed");
            addListener(listeners);
        }
    }

    void disableListeners() {
        toggleableListeners.forEach(HandlerList::unregisterAll);
        Main.log("disabling listeners...");
    }

    void enableListeners() {
        toggleableListeners.forEach(HandlerList::unregisterAll);
        toggleableListeners.forEach(listener -> Bukkit.getServer().getPluginManager().registerEvents(listener, plugin));
    }
}

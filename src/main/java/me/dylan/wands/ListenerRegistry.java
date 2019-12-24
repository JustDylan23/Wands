package me.dylan.wands;

import me.dylan.wands.commandhandler.commands.Wands;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ListenerRegistry {
    private static final WandsPlugin plugin = JavaPlugin.getPlugin(WandsPlugin.class);
    private final Set<Listener> toggleableListeners = new HashSet<>();

    public static void addListener(@NotNull Listener... listeners) {
        for (Listener listener : listeners) {
            WandsPlugin.debug("Registered listener for " + listener.getClass());
            Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
        }
    }

    void addToggleableListener(Listener... listeners) {
        toggleableListeners.addAll(Arrays.asList(listeners));
        if (plugin.getConfigHandler().isMagicEnabled()) {
            addListener(listeners);
        }
    }

    public void disableListeners() {
        toggleableListeners.forEach(HandlerList::unregisterAll);
        WandsPlugin.debug("disabled listeners");
    }

    public void enableListeners() {
        disableListeners();
        toggleableListeners.forEach(listener -> Bukkit.getServer().getPluginManager().registerEvents(listener, plugin));
        WandsPlugin.debug("enabled " + toggleableListeners.size() +  " listener(s)");
    }
}

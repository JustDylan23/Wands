package me.dylan.wands.plugindata;

import me.dylan.wands.Wands;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ListenerRegister {

    private static final Wands plugin = Wands.getPlugin();
    private final Set<Listener> toggleableListeners = new HashSet<>();

    public void addListener(Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
        }
    }

    public void addToggleableListener(Listener... listeners) {
        toggleableListeners.addAll(Arrays.asList(listeners));
        addListener(listeners);
    }

    void disableListeners() {
        toggleableListeners.forEach(HandlerList::unregisterAll);
    }

    void enableListeners() {
        toggleableListeners.forEach(HandlerList::unregisterAll);
        toggleableListeners.forEach(listener -> Bukkit.getServer().getPluginManager().registerEvents(listener, plugin));
    }
}

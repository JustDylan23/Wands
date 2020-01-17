package me.dylan.wands;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class ListenerRegistry {
    private static final WandsPlugin plugin = WandsPlugin.getInstance();
    private static final Listener[] LISTENERS = new Listener[0];
    private final Set<Listener> toggleableListeners = new HashSet<>();

    public static void addListener(@NotNull Listener... listeners) {
        for (Listener listener : listeners) {
            WandsPlugin.debug("Registered listener: " + listener.getClass());
            Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
        }
    }

    void addToggleableListenerAndEnable(Listener... listeners) {
        toggleableListeners.addAll(Arrays.asList(listeners));
        if (plugin.getConfigHandler().isMagicEnabled()) {
            for (Listener listener : listeners) {
                WandsPlugin.debug("Registered toggleable listener: " + listener.getClass());
                Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
            }
        }
    }

    public void disableListeners() {
        toggleableListeners.forEach(HandlerList::unregisterAll);
        WandsPlugin.debug("Disabled all toggleable listeners");
    }

    public void enableListeners() {
        addToggleableListenerAndEnable(toggleableListeners.toArray(LISTENERS));
    }
}

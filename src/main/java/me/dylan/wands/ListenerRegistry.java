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
    private final Set<Listener> toggleableListeners = new HashSet<>();
    private boolean isEnabled = false;

    public static void addListener(@NotNull Listener listener) {
        WandsPlugin.debug("Registered listener: " + listener.getClass());
        Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
    }

    /**
     * Used to enable/disable toggleable listeners.
     * listeners will only be disabled/enabled if that  .
     *
     * @param value boolean
     * @return whether enabling or disabling had any effect
     */
    public boolean enableListeners(boolean value) {
        if (isEnabled == value) return false;
        isEnabled = value;
        if (isEnabled) {
            toggleableListeners.forEach(listener ->
                    Bukkit.getServer().getPluginManager().registerEvents(listener, plugin)
            );
        } else {
            toggleableListeners.forEach(HandlerList::unregisterAll);
        }

        return true;
    }

    void addToggleableListenerAndEnable(Listener... listeners) {
        toggleableListeners.addAll(Arrays.asList(listeners));
        if (isEnabled) {
            for (Listener listener : listeners) {
                WandsPlugin.debug("Registered toggleable listener: " + listener.getClass());
                Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
            }
        }
    }
}

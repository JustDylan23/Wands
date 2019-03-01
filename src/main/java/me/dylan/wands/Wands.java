package me.dylan.wands;

import me.dylan.wands.artifacts.EmpireBow;
import me.dylan.wands.artifacts.TherosDagger;
import me.dylan.wands.commandhandler.ConstructTabCompleter;
import me.dylan.wands.commandhandler.MainCommandHandler;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("WeakerAccess")
public final class Wands extends JavaPlugin {

    private static Wands plugin;

    public static final String PREFIX = "§8§l[§6§lWands§8§l]§r ";

    //if this is false, the wands should all stop with working.
    private boolean wandsEnabled = true;
    private final Set<Listener> toggleableListeners = new HashSet<>();

    private final SpellRegistry spellRegistry = new SpellRegistry();


    @Override
    public void onEnable() {
        if (!Bukkit.getVersion().contains("Paper")) {
            this.getPluginLoader().disablePlugin(this);
            sendConsole("§cThis plugin only works on Paper, an improved version of spigot.");
            sendConsole("§cDisabling plugin...");
        } else {
            plugin = this;

            this.getCommand("wands").setExecutor(new MainCommandHandler());
            this.getCommand("wands").setTabCompleter(new ConstructTabCompleter());

            addListener(new GUIs());

            addToggleableListener(
                    new PlayerInteractionListener(),
                    new TherosDagger(),
                    new EmpireBow()
            );

            spellRegistry.registerSpells(Spell.values());
        }
    }

    public static Wands getPlugin() {
        return plugin;
    }

    public SpellRegistry getSpellRegistry() {
        return spellRegistry;
    }

    public void sendConsole(String text) {
        Bukkit.getConsoleSender().sendMessage(PREFIX + text);
    }

    public void addListener(Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getServer().getPluginManager().registerEvents(listener, this);
        }
    }

    public void addToggleableListener(Listener... listeners) {
        toggleableListeners.addAll(Arrays.asList(listeners));
        addListener(listeners);
    }

    private void disableListeners() {
        toggleableListeners.forEach(HandlerList::unregisterAll);
    }

    private void enableListeners() {
        toggleableListeners.forEach(listener -> Bukkit.getServer().getPluginManager().registerEvents(listener, this));
    }

    public void setWandsEnabled(boolean enabled) {
        if (wandsEnabled != enabled) {
            wandsEnabled = enabled;
            if (enabled) enableListeners();
            else disableListeners();
        }
    }

    public void toggleWandsEnabled() {
        setWandsEnabled(!wandsEnabled);
    }

    public boolean getWandsEnabled() {
        return getPlugin().wandsEnabled;
    }
}

package me.dylan.wands;

import me.dylan.wands.commandhandler.ConstructTabCompleter;
import me.dylan.wands.commandhandler.MainCommandHandler;
import me.dylan.wands.presetitems.EmpireBow;
import me.dylan.wands.presetitems.TherosDagger;
import me.dylan.wands.spellfoundation.SpellRegistry;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("WeakerAccess")
public final class Wands extends JavaPlugin {

    public static final String PREFIX = "§8§l[§6§lWands§8§l]§r ";
    private static Wands plugin;
    private final Set<Listener> toggleableListeners = new HashSet<>();
    private SpellRegistry spellRegistry;
    //if this is false, the wands should all stop with working.
    private boolean wandsEnabled = true;
    private int coolDownTime = 5;

    public static Wands getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        try {
            Player.class.getMethod("sendActionBar", String.class);
        } catch (NoSuchMethodException e) {
            this.getPluginLoader().disablePlugin(this);
            sendConsole("§cThis plugin only works on Paper, an improved version of spigot.");
            sendConsole("§cDisabling plugin...");
            return;
        }
        plugin = this;
        this.getCommand("wands").setExecutor(new MainCommandHandler());
        this.getCommand("wands").setTabCompleter(new ConstructTabCompleter());

        addToggleableListener(
                new PlayerInteractionListener(),
                new TherosDagger(),
                new EmpireBow()
        );
        spellRegistry = new SpellRegistry();
        spellRegistry.loadSpells();

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
        toggleableListeners.forEach(HandlerList::unregisterAll);
        toggleableListeners.forEach(listener -> Bukkit.getServer().getPluginManager().registerEvents(listener, this));
    }

    public void toggleWandsEnabled() {
        setWandsEnabled(!wandsEnabled);
    }

    public boolean getWandsEnabled() {
        return getPlugin().wandsEnabled;
    }

    public void setWandsEnabled(boolean enabled) {
        if (wandsEnabled != enabled) {
            wandsEnabled = enabled;
            if (enabled) enableListeners();
            else disableListeners();
        }
    }

    public int getCoolDownTime() {
        return coolDownTime;
    }

    public void setCoolDownTime(int i) {
        coolDownTime = i;
    }
}

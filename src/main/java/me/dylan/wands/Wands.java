package me.dylan.wands;

import me.dylan.wands.artifacts.EmpireBow;
import me.dylan.wands.artifacts.TherosDagger;
import me.dylan.wands.commandhandler.ConstructTabCompleter;
import me.dylan.wands.commandhandler.MainCommandHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("WeakerAccess")
public final class Wands extends JavaPlugin {

    public static final String PREFIX = ChatColor.translateAlternateColorCodes('&', "&8&l[&6&lWands&8&l]&r ");
    private static Wands plugin;

    //if this is false, the wands should all stop with working.
    private boolean STATUS = true;
    public static final String VERSION = "1.0.0";

    private final SpellRegistry spellRegistry = new SpellRegistry();


    public void onEnable() {
        plugin = this;

        sendConsole("Up and running!");
        this.getCommand("wands").setExecutor(new MainCommandHandler());
        this.getCommand("wands").setTabCompleter(new ConstructTabCompleter());

        registerListener(new GUIs(), new SpellManager());

        registerListener(new TherosDagger(), new EmpireBow());

        spellRegistry.registerSpells(Spell.values());
    }

    public void onDisable() {
        plugin = null;
    }

    public void sendConsole(String text) {
        Bukkit.getConsoleSender().sendMessage(PREFIX + text);
    }

    public void registerListener(Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getServer().getPluginManager().registerEvents(listener, this);
        }
    }

    public static Wands getInstance() {
        return plugin;
    }

    public SpellRegistry getSpellRegistry() {
        return spellRegistry;
    }

    public void disable() {
        getInstance().STATUS = false;
    }

    public void enable() {
        getInstance().STATUS = true;
    }

    public void toggleStatus() {
        getInstance().STATUS = !getInstance().STATUS;
    }

    public boolean getStatus() {
        return getInstance().STATUS;
    }
}

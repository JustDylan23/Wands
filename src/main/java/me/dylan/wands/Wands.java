package me.dylan.wands;

import me.dylan.wands.artifacts.EmpireBow;
import me.dylan.wands.artifacts.TherosDagger;
import me.dylan.wands.commandhandler.ConstructTabCompleter;
import me.dylan.wands.commandhandler.MainCommandHandler;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("WeakerAccess")
public final class Wands extends JavaPlugin {

    private static Wands plugin;

    public static final String PREFIX = "§8§l[§6§lWands§8§l]§r ";

    //if this is false, the wands should all stop with working.
    private boolean status = true;

    private final SpellRegistry spellRegistry = new SpellRegistry();

    @Override
    public void onEnable() {
        if(!Bukkit.getVersion().contains("Paper")) {
            this.getPluginLoader().disablePlugin(this);
            sendConsole("§cThis plugin only works on Paper, an improved version of spigot.");
            sendConsole("§cDisabling plugin...");
        } else {
            plugin = this;

            this.getCommand("wands").setExecutor(new MainCommandHandler());
            this.getCommand("wands").setTabCompleter(new ConstructTabCompleter());

            registerListener(
                    new GUIs(),
                    new SpellManager(),
                    new TherosDagger(),
                    new EmpireBow()
            );

            spellRegistry.registerSpells(Spell.values());
        }
    }

    @Override
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

    public static Wands getPlugin() {
        return plugin;
    }

    public SpellRegistry getSpellRegistry() {
        return spellRegistry;
    }

    public void disable() {
        getPlugin().status = false;
    }

    public void enable() {
        getPlugin().status = true;
    }

    public boolean setStatus(boolean status) {
        return getPlugin().status = status;
    }

    public void toggleStatus() {
        getPlugin().status = !getPlugin().status;
    }

    public boolean getStatus() {
        return getPlugin().status;
    }
}

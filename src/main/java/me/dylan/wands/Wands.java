package me.dylan.wands;

import me.dylan.wands.commandhandler.ConstructTabCompleter;
import me.dylan.wands.commandhandler.MainCommandHandler;
import me.dylan.wands.plugindata.ListenerRegister;
import me.dylan.wands.plugindata.PluginData;
import me.dylan.wands.presetitems.EmpireBow;
import me.dylan.wands.presetitems.TherosDagger;
import me.dylan.wands.spellfoundation.SpellRegistry;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("WeakerAccess")
public final class Wands extends JavaPlugin {

    private static Wands plugin;

    public static final String PREFIX = "§8§l[§6§lWands§8§l]§r ";

    //instances of classes accessible via main class
    private SpellRegistry spellRegistry;
    private PluginData pluginData;
    private ListenerRegister listenerRegister;

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

        PluginCommand cmd =  this.getCommand("wands");
        assert (cmd != null): "Command \"wands\" was not set up correctly in the plugin.yml";
        cmd.setExecutor(new MainCommandHandler());
        cmd.setTabCompleter(new ConstructTabCompleter());

        plugin = this;
        listenerRegister = new ListenerRegister();
        spellRegistry = new SpellRegistry().loadSpells();
        pluginData = new PluginData();

        listenerRegister.addToggleableListener(
                new PlayerInteractionListener(),
                new TherosDagger(),
                new EmpireBow()
        );
    }

    @Override
    public void onDisable() {
        saveConfig();
    }

    public SpellRegistry getSpellRegistry() {
        return spellRegistry;
    }

    public PluginData getPluginData() {
        return pluginData;
    }

    public ListenerRegister getListenerRegister() {
        return listenerRegister;
    }

    public void sendConsole(String text) {
        Bukkit.getConsoleSender().sendMessage(PREFIX + text);
    }
}

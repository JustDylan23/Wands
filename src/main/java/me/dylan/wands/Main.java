package me.dylan.wands;

import me.dylan.wands.commandhandler.MainCommandHandler;
import me.dylan.wands.commandhandler.MainTabCompleter;
import me.dylan.wands.customitem.CustomBow;
import me.dylan.wands.customitem.CustomDagger;
import me.dylan.wands.pluginmeta.ConfigurableData;
import me.dylan.wands.pluginmeta.ListenerRegistry;
import me.dylan.wands.spell.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    private static Main plugin;

    public static final String PREFIX = "§8§l[§6§lWands§8§l]§r ";

    //instances of classes accessible via main class
    private ConfigurableData configurableData;
    private ListenerRegistry listenerRegistry;

    public static Main getPlugin() {
        return plugin;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onEnable() {
        try {
            Player.class.getMethod("sendActionBar", String.class);
        } catch (NoSuchMethodException e) {
            this.getPluginLoader().disablePlugin(this);
            log("§cThis plugin only works on Paper, an improved version of spigot.");
            log("§cDisabling plugin...");
            return;
        }

        PluginCommand cmd = this.getCommand("wands");
        cmd.setExecutor(new MainCommandHandler());
        cmd.setTabCompleter(new MainTabCompleter());

        plugin = this;
        listenerRegistry = new ListenerRegistry();
        configurableData = new ConfigurableData();
        listenerRegistry.addToggleableListener(
                new PlayerListener(),
                new CustomDagger(),
                new CustomBow()
        );
        log("Succesfully enabled");
    }

    @Override
    public void onDisable() {
        saveConfig();
    }

    public ConfigurableData getConfigurableData() {
        return configurableData;
    }

    public ListenerRegistry getListenerRegistry() {
        return listenerRegistry;
    }

    public static void log(String text) {
        Bukkit.getLogger().info(PREFIX + text);
    }
}

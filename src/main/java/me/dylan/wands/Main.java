package me.dylan.wands;

import me.dylan.wands.commandhandler.CommandHandler;
import me.dylan.wands.commandhandler.TabCompletionHandler;
import me.dylan.wands.customitem.AssasinDagger;
import me.dylan.wands.customitem.CursedBow;
import me.dylan.wands.pluginmeta.ConfigurableData;
import me.dylan.wands.pluginmeta.ListenerRegistry;
import me.dylan.wands.spell.PlayerListener;
import me.dylan.wands.spell.handler.MovingBlockSpell;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    public static final String PREFIX = "§8§l[§6§lWands§8§l]§r ";
    private static Main plugin;
    //instances of classes accessible via main class
    private ConfigurableData configurableData;
    private ListenerRegistry listenerRegistry;

    public static Main getPlugin() {
        return plugin;
    }

    public static void log(String text) {
        Bukkit.getLogger().info(PREFIX + text);
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
        plugin = this;

        PluginCommand cmd = this.getCommand("wands");
        cmd.setExecutor(new CommandHandler());
        cmd.setTabCompleter(new TabCompletionHandler());

        listenerRegistry = new ListenerRegistry();
        configurableData = new ConfigurableData();
        listenerRegistry.addToggleableListener(
                new PlayerListener(),
                new AssasinDagger(),
                new CursedBow()
        );
        log("Successfully enabled");
    }

    @Override
    public void onDisable() {
        saveConfig();
        MovingBlockSpell.restorePendingBlocks();
    }

    public ConfigurableData getConfigurableData() {
        return configurableData;
    }

    public ListenerRegistry getListenerRegistry() {
        return listenerRegistry;
    }
}

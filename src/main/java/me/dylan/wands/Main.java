package me.dylan.wands;

import me.dylan.wands.commandhandler.CommandHandler;
import me.dylan.wands.commandhandler.TabCompletionHandler;
import me.dylan.wands.customitems.AssassinDagger;
import me.dylan.wands.customitems.CursedBow;
import me.dylan.wands.pluginmeta.ConfigurableData;
import me.dylan.wands.pluginmeta.ListenerRegistry;
import me.dylan.wands.spell.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

public final class Main extends JavaPlugin {

    public static final String PREFIX = "§8§l[§6§lWands§8§l]§r ";
    private static Main plugin;
    private final Set<Runnable> disableLogic = new HashSet<>();
    //instances of classes accessible via main class
    private ConfigurableData configurableData;
    private ListenerRegistry listenerRegistry;

    public static Main getPlugin() {
        return plugin;
    }

    @SuppressWarnings("WeakerAccess")
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
                new AssassinDagger(),
                new CursedBow()
        );
        log("Successfully enabled");
    }

    @Override
    public void onDisable() {
        saveConfig();
        disableLogic.forEach(Runnable::run);
    }

    public void addDisableLogic(Runnable runnable) {
        this.disableLogic.add(runnable);
    }

    public void removeDisableLogic(Runnable runnable) {
        this.disableLogic.remove(runnable);
    }

    public ConfigurableData getConfigurableData() {
        return configurableData;
    }

    public ListenerRegistry getListenerRegistry() {
        return listenerRegistry;
    }
}

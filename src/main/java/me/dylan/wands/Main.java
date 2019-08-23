package me.dylan.wands;

import me.dylan.wands.commandhandler.commands.*;
import me.dylan.wands.commandhandler.tabcompleters.BindComplete;
import me.dylan.wands.commandhandler.tabcompleters.UnbindComplete;
import me.dylan.wands.commandhandler.tabcompleters.WandsComplete;
import me.dylan.wands.config.ConfigurableData;
import me.dylan.wands.customitems.AssassinDagger;
import me.dylan.wands.customitems.CursedBow;
import me.dylan.wands.customitems.MortalBlade;
import me.dylan.wands.spell.CooldownManager;
import me.dylan.wands.spell.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;

public final class Main extends JavaPlugin {

    public static final String PREFIX = "§8§l[§6§lWands§8§l]§r ";
    private static Main plugin;
    private final Set<Runnable> disableLogic = new HashSet<>();
    // instances of classes accessible via main class
    private ConfigurableData configurableData;
    private ListenerRegistry listenerRegistry;
    private MouseClickListeners mouseClickListeners;
    private CooldownManager cooldownManager;

    public static Main getPlugin() {
        return plugin;
    }

    @SuppressWarnings("WeakerAccess")
    public static void log(String text) {
        Bukkit.getLogger().info(PREFIX + text);
    }

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

        addCommand("wands", new Wands(), new WandsComplete());
        addCommand("createwand", new CreateWand(), null);
        addCommand("bind", new Bind(), new BindComplete());
        addCommand("unbind", new Unbind(), new UnbindComplete());
        addCommand("bindall", new BindAll(), null);
        addCommand("unbindall", new UnbindAll(), null);

        this.listenerRegistry = new ListenerRegistry();
        this.configurableData = new ConfigurableData();
        this.mouseClickListeners = new MouseClickListeners();
        this.cooldownManager = new CooldownManager();

        listenerRegistry.addToggleableListener(
                new PlayerListener(),
                new AssassinDagger(),
                new CursedBow(),
                new MortalBlade(),
                mouseClickListeners
        );
        log("Successfully enabled");
    }

    @Override
    public void onDisable() {
        saveConfig();
        this.disableLogic.forEach(Runnable::run);
    }

    private void addCommand(String command, CommandExecutor executor, TabCompleter tabCompleter) {
        PluginCommand cmd = getCommand(command);
        if (cmd == null) throw new NullPointerException("Command " + command + "was not found in plugin.yml");
        cmd.setExecutor(executor);
        cmd.setTabCompleter(tabCompleter);
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

    public MouseClickListeners getMouseClickListeners() {
        return mouseClickListeners;
    }

    public CooldownManager getCooldownManager() {
        return cooldownManager;
    }
}

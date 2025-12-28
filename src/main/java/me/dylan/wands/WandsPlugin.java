package me.dylan.wands;

import me.dylan.wands.commandhandler.commands.*;
import me.dylan.wands.commandhandler.tabcompleters.BindComplete;
import me.dylan.wands.commandhandler.tabcompleters.TweakSpellComplete;
import me.dylan.wands.commandhandler.tabcompleters.UnbindComplete;
import me.dylan.wands.commandhandler.tabcompleters.WandsComplete;
import me.dylan.wands.config.ConfigHandler;
import me.dylan.wands.customitems.AssassinDagger;
import me.dylan.wands.customitems.CursedBow;
import me.dylan.wands.spell.CooldownManager;
import me.dylan.wands.spell.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

public final class WandsPlugin extends JavaPlugin {

    public static final boolean DEBUG = false;

    public static final String PREFIX = "§8§l[§6§lWands§8§l]§r ";
    public static final String PREFIX_TOP = "§e---- " + PREFIX + "§e----§r\n";
    private static final String PREFIX_WARNING = "§e[§e§lWARNING§r] §c";
    private static final Set<Runnable> disableLogic = new HashSet<>();
    private static WandsPlugin instance = null;

    private File configFile = null;
    private ConfigHandler configHandler = null;
    private CooldownManager cooldownManager = null;
    private Updater updater = null;

    public static void log(String message) {
        Bukkit.getLogger().info(PREFIX + message);
    }

    public static void warn(String message) {
        Bukkit.getLogger().info(PREFIX + PREFIX_WARNING + message);
    }

    public static void debug(Object message) {
        if (DEBUG) {
            log(String.valueOf(message));
        }
    }

    public static void addDisableLogic(Runnable runnable) {
        disableLogic.add(runnable);
    }

    public static WandsPlugin getInstance() {
        return Objects.requireNonNull(instance);
    }

    private static void loadListeners(ListenerRegistry listenerRegistry, ConfigHandler configHandler) {
        listenerRegistry.enableListeners(configHandler.isMagicEnabled());

        PlayerListener playerListener = new PlayerListener();
        AssassinDagger assassinDagger = new AssassinDagger();
        CursedBow cursedBow = new CursedBow();

        MouseClickListeners mouseClickListeners = new MouseClickListeners();

        mouseClickListeners.addLeftAndRightClickListener(playerListener);
        mouseClickListeners.addRightClickListener(assassinDagger);
        mouseClickListeners.addRightClickListener(cursedBow);

        listenerRegistry.addToggleableListenerAndEnable(
                playerListener,
                assassinDagger,
                cursedBow,
                mouseClickListeners
        );
    }

    private static void loadCommands(ConfigHandler configHandler, WandsPlugin plugin) {
        plugin.addCommand("wands", new Wands(configHandler, plugin.getDescription().getVersion()), new WandsComplete());
        plugin.addCommand("createwand", new CreateWand(), null);
        plugin.addCommand("clearwand", new ClearWand(), null);
        plugin.addCommand("bind", new Bind(), new BindComplete());
        plugin.addCommand("unbind", new Unbind(), new UnbindComplete());
        plugin.addCommand("bindall", new BindAll(), null);
        plugin.addCommand("unbindall", new UnbindAll(), null);
        plugin.addCommand("tweakcooldown", new TweakCooldown(configHandler), new TweakSpellComplete());
    }

    @Override
    public void onEnable() {
        instance = this;
        ListenerRegistry listenerRegistry = new ListenerRegistry();
        this.configFile = new File(getDataFolder(), "config.dat");
        this.configHandler = ConfigHandler.load(configFile, listenerRegistry);
        this.cooldownManager = new CooldownManager(configHandler);
        loadListeners(listenerRegistry, configHandler);
        loadCommands(configHandler, this);
        this.updater = new Updater(getFile().getName(), this, configHandler);
        new MetricsLite(this);
        log("Enabled successfully");
    }

    @Override
    public void onDisable() {
        configHandler.save(configFile);
        disableLogic.forEach(Runnable::run);
    }

    private void addCommand(String command, CommandExecutor executor, TabCompleter tabCompleter) {
        PluginCommand cmd = getCommand(command);
        if (cmd == null) throw new NoSuchElementException("Command " + command + " was not found in plugin.yml");
        cmd.setExecutor(executor);
        cmd.setTabCompleter(tabCompleter);
    }

    public ConfigHandler getConfigHandler() {
        return configHandler;
    }

    public CooldownManager getCooldownManager() {
        return cooldownManager;
    }

    public Updater getUpdater() {
        return updater;
    }
}

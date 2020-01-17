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
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

public final class WandsPlugin extends JavaPlugin {

    public static final boolean DEBUG = true;

    public static final String PREFIX = "§8§l[§6§lWands§8§l]§r ";
    public static final String PREFIX_TOP = "§e---- " + PREFIX + "§e----§r\n";
    private static final String PREFIX_WARNING = "§e[§e§lWARNING§r] §c";
    private static final Set<Runnable> disableLogic = new HashSet<>();
    private static WandsPlugin instance = null;

    private File configFile = null;
    private ConfigHandler configHandler = null;
    private CooldownManager cooldownManager = null;

    public static void log(String message) {
        Bukkit.getLogger().info(PREFIX + message);
    }

    public static void warn(String message) {
        Bukkit.getLogger().info(PREFIX + PREFIX_WARNING + message);
    }

    public static void debug(String message) {
        if (DEBUG) {
            log(message);
        }
    }

    public static void addDisableLogic(Runnable runnable) {
        disableLogic.add(runnable);
    }

    public static WandsPlugin getInstance() {
        return Objects.requireNonNull(instance);
    }

    @Override
    public void onEnable() {
        instance = this;
        ListenerRegistry listenerRegistry = new ListenerRegistry();
        this.configFile = new File(getDataFolder(), "config.dat");
        this.configHandler = ConfigHandler.load(configFile, listenerRegistry);
        this.cooldownManager = new CooldownManager(configHandler);
        loadListeners(listenerRegistry);
        loadCommands();
        log("Enabled successfully");
        log("Checking for updates...");
        Bukkit.getScheduler().runTaskTimer(this, this::lookForUpdates, 0L, 3600L);
        new MetricsLite(this);
    }

    private void lookForUpdates() {
        UpdateChecker.getLatestVersionString().whenComplete((fetchedVersion, throwable) -> {
            if (throwable != null) {
                WandsPlugin.warn("Failed to check if updates are available");
                return;
            }
            String currentVersion = getDescription().getVersion();
            if (!currentVersion.equals(fetchedVersion)) {
                String message = "A new update is available\n" +
                        currentVersion + "->" + fetchedVersion +
                        "\nAvailable here: " + UpdateChecker.RESOURCE;
                Bukkit.getOnlinePlayers().stream().filter(Player::isOp).forEach(p -> p.sendMessage(
                        PREFIX_TOP + message
                ));
                log(message);

            }
        });
    }

    @Override
    public void onDisable() {
        configHandler.save(configFile);
        disableLogic.forEach(Runnable::run);
    }

    private void loadListeners(ListenerRegistry listenerRegistry) {
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

    private void loadCommands() {
        addCommand("wands", new Wands(configHandler, getDescription().getVersion()), new WandsComplete());
        addCommand("createwand", new CreateWand(), null);
        addCommand("clearwand", new ClearWand(), null);
        addCommand("bind", new Bind(), new BindComplete());
        addCommand("unbind", new Unbind(), new UnbindComplete());
        addCommand("bindall", new BindAll(), null);
        addCommand("unbindall", new UnbindAll(), null);
        addCommand("tweakcooldown", new TweakCooldown(configHandler), new TweakSpellComplete());
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
}

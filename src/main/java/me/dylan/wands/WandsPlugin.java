package me.dylan.wands;

import com.google.gson.Gson;
import me.dylan.wands.commandhandler.commands.*;
import me.dylan.wands.commandhandler.tabcompleters.BindComplete;
import me.dylan.wands.commandhandler.tabcompleters.TweakSpellComplete;
import me.dylan.wands.commandhandler.tabcompleters.UnbindComplete;
import me.dylan.wands.commandhandler.tabcompleters.WandsComplete;
import me.dylan.wands.config.Config;
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

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.zip.GZIPInputStream;

public final class WandsPlugin extends JavaPlugin {

    public static final boolean DEBUG = true;
    public static final String PREFIX = "§8§l[§6§lWands§8§l]§r ";
    private static final String PREFIX_WARNING = "§e[§e§lWARNING§r] §c";
    private static final Set<Runnable> disableLogic = new HashSet<>();

    private File configFile;
    private ConfigHandler configHandler;
    private CooldownManager cooldownManager;

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

    @Override
    public void onEnable() {
        configFile = new File(getDataFolder(), "config.dat");
        ListenerRegistry listenerRegistry = new ListenerRegistry();
        loadConfig(listenerRegistry);
        loadListeners(listenerRegistry);
        loadCommands();
        log("Enabled successfully");
    }

    @Override
    public void onDisable() {
        configHandler.save(configFile);
        disableLogic.forEach(Runnable::run);
    }

    private void loadConfig(ListenerRegistry listenerRegistry) {
        Config config;
        if (configFile.exists()) {
            try (DataInputStream stream = new DataInputStream(new GZIPInputStream(new FileInputStream(configFile)))) {
                config = new Gson().fromJson(stream.readUTF(), Config.class);
                log("Loaded config");
            } catch (IOException e) {
                throw new IllegalStateException("Config is corrupted");
            }
        } else {
            config = new Config();
            log("Created config");
        }

        this.configHandler = new ConfigHandler(config, listenerRegistry);
        this.cooldownManager = new CooldownManager(configHandler);
    }

    private void loadListeners(ListenerRegistry listenerRegistry) {
        PlayerListener playerListener = new PlayerListener();
        AssassinDagger assassinDagger = new AssassinDagger();
        CursedBow cursedBow = new CursedBow();

        MouseClickListeners mouseClickListeners = new MouseClickListeners();

        mouseClickListeners.addLeftAndRightClickListener(playerListener);
        mouseClickListeners.addRightClickListener(assassinDagger);
        mouseClickListeners.addRightClickListener(cursedBow);

        listenerRegistry.addToggleableListener(
                playerListener,
                assassinDagger,
                cursedBow,
                mouseClickListeners
        );

        if (this.configHandler.isMagicEnabled()) {
            listenerRegistry.enableListeners();
        }
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

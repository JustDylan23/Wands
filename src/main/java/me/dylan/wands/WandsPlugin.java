package me.dylan.wands;

import me.dylan.wands.commandhandler.commands.*;
import me.dylan.wands.commandhandler.tabcompleters.BindComplete;
import me.dylan.wands.commandhandler.tabcompleters.UnbindComplete;
import me.dylan.wands.commandhandler.tabcompleters.WandsComplete;
import me.dylan.wands.config.ConfigurableData;
import me.dylan.wands.customitems.AssassinDagger;
import me.dylan.wands.customitems.CursedBow;
import me.dylan.wands.spell.CooldownManager;
import me.dylan.wands.spell.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

public final class WandsPlugin extends JavaPlugin {

    public static final String PREFIX = "§8§l[§6§lWands§8§l]§r ";
    private static final Set<Runnable> disableLogic = new HashSet<>();
    // instances of classes accessible via main class
    private ConfigurableData configurableData;
    private CooldownManager cooldownManager;

    @SuppressWarnings("WeakerAccess")
    public static void log(String text) {
        Bukkit.getLogger().info(PREFIX + text);
    }

    public static void addDisableLogic(Runnable runnable) {
        disableLogic.add(runnable);
    }

    @Override
    public void onEnable() {
        ListenerRegistry listenerRegistry = new ListenerRegistry();

        this.configurableData = new ConfigurableData(listenerRegistry);
        this.cooldownManager = new CooldownManager(configurableData);

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

        addCommand("wands", new Wands(configurableData, getDescription().getVersion()), new WandsComplete());
        addCommand("createwand", new CreateWand(), null);
        addCommand("clearwand", new ClearWand(), null);
        addCommand("bind", new Bind(), new BindComplete());
        addCommand("unbind", new Unbind(), new UnbindComplete());
        addCommand("bindall", new BindAll(), null);
        addCommand("unbindall", new UnbindAll(), null);

        log("Up and running!");
    }

    @Override
    public void onDisable() {
        saveConfig();
        disableLogic.forEach(Runnable::run);
    }

    private void addCommand(String command, CommandExecutor executor, TabCompleter tabCompleter) {
        PluginCommand cmd = getCommand(command);
        if (cmd == null) throw new NoSuchElementException("Command " + command + " was not found in plugin.yml");
        cmd.setExecutor(executor);
        cmd.setTabCompleter(tabCompleter);
    }

    public ConfigurableData getConfigurableData() {
        return configurableData;
    }

    public CooldownManager getCooldownManager() {
        return cooldownManager;
    }
}

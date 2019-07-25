package me.dylan.wands;

import me.dylan.wands.commandhandler.commands.Bind;
import me.dylan.wands.commandhandler.commands.Unbind;
import me.dylan.wands.commandhandler.commands.UnbindAll;
import me.dylan.wands.commandhandler.commands.Wands;
import me.dylan.wands.commandhandler.tabcompleters.BindComplete;
import me.dylan.wands.commandhandler.tabcompleters.UnbindComplete;
import me.dylan.wands.commandhandler.tabcompleters.WandsComplete;
import me.dylan.wands.customitems.AssassinDagger;
import me.dylan.wands.customitems.CursedBow;
import me.dylan.wands.pluginmeta.ConfigurableData;
import me.dylan.wands.pluginmeta.ListenerRegistry;
import me.dylan.wands.spell.PlayerListener;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;

public final class Main extends JavaPlugin {

    public static final String PREFIX = "§8§l[§6§lWands§8§l]§r ";
    private static Main plugin;
    private final Set<Runnable> disableLogic = new HashSet<>();
    // instances of classes accessible via main class
    private ConfigurableData configurableData;
    private ListenerRegistry listenerRegistry;

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
        addCommand("bind", new Bind(), new BindComplete());
        addCommand("unbind", new Unbind(), new UnbindComplete());
//        addCommand("bindall", new BindAll(), null);
        addCommand("unbindall", new UnbindAll(), null);

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

    private void addCommand(@Nonnull String command, @Nonnull CommandExecutor executor, @Nullable TabCompleter tabCompleter) {
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
}

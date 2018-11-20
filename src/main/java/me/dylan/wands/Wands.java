package me.dylan.wands;

import me.dylan.wands.spells.Comet;
import me.dylan.wands.CommandHandler.CommandArgumentHandler;
import me.dylan.wands.CommandHandler.ConstructTabCompleter;
import me.dylan.wands.spells.PoisonWave;
import me.dylan.wands.spells.Spark;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Wands extends JavaPlugin {

    public static final String PREFIX = ChatColor.translateAlternateColorCodes('&', "&8&l[&6&lWands&8&l]&r ");

    public static boolean ACTIVE = true;

    public static final String VERSION = "1.0.0";

    protected final SpellRegistry spellRegister = new SpellRegistry();

    public void onEnable() {
        sendConsole("Up and running!");
        this.getCommand("wands").setExecutor(new CommandArgumentHandler(this));

        this.getCommand("wands").setTabCompleter(new ConstructTabCompleter(this));

        PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new SpellManager(this.spellRegister), this);
        pluginManager.registerEvents(new GUIs(), this);

        spellRegister.registerSpell(1, new Comet(this));
        spellRegister.registerSpell(2, new Spark(this));
        spellRegister.registerSpell(3, new PoisonWave(this));
    }

    public void onDisable() {
    }

    public static void sendConsole(String text) {
        Bukkit.getConsoleSender().sendMessage(PREFIX + text);
        Bukkit.getLogger().info("");
    }
}

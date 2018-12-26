package me.dylan.wands;

import me.dylan.wands.spells.Comet;
import me.dylan.wands.CommandHandler.CommandArgumentHandler;
import me.dylan.wands.CommandHandler.ConstructTabCompleter;
import me.dylan.wands.spells.PoisonWave;
import me.dylan.wands.spells.Spark;
import me.dylan.wands.spells.TherosDagger;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Wands extends JavaPlugin {

    public static final String PREFIX = ChatColor.translateAlternateColorCodes('&', "&8&l[&6&lWands&8&l]&r ");

    //if this is false, the wands should all stop with working.
    public static boolean ENABLED = true;

    public static final String VERSION = "1.0.0";

    private final SpellRegistry spellRegistery = new SpellRegistry();

    private static Wands plugin;

    public void onEnable() {
        plugin = this;

        sendConsole("Up and running!");
        this.getCommand("wands").setExecutor(new CommandArgumentHandler(this));

        this.getCommand("wands").setTabCompleter(new ConstructTabCompleter(this));

        registerListener(new GUIs(), new SpellManager());

        registerListener(new TherosDagger());
        spellRegistery.registerSpell(1, new Comet(this));
        spellRegistery.registerSpell(2, new Spark(this));
        spellRegistery.registerSpell(3, new PoisonWave(this));
    }

    public void onDisable() {
        plugin = null;
    }

    public static void sendConsole(String text) {
        Bukkit.getConsoleSender().sendMessage(PREFIX + text);
    }

    public void registerListener(Listener... listeners) {
        for (Listener listener : listeners) {
            Bukkit.getServer().getPluginManager().registerEvents(listener, this);
        }
    }

    public static Wands getInstance(){
        return plugin;
    }

    public SpellRegistry getSpellRegistery() {
        return spellRegistery;
    }
}

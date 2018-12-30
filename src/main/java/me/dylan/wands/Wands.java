package me.dylan.wands;

import me.dylan.wands.artifacts.TherosDagger;
import me.dylan.wands.commandhandler.MainCommandHandler;
import me.dylan.wands.commandhandler.ConstructTabCompleter;
import me.dylan.wands.spells.Comet;
import me.dylan.wands.spells.PoisonWave;
import me.dylan.wands.spells.Spark;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class Wands extends JavaPlugin {

    public static final String PREFIX = ChatColor.translateAlternateColorCodes('&', "&8&l[&6&lWands&8&l]&r ");

    //if this is false, the wands should all stop with working.
    public static boolean ENABLED = true;

    public static final String VERSION = "1.0.0";

    private final MainCommandHandler commandArgumentHandler = new MainCommandHandler();
    private final ConstructTabCompleter constructTabCompleter = new ConstructTabCompleter();

    private final SpellRegistry spellRegistry = new SpellRegistry();
    private final WandsRegistry wandsRegistry = new WandsRegistry();

    private static Wands plugin;

    public void onEnable() {
        plugin = this;

        sendConsole("Up and running!");
        this.getCommand("wands").setExecutor(commandArgumentHandler);
        this.getCommand("wands").setTabCompleter(constructTabCompleter);

        registerListener(new GUIs(), new SpellManager());

        AdvancedItemStack empireWand = new AdvancedItemStack(Material.BLAZE_ROD, "&eEmpire Wand");
        wandsRegistry.registerWand(empireWand, 1);

        registerListener(new TherosDagger());
        spellRegistry.registerSpell(1, new Comet());
        spellRegistry.registerSpell(2, new Spark());
        spellRegistry.registerSpell(3, new PoisonWave());
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

    public static Wands getInstance() {
        return plugin;
    }



    public SpellRegistry getSpellRegistry() {
        return spellRegistry;
    }
}

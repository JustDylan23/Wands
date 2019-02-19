package me.dylan.wands;

import me.dylan.wands.artifacts.EmpireBow;
import me.dylan.wands.artifacts.TherosDagger;
import me.dylan.wands.commandhandler.ConstructTabCompleter;
import me.dylan.wands.commandhandler.MainCommandHandler;
import me.dylan.wands.spells.Comet;
import me.dylan.wands.spells.Launch;
import me.dylan.wands.spells.PoisonWave;
import me.dylan.wands.spells.Spark;
import net.minecraft.server.v1_13_R2.ChatComponentText;
import net.minecraft.server.v1_13_R2.ChatMessageType;
import net.minecraft.server.v1_13_R2.PacketPlayOutChat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_13_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

@SuppressWarnings("WeakerAccess")
public final class Wands extends JavaPlugin {

    public static final String PREFIX = ChatColor.translateAlternateColorCodes('&', "&8&l[&6&lWands&8&l]&r ");

    //if this is false, the wands should all stop with working.
    private boolean STATUS = true;

    public static final String VERSION = "1.0.0";

    private final MainCommandHandler commandArgumentHandler = new MainCommandHandler();
    private final ConstructTabCompleter constructTabCompleter = new ConstructTabCompleter();

    private final SpellRegistry spellRegistry = new SpellRegistry();

    private static Wands plugin;

    public void onEnable() {
        plugin = this;

        sendConsole("Up and running!");
        this.getCommand("wands").setExecutor(commandArgumentHandler);
        this.getCommand("wands").setTabCompleter(constructTabCompleter);

        registerListener(new GUIs(), new SpellManager());

        registerListener(new TherosDagger(), new EmpireBow());

        spellRegistry.registerSpell(1, Comet.getInstance());
        spellRegistry.registerSpell(2, Spark.getInstance());
        spellRegistry.registerSpell(3, Launch.getInstance());
        spellRegistry.registerSpell(4, PoisonWave.getInstance());
    }

    public void onDisable() {
        plugin = null;
    }

    public static void sendConsole(String text) {
        Bukkit.getConsoleSender().sendMessage(PREFIX + text);
    }

    public static void sendActionBar(Player player, String message) {
        CraftPlayer craftPlayer = (CraftPlayer) player;
        if (craftPlayer.getHandle().playerConnection != null && message != null && !message.isEmpty()) {
            craftPlayer.getHandle().playerConnection.sendPacket(new PacketPlayOutChat(new ChatComponentText(message), ChatMessageType.GAME_INFO));
        }
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

    public static void disable() {
        getInstance().STATUS = false;
    }

    public static void enable() {
        getInstance().STATUS = true;
    }

    public static void toggleStatus() {
        getInstance().STATUS = !getInstance().STATUS;
    }

    public static boolean getStatus() {
        return getInstance().STATUS;
    }
}

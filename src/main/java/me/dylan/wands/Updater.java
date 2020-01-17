package me.dylan.wands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public final class Updater {
    private static final int ID = 71154;
    public static final String URL_DOWNLOAD = "https://api.spiget.org/v2/resources/" + ID + "/download";
    private static final String URL_VERSION = "https://api.spigotmc.org/legacy/update.php?resource=" + ID;
    private static final Plugin plugin = WandsPlugin.getInstance();

    private Updater() {
    }

    @NotNull
    public static CompletableFuture<String> getLatestVersionString() {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(new URL(URL_VERSION).openConnection().getInputStream()))) {
                completableFuture.complete(in.readLine());
            } catch (IOException e) {
                completableFuture.completeExceptionally(e);
            }
        });

        return completableFuture;
    }

    public static void checkForUpdates(BukkitTask bukkitTask) {
        String currentVersion = WandsPlugin.getInstance().getDescription().getVersion();
        getLatestVersionString().whenCompleteAsync((fetchedVersion, throwable) -> {
            if (throwable == null) {
                if (!currentVersion.equals(fetchedVersion)) {
                    bukkitTask.cancel();
                    String message = "Version §a" + fetchedVersion + "§r is available (current version §c" + currentVersion + "§r) run \"/wands update install\" to update";
                    String playerMessage = WandsPlugin.PREFIX_TOP + message;
                    Bukkit.getOnlinePlayers().stream().filter(p -> p.hasPermission("wands.update")).forEach(p -> p.sendMessage(playerMessage));
                    WandsPlugin.log(message);
                }
            }
        });
    }

    public static void checkForUpdates(@NotNull CommandSender sender, boolean install) {
        String currentVersion = WandsPlugin.getInstance().getDescription().getVersion();
        sender.sendMessage(WandsPlugin.PREFIX_TOP + "Checking for updates...");
        getLatestVersionString().whenCompleteAsync((fetchedVersion, throwable) -> {
            if (throwable != null) {
                sender.sendMessage("Failed to connect");
            } else if (currentVersion.equals(fetchedVersion)) {
                sender.sendMessage("No updates available");
            } else {
                sender.sendMessage("Version §a" + fetchedVersion + "§r is available (current version §c" + currentVersion + "§r)");
                if (install) {
                    install();
                } else {
                    sender.sendMessage("Run \"/wands update install\" to update");
                }
            }
        });
    }

    private static void install() {
        Bukkit.broadcastMessage(WandsPlugin.PREFIX + "Restarting server, installing update");
    }
}

package me.dylan.wands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.concurrent.CompletableFuture;

public final class Updater implements Listener {
    private static final int ID = 71154;
    private static final String URL_DOWNLOAD = "https://api.spiget.org/v2/resources/" + ID + "/download";
    private static final String URL_VERSION = "https://api.spigotmc.org/legacy/update.php?resource=" + ID;
    private final WandsPlugin plugin;
    private final String updateFileName;

    Updater(String updateFileName, WandsPlugin plugin) {
        ListenerRegistry.addListener(this);
        this.updateFileName = updateFileName;
        this.plugin = plugin;
    }

    @NotNull
    private CompletableFuture<String> getLatestVersionString() {
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

    void checkForUpdates(BukkitTask bukkitTask) {
        String currentVersion = WandsPlugin.getInstance().getDescription().getVersion();
        getLatestVersionString().whenCompleteAsync((fetchedVersion, throwable) -> {
            if (plugin.getConfigHandler().areNotificationsEnabled() && throwable == null && !currentVersion.equals(fetchedVersion)) {
                bukkitTask.cancel();
                String message = WandsPlugin.PREFIX_TOP + getNewVersionMessage(currentVersion, fetchedVersion) + getInstallInstructionMessage(true);
                Bukkit.getOnlinePlayers().stream().filter(p -> p.hasPermission("wands.update.install")).forEach(p -> p.sendMessage(message));
                WandsPlugin.log(getNewVersionMessage(currentVersion, fetchedVersion) + getInstallInstructionMessage(false));
            }
        });
    }

    public void checkForUpdates(@NotNull CommandSender sender, boolean install) {
        String currentVersion = WandsPlugin.getInstance().getDescription().getVersion();
        sender.sendMessage(WandsPlugin.PREFIX_TOP + "Checking for updates...");
        getLatestVersionString().whenComplete((fetchedVersion, throwable) -> {
            if (throwable != null) {
                sender.sendMessage("Failed to connect");
            } else if (currentVersion.equals(fetchedVersion)) {
                sender.sendMessage("No updates available");
            } else {
                sender.sendMessage(getNewVersionMessage(currentVersion, fetchedVersion));
                if (install) {
                    sender.sendMessage("Installing update");
                    install();
                } else {
                    sender.sendMessage(getInstallInstructionMessage(false));
                }
            }
        });
    }

    private void install() {
        Bukkit.getUpdateFolderFile().mkdirs();
        File file = new File(Bukkit.getUpdateFolderFile(), updateFileName);
        try (
                ReadableByteChannel readableByteChannel = Channels.newChannel(new URL(URL_DOWNLOAD).openStream());
                FileOutputStream fileOutputStream = new FileOutputStream(file)
        ) {
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        } catch (IOException e) {
            WandsPlugin.log(e.toString());
        }
        Bukkit.broadcastMessage(WandsPlugin.PREFIX + "Reloading server to complete installation...");
        Bukkit.getScheduler().runTask(plugin, () -> {
            Bukkit.getServer().reload();
            Bukkit.broadcastMessage(WandsPlugin.PREFIX + "Finished installing update");
        });
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (plugin.getConfigHandler().areNotificationsEnabled() && player.hasPermission("wands.update.install")) {
            String currentVersion = WandsPlugin.getInstance().getDescription().getVersion();
            getLatestVersionString().whenComplete((fetchedVersion, throwable) -> {
                if (throwable == null && !currentVersion.equals(fetchedVersion)) {
                    player.sendMessage(WandsPlugin.PREFIX_TOP + getNewVersionMessage(currentVersion, fetchedVersion) + getInstallInstructionMessage(true));
                }
            });
        }
    }

    private String getNewVersionMessage(String currentVersion, String newVersion) {
        return "Version §a" + newVersion + "§r is available (current version §c" + currentVersion + "§r)";
    }

    private String getInstallInstructionMessage(boolean newLine) {
        return (newLine ? "\n" : "") + "Please run \"/wands update install\" to update";
    }
}

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
                String message = WandsPlugin.PREFIX_TOP + getNewVersionMessage(currentVersion, fetchedVersion) + getDownloadInstructionMessage(true);
                Bukkit.getOnlinePlayers().stream().filter(p -> p.hasPermission("wands.update.download")).forEach(p -> p.sendMessage(message));
                WandsPlugin.log(getNewVersionMessage(currentVersion, fetchedVersion) + " " + getDownloadInstructionMessage(false));
            }
        });
    }

    public void checkForUpdates(@NotNull CommandSender sender, boolean install) {
        String currentVersion = WandsPlugin.getInstance().getDescription().getVersion();
        getLatestVersionString().whenComplete((fetchedVersion, throwable) -> {
            if (throwable != null) {
                sender.sendMessage(WandsPlugin.PREFIX + "Failed to check for updates");
            } else if (currentVersion.equals(fetchedVersion)) {
                sender.sendMessage(WandsPlugin.PREFIX + "Already up to date!");
            } else {
                if (install) {
                    install(sender, currentVersion, fetchedVersion);
                } else {
                    sender.sendMessage(WandsPlugin.PREFIX_TOP + getNewVersionMessage(currentVersion, fetchedVersion) + getDownloadInstructionMessage(true));
                }
            }
        });
    }

    private void install(CommandSender sender, String currentVer, String newVer) {
        //noinspection ResultOfMethodCallIgnored
        Bukkit.getUpdateFolderFile().mkdirs();
        File file = new File(Bukkit.getUpdateFolderFile(), updateFileName);
        if (file.exists()) {
            sender.sendMessage(WandsPlugin.PREFIX + "§cPlease complete the pending update first\nby restarting/reloading");
            return;
        }
        try (
                ReadableByteChannel readableByteChannel = Channels.newChannel(new URL(URL_DOWNLOAD).openStream());
                FileOutputStream fileOutputStream = new FileOutputStream(file)
        ) {
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
        } catch (IOException e) {
            WandsPlugin.log(e.toString());
        }
        sender.sendMessage(WandsPlugin.PREFIX_TOP + "Finished downloading §av" + newVer + "§r (current §cv" + currentVer + "§r)\nTo complete the installation,\nplease restart/reload the server");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        if (plugin.getConfigHandler().areNotificationsEnabled() && player.hasPermission("wands.update.download")) {
            String currentVersion = WandsPlugin.getInstance().getDescription().getVersion();
            getLatestVersionString().whenComplete((fetchedVersion, throwable) -> {
                if (throwable == null && !currentVersion.equals(fetchedVersion)) {
                    player.sendMessage(WandsPlugin.PREFIX_TOP + getNewVersionMessage(currentVersion, fetchedVersion) + getDownloadInstructionMessage(true));
                }
            });
        }
    }

    private String getNewVersionMessage(String currentVersion, String newVersion) {
        return "§av" + newVersion + "§r is available (current §cv" + currentVersion + "§r)";
    }

    private String getDownloadInstructionMessage(boolean newLine) {
        return (newLine ? "\n" : "") + "Run \"/wands update download\" to download the update";
    }
}

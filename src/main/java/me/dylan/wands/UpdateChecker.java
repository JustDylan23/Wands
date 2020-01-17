package me.dylan.wands;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public final class UpdateChecker {
    private static final int ID = 71154;
    public static final String RESOURCE = "https://www.spigotmc.org/resources/" + ID + "/updates";
    private static final String URL = "https://api.spigotmc.org/legacy/update.php?resource=" + ID;
    private static final Plugin plugin = WandsPlugin.getInstance();

    private UpdateChecker() {
    }

    @NotNull
    public static CompletableFuture<String> getLatestVersionString() {
        CompletableFuture<String> completableFuture = new CompletableFuture<>();

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try (BufferedReader in = new BufferedReader(new InputStreamReader(new URL(URL).openConnection().getInputStream()))) {
                completableFuture.complete(in.readLine());
            } catch (IOException e) {
                completableFuture.completeExceptionally(e);
            }
        });

        return completableFuture;
    }
}

package me.dylan.wands.utils;

import me.dylan.wands.WandsPlugin;
import org.bukkit.Bukkit;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.Metadatable;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

@SuppressWarnings("WeakerAccess")
public final class Common {
    private static final WandsPlugin plugin = WandsPlugin.getInstance();
    private static final FixedMetadataValue METADATA_VALUE_TRUE = new FixedMetadataValue(plugin, true);
    private static final Consumer<?> EMPTY_CONSUMER = t -> {
    };
    private static final BiConsumer<?, ?> EMPTY_BI_CONSUMER = (t, u) -> {
    };
    private static final Predicate<?> EMPTY_PREDICATE = t -> true;

    private Common() {
    }

    @NotNull
    public static String pascalCaseToWords(@NotNull String input) {
        return input.replaceAll("[A-Z]", " $0").trim();
    }

    @SuppressWarnings("unchecked")
    public static <T> Consumer<T> emptyConsumer() {
        return (Consumer<T>) EMPTY_CONSUMER;
    }

    @SuppressWarnings("unchecked")
    public static <T, U> BiConsumer<T, U> emptyBiConsumer() {
        return (BiConsumer<T, U>) EMPTY_BI_CONSUMER;
    }

    @SuppressWarnings("unchecked")
    public static <T> Predicate<T> emptyPredicate() {
        return (Predicate<T>) EMPTY_PREDICATE;
    }

    public static void runTaskLater(Runnable runnable, long delay) {
        Bukkit.getScheduler().runTaskLater(plugin, runnable, delay);
    }

    public static void runRepeatableTaskLater(Runnable runnable, @NotNull int... delays) {
        int delay = 0;
        for (int i : delays) {
            delay += i;
            Bukkit.getScheduler().runTaskLater(plugin, runnable, delay);
        }
    }

    public static void runTaskTimer(@NotNull BukkitRunnable bukkitRunnable, long delay, long period) {
        bukkitRunnable.runTaskTimer(plugin, delay, period);
    }

    public static FixedMetadataValue metadataValue(Object object) {
        return new FixedMetadataValue(plugin, object);
    }

    public static FixedMetadataValue getMetadataValueTrue() {
        return METADATA_VALUE_TRUE;
    }

    public static void removeMetaData(@NotNull Metadatable metadatable, String metaKey) {
        metadatable.removeMetadata(metaKey, plugin);
    }
}

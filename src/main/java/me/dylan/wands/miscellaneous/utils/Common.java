package me.dylan.wands.miscellaneous.utils;

import me.dylan.wands.Main;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;

public final class Common {
    private static final Main plugin = Main.getPlugin();
    public static final FixedMetadataValue METADATA_VALUE_TRUE = new FixedMetadataValue(plugin, true);
    private static final Consumer<?> EMPTY_CONSUMER = t -> {
    };
    private static final BiConsumer<?, ?> EMPTY_BI_CONSUMER = (t, u) -> {
    };
    private static final Predicate<?> EMPTY_PREDICATE = t -> true;

    private Common() {
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
}

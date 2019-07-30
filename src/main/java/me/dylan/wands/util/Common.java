package me.dylan.wands.util;

import me.dylan.wands.Main;
import org.bukkit.metadata.FixedMetadataValue;
import org.jetbrains.annotations.Contract;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Common {
    private final static Main plugin = Main.getPlugin();
    public static final FixedMetadataValue METADATA_VALUE_TRUE = new FixedMetadataValue(plugin, true);
    private final static Consumer<?> EMPTY_CONSUMER = t -> {
    };
    private final static BiConsumer<?, ?> EMPTY_BI_CONSUMER = (t, u) -> {
    };

    @Contract(pure = true)
    @SuppressWarnings("unchecked")
    public static <T> Consumer<T> emptyConsumer() {
        return (Consumer<T>) EMPTY_CONSUMER;
    }

    @Contract(pure = true)
    @SuppressWarnings("unchecked")
    public static <T, U> BiConsumer<T, U> emptyBiConsumer() {
        return (BiConsumer<T, U>) EMPTY_BI_CONSUMER;
    }
}

package me.dylan.wands.util;

import me.dylan.wands.Main;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.function.Consumer;

public class Common {
    private final static Main plugin = Main.getPlugin();
    public static final FixedMetadataValue METADATA_VALUE_TRUE = new FixedMetadataValue(plugin, true);
    private final static Consumer<?> EMPTY_CONSUMER = e -> {
    };

    @SuppressWarnings("unchecked")
    public static <T> Consumer<T> emptyConsumer() {
        return (Consumer<T>) EMPTY_CONSUMER;
    }
}

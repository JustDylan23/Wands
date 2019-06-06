package me.dylan.wands.util;

import me.dylan.wands.Main;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.function.Consumer;

public class ShorthandUtil {
    private final static Main plugin = Main.getPlugin();

    private final static Consumer<?> EMPTY_CONSUMER = e -> {
    };

    public static final FixedMetadataValue METADATA_VALUE_TRUE = new FixedMetadataValue(plugin, true);

    @SuppressWarnings("unchecked")
    public static <T> Consumer<T> emptyConsumer() {
        return (Consumer<T>) EMPTY_CONSUMER;
    }
}

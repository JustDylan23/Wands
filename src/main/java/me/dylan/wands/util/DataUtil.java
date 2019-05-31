package me.dylan.wands.util;

import java.util.function.Consumer;

public class DataUtil {
    private final static Consumer<?> EMPTY_CONSUMER = e -> {
    };

    @SuppressWarnings("unchecked")
    public static <T> Consumer<T> emptyConsumer() {
        return (Consumer<T>) EMPTY_CONSUMER;
    }
}

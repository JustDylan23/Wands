package me.dylan.wands.commandhandler;

import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseCompleter implements TabCompleter {
    protected List<String> validCompletions(@NotNull String toComplete, String... values) {
        String toCompleteLowered = toComplete.toLowerCase();
        return Arrays.stream(values)
                .map(String::toLowerCase)
                .filter(s -> s.contains(toCompleteLowered))
                .collect(Collectors.toList());
    }
}

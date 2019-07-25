package me.dylan.wands.commandhandler;

import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public abstract class BaseCompleter implements TabCompleter {
    protected List<String> validCompletions(String value, String... values) {
        return Arrays.stream(values)
                .filter(s -> s.toLowerCase().contains(value))
                .map(String::toLowerCase)
                .collect(Collectors.toList());
    }
}

package me.dylan.wands.commandhandler;

import me.dylan.wands.InGameItems;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ConstructTabCompleter implements TabCompleter {

    private List<String> validCompletions(String value, String[] values) {
        return Arrays.stream(values)
                .filter(c -> c.startsWith(value))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        String value = args[args.length - 1].toLowerCase();
        switch (args.length) {
            case 1:
                return validCompletions(value, Completions.DEFAULT.completions);
            case 2:
                switch (args[0]) {
                    case "get":
                        return Arrays.stream(InGameItems.values()).map(Enum::toString).collect(Collectors.toList());
                    case "set":
                        return validCompletions(value, Completions.SET.completions);
                }
        }
        return null;
    }

    private enum Completions {
        DEFAULT("reload", "enable", "disable", "update", "help", "get", "set"),
        SET("cooldown");

        final String[] completions;

        Completions(String... completions) {
            this.completions = completions;
        }
    }
}

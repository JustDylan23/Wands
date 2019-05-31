package me.dylan.wands.commandhandler;

import me.dylan.wands.pluginmeta.ObtainableItem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MainTabCompleter implements TabCompleter {

    private List<String> validCompletions(String value, String... values) {
        return Arrays.stream(values)
                .filter(s -> s.startsWith(value))
                .collect(Collectors.toList());
    }

    @Override
    public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String alias, @Nonnull String[] args) {
        String value = args[args.length - 1];
        if (args.length == 1)
            return validCompletions(value, "enable", "disable", "get", "set", "info", "spells", "getconfig");
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("get"))
                return validCompletions(value, ObtainableItem.getNames());
            if (args[0].equalsIgnoreCase("set"))
                return validCompletions(value, "cooldown");
        }
        return Collections.emptyList();
    }
}

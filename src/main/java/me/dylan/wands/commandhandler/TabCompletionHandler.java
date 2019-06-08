package me.dylan.wands.commandhandler;

import me.dylan.wands.pluginmeta.ObtainableItem;
import me.dylan.wands.spell.SpellType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TabCompletionHandler implements TabCompleter {

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
        else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("spells"))
                return validCompletions(value, "props");
            if (args[0].equalsIgnoreCase("get"))
                return validCompletions(value, ObtainableItem.getNames());
            if (args[0].equalsIgnoreCase("set"))
                return validCompletions(value, "cooldown");
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("spells")) {
                if (args[1].equalsIgnoreCase("props")) {
                    String[] str = Arrays.stream(SpellType.values()).map(Enum::toString).toArray(String[]::new);
                    return validCompletions(value, str);
                }
            }
        }
        return Collections.emptyList();
    }
}

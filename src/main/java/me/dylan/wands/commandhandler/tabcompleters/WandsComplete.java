package me.dylan.wands.commandhandler.tabcompleters;

import me.dylan.wands.PreSetItem;
import me.dylan.wands.commandhandler.BaseCompleter;
import me.dylan.wands.spell.SpellType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WandsComplete extends BaseCompleter {

    private final String[] itemNames;

    public WandsComplete() {
        PreSetItem[] items = PreSetItem.values();
        itemNames = new String[items.length];
        for (int i = 0; i < items.length; i++) {
            itemNames[i] = items[i].toString();
        }
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        String value = args[args.length - 1];
        if (args.length == 1)
            return validCompletions(value, "enable", "disable", "get", "set", "info", "spells", "getconfig", "inspect", "update");
        else if (args.length == 2) {
            if ("update".equalsIgnoreCase(args[0])) {
                return validCompletions(value, "download");
            }
            if ("spells".equalsIgnoreCase(args[0])) {
                String[] completions = Arrays.stream(SpellType.values()).map(Enum::toString).toArray(String[]::new);
                return validCompletions(value, completions);
            }
            if ("get".equalsIgnoreCase(args[0]))
                return validCompletions(value, itemNames);
            if ("set".equalsIgnoreCase(args[0]))
                return validCompletions(value, "cooldown", "restriction", "notifications");
        } else if (args.length == 3) {
            if ("set".equalsIgnoreCase(args[0]))
                if ("restriction".equalsIgnoreCase(args[1]) || "notifications".equalsIgnoreCase(args[1]))
                    return validCompletions(value, "true", "false");
        }
        return Collections.emptyList();
    }
}

package me.dylan.wands.commandhandler.tabcompleters;

import me.dylan.wands.PreSetItem;
import me.dylan.wands.commandhandler.CommandUtils;
import me.dylan.wands.spell.SpellType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WandsComplete implements TabCompleter {

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
        if (args.length == 1) {
            return CommandUtils.validCompletions(value, "enable", "disable", "get", "getscroll", "set", "info", "spells", "settings", "inspect", "update");
        }
        if (args.length == 2) {
            if ("update".equalsIgnoreCase(args[0])) {
                return CommandUtils.validCompletions(value, "download");
            }
            if ("spells".equalsIgnoreCase(args[0]) || "getscroll".equalsIgnoreCase(args[0])) {
                String[] completions = Arrays.stream(SpellType.values()).map(Enum::toString).toArray(String[]::new);
                return CommandUtils.validCompletions(value, completions);
            }
            if ("get".equalsIgnoreCase(args[0])) {
                return CommandUtils.validCompletions(value, itemNames);
            }
            if ("settings".equalsIgnoreCase(args[0])) {
                return CommandUtils.validCompletions(value, "cooldown", "restriction", "notifications");
            }
        } else if (args.length == 3 && "set".equalsIgnoreCase(args[0]) && ("restriction".equalsIgnoreCase(args[1]) || "notifications".equalsIgnoreCase(args[1]))) {
            return CommandUtils.validCompletions(value, "true", "false");
        }
        return Collections.emptyList();
    }
}

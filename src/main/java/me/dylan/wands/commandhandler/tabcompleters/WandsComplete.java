package me.dylan.wands.commandhandler.tabcompleters;

import me.dylan.wands.PreSetItem;
import me.dylan.wands.commandhandler.BaseCompleter;
import me.dylan.wands.spell.SpellInstance;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class WandsComplete extends BaseCompleter {

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        String value = args[args.length - 1];
        if (args.length == 1)
            return validCompletions(value, "enable", "disable", "get", "set", "info", "spells", "getconfig");
        else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("spells")) {
                String[] str = Arrays.stream(SpellInstance.values()).map(Enum::toString).toArray(String[]::new);
                return validCompletions(value, str);
            }
            if (args[0].equalsIgnoreCase("get"))
                return validCompletions(value, PreSetItem.getNames());
            if (args[0].equalsIgnoreCase("set"))
                return validCompletions(value, "cooldown", "selfharm", "restriction");
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("set"))
                if (args[1].equalsIgnoreCase("selfharm") || args[1].equalsIgnoreCase("restriction"))
                    return validCompletions(value, "true", "false");
        }
        return Collections.emptyList();
    }
}

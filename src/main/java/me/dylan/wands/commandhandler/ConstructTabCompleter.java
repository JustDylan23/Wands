package me.dylan.wands.commandhandler;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConstructTabCompleter implements TabCompleter {
    private final List<String> completions = new ArrayList<>();

    private void setCompletions(String[] strings) {
        Collections.addAll(completions, strings);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        completions.clear();
        if (command.getName().equalsIgnoreCase("wands")) {
            if (args.length == 1) {
                setCompletions(new String[]{"info", "set", "disable", "enable", "dropondeath", "spells", "permission"});
                return completions;
            }
        }
        return completions;
    }
}

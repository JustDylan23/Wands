package me.dylan.wands.CommandHandler;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;

public class ConstructTabCompleter implements TabCompleter {

    private final Plugin plugin;

    private List<String> completions = new ArrayList<>();

    public ConstructTabCompleter
            (Plugin plugin) {
        this.plugin = plugin;
    }

    private void setCompletions(String[] strings) {
        for (String string : strings) {
            completions.add(string);
        }
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

package me.dylan.wands.commandhandler.tabcompleters;

import me.dylan.wands.commandhandler.CommandUtils;
import me.dylan.wands.spell.SpellType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class TweakSpellComplete implements TabCompleter {
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        String value = args[args.length - 1];
        if (args.length == 1) {
            SpellType[] spellTypes = SpellType.values();
            String[] completions = new String[spellTypes.length];
            for (int i = 0; i < spellTypes.length; i++) {
                completions[i] = spellTypes[i].toString();
            }
            return CommandUtils.validCompletions(value, completions);
        }
        return Collections.emptyList();
    }
}


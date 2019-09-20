package me.dylan.wands.commandhandler.tabcompleters;

import me.dylan.wands.commandhandler.BaseCompleter;
import me.dylan.wands.spell.SpellType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class TweakSpellComplete extends BaseCompleter {
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        String value = args[args.length - 1];
        if (args.length == 1) {
            SpellType[] spellTypes = SpellType.values();
            String[] completions = new String[spellTypes.length];
            for (int i = 0; i < spellTypes.length; i++) {
                completions[i] = spellTypes[i].toString();
            }
            return validCompletions(value, completions);
        }
        return Collections.emptyList();
    }
}


package me.dylan.wands.commandhandler.tabcompleters;

import me.dylan.wands.commandhandler.BaseCompleter;
import me.dylan.wands.spell.SpellCompound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class UnbindComplete extends BaseCompleter {
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1 && sender instanceof Player) {
            String[] completions = new SpellCompound(((Player) sender).getInventory().getItemInMainHand())
                    .getSpells()
                    .stream()
                    .map(Objects::toString)
                    .toArray(String[]::new);

            return validCompletions(args[0], completions);
        }
        return Collections.emptyList();
    }
}
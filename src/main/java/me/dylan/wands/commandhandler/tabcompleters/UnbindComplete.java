package me.dylan.wands.commandhandler.tabcompleters;

import me.dylan.wands.commandhandler.BaseCompleter;
import me.dylan.wands.spell.SpellManagementUtil.SpellCompoundUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.List;

public class UnbindComplete extends BaseCompleter {
    @Override
    public List<String> onTabComplete(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String alias, @Nonnull String[] args) {
        if (args.length == 1 && sender instanceof Player) {
            return validCompletions(args[0], (SpellCompoundUtil.getSpells(((Player) sender).getInventory().getItemInMainHand())).toArray(new String[0]));
        }
        return Collections.emptyList();
    }
}
package me.dylan.wands.commandhandler.tabcompleters;

import me.dylan.wands.commandhandler.BaseCompleter;
import me.dylan.wands.spell.SpellType;
import me.dylan.wands.spell.SpellManagementUtil;
import me.dylan.wands.spell.SpellManagementUtil.SpellCompoundUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BindComplete extends BaseCompleter {
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1 && sender instanceof Player) {
            String value = args[0];
            ItemStack itemStack = ((Player) sender).getInventory().getItemInMainHand();
            if (SpellManagementUtil.isWand(itemStack)) {
                List<String> spells = SpellCompoundUtil.getSpells(itemStack);
                String[] str = Arrays.stream(SpellType.values())
                        .map(Enum::toString).filter(s -> !spells.contains(s))
                        .toArray(String[]::new);
                return validCompletions(value, str);
            }
        }
        return Collections.emptyList();
    }
}


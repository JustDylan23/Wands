package me.dylan.wands.commandhandler.tabcompleters;

import me.dylan.wands.commandhandler.BaseCompleter;
import me.dylan.wands.spell.SpellCompound;
import me.dylan.wands.spell.SpellManagementUtil;
import me.dylan.wands.spell.SpellType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class BindComplete extends BaseCompleter {
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1 && sender instanceof Player) {
            String value = args[0];
            ItemStack itemStack = ((Player) sender).getInventory().getItemInMainHand();
            if (SpellManagementUtil.isWand(itemStack)) {
                List<SpellType> bound = new SpellCompound(itemStack).getSpells();
                List<SpellType> unbound = new ArrayList<>(Arrays.asList(SpellType.values()));
                unbound.removeAll(bound);

                String[] completions = unbound.stream().map(Objects::toString).toArray(String[]::new);

                return validCompletions(value, completions);
            }
        }
        return Collections.emptyList();
    }
}


package me.dylan.wands.commandhandler.tabcompleters;

import me.dylan.wands.commandhandler.BaseCompleter;
import me.dylan.wands.spell.SpellCompound;
import me.dylan.wands.spell.SpellType;
import me.dylan.wands.spell.accessories.ItemTag;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class BindComplete extends BaseCompleter {
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1 && sender instanceof Player) {
            String value = args[0];
            ItemStack itemStack = ((Player) sender).getInventory().getItemInMainHand();
            if (ItemTag.IS_WAND.isTagged(itemStack)) {
                List<SpellType> compound = Arrays.asList(SpellType.values());
                compound.removeAll(SpellCompound.getCompound(itemStack));
                return validCompletions(value, compound.stream().map(Objects::toString).toArray(String[]::new));
            }
        }
        return Collections.emptyList();
    }
}


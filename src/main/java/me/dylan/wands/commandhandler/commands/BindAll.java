package me.dylan.wands.commandhandler.commands;

import me.dylan.wands.WandsPlugin;
import me.dylan.wands.commandhandler.CommandUtils;
import me.dylan.wands.spell.SpellCompound;
import me.dylan.wands.spell.SpellType;
import me.dylan.wands.utils.ItemUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Set;

public class BindAll implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (CommandUtils.isPlayerOrNotify(sender)) {
            Player player = (Player) sender;
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            if (CommandUtils.isWandOrNotify(sender, itemStack)) {
                Set<SpellType> compound = SpellCompound.getCompound(itemStack);
                compound.addAll(Arrays.asList(SpellType.values()));
                SpellCompound.apply(compound, itemStack);
                sender.sendMessage(WandsPlugin.PREFIX + "Successfully added all spells to " + ItemUtil.getName(itemStack));
            }
        }
        return true;
    }
}

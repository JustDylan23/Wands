package me.dylan.wands.commandhandler.commands;

import me.dylan.wands.Main;
import me.dylan.wands.commandhandler.BaseCommand;
import me.dylan.wands.spell.SpellManagementUtil.SpellCompoundUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class BindAll extends BaseCommand {
    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command cmd, @Nonnull String label, @Nonnull String[] args) {
        if (isPlayer(sender)) {
            ItemStack itemStack = ((Player) sender).getInventory().getItemInMainHand();
            if (isWand(sender, itemStack)) {
                SpellCompoundUtil.addAllSpells(itemStack);
                sender.sendMessage(Main.PREFIX + "Successfully added all spells to " + itemStack.getItemMeta().getDisplayName());
            }
        }
        return true;
    }
}

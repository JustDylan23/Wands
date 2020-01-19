package me.dylan.wands.commandhandler.commands;

import me.dylan.wands.WandsPlugin;
import me.dylan.wands.commandhandler.CommandUtils;
import me.dylan.wands.spell.util.SpellInteractionUtil;
import me.dylan.wands.utils.ItemUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ClearWand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (CommandUtils.isPlayerOrNotify(sender)) {
            Player player = (Player) sender;
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            if (CommandUtils.isWandOrNotify(sender, itemStack)) {
                SpellInteractionUtil.undoWand(itemStack);
                ItemUtil.setName(itemStack, "");
                sender.sendMessage(WandsPlugin.PREFIX + "Successfully reverted " + itemStack.getType() + " to initial state");
            }
        }
        return true;
    }
}

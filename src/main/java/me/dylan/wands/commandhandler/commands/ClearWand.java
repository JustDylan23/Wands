package me.dylan.wands.commandhandler.commands;

import me.dylan.wands.WandsPlugin;
import me.dylan.wands.commandhandler.BaseCommand;
import me.dylan.wands.spell.util.SpellInteractionUtil;
import me.dylan.wands.utils.ItemUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ClearWand extends BaseCommand {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (isPlayer(sender)) {
            Player player = (Player) sender;
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            if (isWand(sender, itemStack)) {
                SpellInteractionUtil.undoWand(itemStack);
                ItemUtil.setName(itemStack, "");
                sender.sendMessage(WandsPlugin.PREFIX + "Successfully reverted " + itemStack.getType() + " to initial state");
            }
        }
        return true;
    }
}

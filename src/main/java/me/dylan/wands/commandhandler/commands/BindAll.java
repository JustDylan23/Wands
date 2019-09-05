package me.dylan.wands.commandhandler.commands;

import me.dylan.wands.Main;
import me.dylan.wands.commandhandler.BaseCommand;
import me.dylan.wands.spell.tools.SpellCompound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class BindAll extends BaseCommand {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (isPlayer(sender)) {
            Player player = (Player) sender;
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            if (isWand(sender, itemStack)) {
                SpellCompound compound = new SpellCompound(itemStack);
                compound.addAll();
                compound.apply(itemStack);
                sender.sendMessage(Main.PREFIX + "Successfully added all spells to " + itemStack.getItemMeta().getDisplayName());
            }
        }
        return true;
    }
}

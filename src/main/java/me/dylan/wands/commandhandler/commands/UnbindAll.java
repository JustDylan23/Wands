package me.dylan.wands.commandhandler.commands;

import me.dylan.wands.WandsPlugin;
import me.dylan.wands.commandhandler.BaseCommand;
import me.dylan.wands.spell.SpellCompound;
import me.dylan.wands.spell.SpellType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class UnbindAll extends BaseCommand {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (isPlayer(sender)) {
            Player player = (Player) sender;
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            if (isWand(sender, itemStack)) {
                String itemName = itemStack.getItemMeta().getDisplayName();
                Set<SpellType> compound = SpellCompound.getCompound(itemStack);
                if (!compound.isEmpty()) {
                    compound.clear();
                    SpellCompound.apply(compound, itemStack);
                    sender.sendMessage(WandsPlugin.PREFIX + "Successfully removed all spells from " + itemName);
                } else {
                    sender.sendMessage(WandsPlugin.PREFIX + itemName + " §ris already empty!");
                }
            }
        }
        return true;
    }
}

package me.dylan.wands.commandhandler.commands;

import me.dylan.wands.WandsPlugin;
import me.dylan.wands.commandhandler.BaseCommand;
import me.dylan.wands.spell.SpellType;
import me.dylan.wands.spell.tools.SpellCompound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Unbind extends BaseCommand {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (isPlayer(sender)) {
            if (args.length == 1) {
                Player player = (Player) sender;
                String argument = args[0].toUpperCase();
                ItemStack itemStack = player.getInventory().getItemInMainHand();
                if (isWand(player, itemStack)) {
                    SpellType spellType = SpellType.getSpellType(argument);
                    if (spellType != null) {
                        String itemName = itemStack.getItemMeta().getDisplayName();
                        SpellCompound compound = new SpellCompound(itemStack);
                        if (compound.remove(spellType)) {
                            compound.apply(itemStack);
                            sender.sendMessage(WandsPlugin.PREFIX + "Successfully removed §7§l" + argument.toLowerCase() + "§r from " + itemName);
                        } else {
                            sender.sendMessage(WandsPlugin.PREFIX + itemName + "§r doesn't contain spell §7§l" + argument.toLowerCase());
                        }
                    } else {
                        sender.sendMessage(WandsPlugin.PREFIX + "§7§l" + argument + " §ris not a spell!");
                    }
                }
            } else return false;
        }
        return true;
    }
}

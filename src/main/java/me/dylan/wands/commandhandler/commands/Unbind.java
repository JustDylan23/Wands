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

import java.util.Set;

public class Unbind implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (CommandUtils.isPlayerOrNotify(sender)) {
            if (args.length == 1) {
                Player player = (Player) sender;
                String argument = args[0].toUpperCase();
                ItemStack itemStack = player.getInventory().getItemInMainHand();
                if (CommandUtils.isWandOrNotify(player, itemStack)) {
                    SpellType spellType = SpellType.fromString(argument);
                    if (CommandUtils.isSpellOrNotify(sender, spellType, argument)) {
                        String itemName = ItemUtil.getName(itemStack);
                        Set<SpellType> compound = SpellCompound.getCompound(itemStack);
                        if (compound.remove(spellType)) {
                            SpellCompound.apply(compound, itemStack);
                            sender.sendMessage(WandsPlugin.PREFIX + "Successfully removed §7§l" + argument.toLowerCase() + "§r from " + itemName);
                        } else {
                            sender.sendMessage(WandsPlugin.PREFIX + itemName + "§r doesn't contain spell §7§l" + argument.toLowerCase());
                        }
                    }
                }
            } else return false;
        }
        return true;
    }
}

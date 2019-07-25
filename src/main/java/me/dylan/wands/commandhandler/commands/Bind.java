package me.dylan.wands.commandhandler.commands;

import me.dylan.wands.Main;
import me.dylan.wands.commandhandler.BaseCommand;
import me.dylan.wands.spell.SpellManagementUtil.SpellCompoundUtil;
import me.dylan.wands.spell.SpellType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;

public class Bind extends BaseCommand {
    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command cmd, @Nonnull String label, @Nonnull String[] args) {
        if (isPlayer(sender)) {
            if (args.length == 1) {
                Player player = (Player) sender;
                String argument = args[0].toLowerCase();
                ItemStack itemStack = player.getInventory().getItemInMainHand();
                if (isWand(player, itemStack)) {
                    SpellType spellType = SpellType.getSpellType(argument);
                    if (spellType != null) {
                        String itemName = itemStack.getItemMeta().getDisplayName();
                        if (!SpellCompoundUtil.containsSpell(itemStack, argument)) {
                            SpellCompoundUtil.addSpell(itemStack, spellType);
                            sender.sendMessage(Main.PREFIX + "Successfully added §7§l" + argument.toLowerCase() + "§r to " + itemName);
                        } else {
                            sender.sendMessage(Main.PREFIX + itemName + "§r already contains §7§l" + argument);
                        }
                    } else {
                        sender.sendMessage(Main.PREFIX + "§7§l" + argument + " §ris not a spell!");
                    }
                }
            } else return false;
        }
        return true;
    }

}

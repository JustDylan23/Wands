package me.dylan.wands.commandhandler.commands;

import me.dylan.wands.Main;
import me.dylan.wands.commandhandler.BaseCommand;
import me.dylan.wands.spell.SpellInstance;
import me.dylan.wands.spell.SpellManagementUtil.SpellCompoundUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class Bind extends BaseCommand {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
        if (isPlayer(sender)) {
            if (args.length == 1) {
                Player player = (Player) sender;
                String argument = args[0].toLowerCase();
                ItemStack itemStack = player.getInventory().getItemInMainHand();
                if (isWand(player, itemStack)) {
                    SpellInstance spellInstance = SpellInstance.getSpellType(argument);
                    if (spellInstance != null) {
                        String itemName = itemStack.getItemMeta().getDisplayName();
                        if (!SpellCompoundUtil.containsSpell(itemStack, argument)) {
                            if (SpellCompoundUtil.addSpell(itemStack, spellInstance, player, false)) {
                                sender.sendMessage(Main.PREFIX + "Successfully added §7§l" + argument.toLowerCase() + "§r to " + itemName);
                            }
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

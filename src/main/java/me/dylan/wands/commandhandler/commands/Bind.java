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

import java.util.Objects;

public class Bind extends BaseCommand {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (isPlayer(sender)) {
            if (args.length == 1) {
                Player player = (Player) sender;
                String argument = args[0].toLowerCase();
                ItemStack itemStack = player.getInventory().getItemInMainHand();
                if (isWand(player, itemStack)) {
                    SpellType spellType = SpellType.fromString(argument);
                    if (isSpell(sender, spellType, argument)) {
                        String itemName = Objects.requireNonNull(itemStack.getItemMeta()).getDisplayName();
                        SpellCompound compound = new SpellCompound(itemStack);
                        if (compound.add(spellType)) {
                            compound.apply(itemStack);
                            sender.sendMessage(WandsPlugin.PREFIX + "Successfully added §7§l" + argument.toLowerCase() + "§r to " + itemName);
                        } else {
                            sender.sendMessage(WandsPlugin.PREFIX + itemName + "§r already contains §7§l" + argument);
                        }
                    }
                }
            } else return false;
        }
        return true;
    }
}

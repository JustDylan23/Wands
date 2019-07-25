package me.dylan.wands.commandhandler.commands;

import me.dylan.wands.Main;
import me.dylan.wands.commandhandler.BaseCommand;
import me.dylan.wands.spell.SpellManagementUtil;
import me.dylan.wands.util.ItemUtil;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nonnull;
import java.util.StringJoiner;

public class CreateWand extends BaseCommand {
    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command cmd, @Nonnull String label, @Nonnull String[] args) {
        if (isPlayer(sender)) {
            if (args.length >= 1) {
                Player player = (Player) sender;
                ItemStack itemStack = player.getInventory().getItemInMainHand();
                if (itemStack.getType() != Material.AIR) {
                    String displayName = itemStack.getItemMeta().getDisplayName();
                    if (SpellManagementUtil.isWand(itemStack)) {
                        sender.sendMessage(Main.PREFIX + displayName + "§r is already a wand!");
                    } else {
                        if (SpellManagementUtil.canBeRegistered(itemStack)) {
                            SpellManagementUtil.setAsWand(itemStack);
                            StringJoiner stringJoiner = new StringJoiner(" ");
                            for (String arg : args) {
                                stringJoiner.add(arg);
                            }
                            ItemUtil.setName(itemStack, stringJoiner.toString());
                            sender.sendMessage(Main.PREFIX + itemStack.getItemMeta().getDisplayName() + "§r is registered as wand");
                            sender.sendMessage(Main.PREFIX + "use §8/bind <spell>§r to bind spells");
                        } else {
                            sender.sendMessage(Main.PREFIX + displayName + "§r can't be registered!");
                        }
                    }
                } else {
                    sender.sendMessage(Main.PREFIX + "You are not holding an item!");
                }
            } else return false;
        }
        return true;
    }
}

package me.dylan.wands.commandhandler.commands;

import me.dylan.wands.WandsPlugin;
import me.dylan.wands.commandhandler.BaseCommand;
import me.dylan.wands.miscellaneous.utils.ItemUtil;
import me.dylan.wands.spell.ItemTag;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.StringJoiner;

public class CreateWand extends BaseCommand {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (isPlayer(sender)) {
            if (args.length >= 1) {
                Player player = (Player) sender;
                ItemStack itemStack = player.getInventory().getItemInMainHand();
                if (itemStack.getType() != Material.AIR) {
                    String displayName = itemStack.getItemMeta().getDisplayName();
                    if (ItemTag.IS_WAND.isTagged(itemStack)) {
                        sender.sendMessage(WandsPlugin.PREFIX + displayName + "§r is already a wand!");
                    } else {
                        if (!ItemTag.CANNOT_REGISTER.isTagged(itemStack)) {
                            ItemTag.IS_WAND.tag(itemStack);
                            StringJoiner stringJoiner = new StringJoiner(" ");
                            for (String arg : args) {
                                stringJoiner.add(arg);
                            }
                            ItemUtil.setName(itemStack, stringJoiner.toString());
                            sender.sendMessage(WandsPlugin.PREFIX + itemStack.getItemMeta().getDisplayName() + "§r is registered as wand");
                            sender.sendMessage(WandsPlugin.PREFIX + "use §8/bind <spell>§r to bind spells");
                        } else {
                            sender.sendMessage(WandsPlugin.PREFIX + displayName + "§r can't be registered!");
                        }
                    }
                } else {
                    sender.sendMessage(WandsPlugin.PREFIX + "You are not holding an item!");
                }
            } else return false;
        }
        return true;
    }
}

package me.dylan.wands.commandhandler.commands;

import me.dylan.wands.WandsPlugin;
import me.dylan.wands.commandhandler.CommandUtils;
import me.dylan.wands.spell.accessories.ItemTag;
import me.dylan.wands.utils.ItemUtil;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.StringJoiner;

public class CreateWand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (CommandUtils.isPlayerOrNotify(sender)) {
            if (args.length >= 1) {
                Player player = (Player) sender;
                ItemStack itemStack = player.getInventory().getItemInMainHand();
                if (itemStack.getType() != Material.AIR) {
                    String displayName = ItemUtil.getName(itemStack);
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
                            sender.sendMessage(WandsPlugin.PREFIX + displayName + "§r is registered as wand");
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

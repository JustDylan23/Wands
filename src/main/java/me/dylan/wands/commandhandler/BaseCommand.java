package me.dylan.wands.commandhandler;

import me.dylan.wands.Main;
import me.dylan.wands.spell.ItemTag;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public abstract class BaseCommand implements CommandExecutor {
    protected boolean isPlayer(CommandSender sender) {
        if (sender instanceof Player) {
            return true;
        } else {
            sender.sendMessage(Main.PREFIX + "Only players can perform this command!");
            return false;
        }
    }

    protected boolean isWand(CommandSender sender, ItemStack itemStack) {
        if (ItemTag.IS_WAND.isTagged(itemStack)) {
            return true;
        } else {
            sender.sendMessage(Main.PREFIX + "Held item is not a wand!");
            return false;
        }
    }

    protected boolean checkPerm(@NotNull CommandSender commandSender, String permission) {
        if (commandSender.hasPermission("wands.command." + permission)) {
            return true;
        } else {
            commandSender.sendMessage("§cYou require §rwands.command." + permission);
            return false;
        }
    }
}

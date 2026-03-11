package me.dylan.wands.command;

import me.dylan.wands.WandsPlugin;
import me.dylan.wands.spell.accessories.ItemTag;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class CommandUtils {

    private CommandUtils() {
    }

    public static boolean isWandOrNotify(CommandSender sender, ItemStack itemStack) {
        if (ItemTag.IS_WAND.isTagged(itemStack)) {
            return true;
        } else {
            sender.sendMessage(WandsPlugin.PREFIX + "Held item is not a wand!");
            return false;
        }
    }

    public static boolean isInRangeOrNotify(@NotNull CommandSender commandSender, int in) {
        if (in >= 0 && in <= 99) {
            return true;
        } else {
            commandSender.sendMessage(in + " must be a number between " + 0 + " and " + 99);
            return false;
        }
    }
}

package me.dylan.wands.commandhandler;

import me.dylan.wands.Wands;
import me.dylan.wands.plugindata.ObtainableItem;
import me.dylan.wands.spells.meta.Spell;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.StringJoiner;

public class MainCommandHandler implements CommandExecutor {
    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command cmd, @Nonnull String label, String[] args) {
        switch (args.length) {
            case 1:
                switch (args[0]) {
                    case "disable":
                        Wands.getPlugin().getPluginData().allowMagicUse(false);
                        sender.sendMessage(Wands.PREFIX + "All wands are now disabled.");
                        return true;
                    case "enable":
                        Wands.getPlugin().getPluginData().allowMagicUse(true);
                        sender.sendMessage(Wands.PREFIX + "All wands are now enabled.");
                        return true;
                    case "spells":
                        Spell[] spells = Spell.values();
                        StringJoiner sj = new StringJoiner(", ");
                        for (Spell spell : spells) {
                            sj.add(spell.toString());
                        }
                        sender.sendMessage("§6Spells (" + spells.length + "): §r" + sj.toString());
                        return true;
                    case "info":
                        sender.sendMessage("§e ---- §6Wands§e ----");
                        sender.sendMessage("§6Created by: §e_JustDylan_");
                        sender.sendMessage("§6Current version:§e " + Wands.getPlugin().getDescription().getVersion());
                        return true;
                }
                return false;
            case 2:
                if (args[0].equalsIgnoreCase("get")) {
                    try {
                        ObtainableItem value = ObtainableItem.valueOf(args[1].toUpperCase());
                        if (sender instanceof Player)
                            ((Player) sender).getInventory().addItem(value.getItemStack());
                        else {
                            sender.sendMessage(Wands.PREFIX + "You must be a player in order to perform this action");
                        }
                    } catch (IllegalArgumentException e) {
                        sender.sendMessage(Wands.PREFIX + "Wand does not exist!");
                    }
                    return true;
                }
                return false;
            case 3:
                if (args[0].equalsIgnoreCase("set")) {
                    if (args[1].equalsIgnoreCase("cooldown")) {
                        try {
                            int i = Integer.parseInt(args[2]);
                            if (i < 0) {
                                sender.sendMessage(Wands.PREFIX + "Cooldown can't be a negative number!");
                                return true;
                            }
                            Wands.getPlugin().getPluginData().setMagicCooldownTime(i);
                            String message = Wands.PREFIX + "Cooldown has been set to " + i + " second" + ((i != 1) ? "s" : "");
                            sender.sendMessage(message);
                        } catch (NumberFormatException e) {
                            sender.sendMessage(Wands.PREFIX + "Cooldown can only be set to a full number!");
                        }
                        return true;
                    }
                }
        }
        return false;
    }


}

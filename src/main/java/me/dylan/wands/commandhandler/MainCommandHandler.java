package me.dylan.wands.commandhandler;

import me.dylan.wands.GUIs;
import me.dylan.wands.Wands;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MainCommandHandler implements CommandExecutor{

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("wands")) {
            if (args.length == 0) {
                if (sender instanceof Player) {
                    GUIs.openGUI((Player) sender);
                }
                return true;
            } else if (args[0].equalsIgnoreCase("info")) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&e ---- &6Wands&e ----"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Created by: &e_JustDylan_"));
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6Current version:&e " + Wands.VERSION));
            }
            sender.sendMessage(args[0] + " " + args[1]);
            return true;
        }
        return true;
    }
}

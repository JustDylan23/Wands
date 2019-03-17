package me.dylan.wands.commandhandler;

import me.dylan.wands.GUIs;
import me.dylan.wands.SpellFoundation.Spell;
import me.dylan.wands.Wands;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.StringJoiner;

public class MainCommandHandler implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (label.equalsIgnoreCase("wands")) {
            if (args.length == 0) {
                if (sender instanceof Player) {
                    GUIs.openGUI((Player) sender);
                }
                return true;
            } else if (args[0].equalsIgnoreCase("spells")) {
                Spell[] spells = Spell.values();
                StringJoiner sj = new StringJoiner(", ");
                for (Spell spell : spells) {
                    sj.add(spell.toString());
                }
                sender.sendMessage("§6Spells (" + spells.length + "): §r" + sj.toString());
            } else if (args[0].equalsIgnoreCase("info")) {
                sender.sendMessage("§e ---- §6Wands§e ----");
                sender.sendMessage("§6Created by: §e_JustDylan_");
                sender.sendMessage("§6Current version:§e " + Wands.getPlugin().getDescription().getVersion());
            }
            return true;
        }
        return true;
    }
}

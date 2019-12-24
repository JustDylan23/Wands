package me.dylan.wands.commandhandler.commands;

import me.dylan.wands.WandsPlugin;
import me.dylan.wands.commandhandler.BaseCommand;
import me.dylan.wands.config.ConfigHandler;
import me.dylan.wands.spell.SpellType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class TweakCooldown extends BaseCommand {
    private final ConfigHandler configHandler;

    public TweakCooldown(ConfigHandler configHandler) {
        this.configHandler = configHandler;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length > 0) {
            SpellType spellType = SpellType.fromString(args[0]);
            if (isSpell(sender, spellType, args[0]))
                if (args.length > 1) {
                    try {
                        int input = Integer.parseInt(args[1]);
                        String message = WandsPlugin.PREFIX + spellType.name + "'s cooldown is now default value + " + input;
                        if (isInRange(sender, 0, 99, input)) {
                            configHandler.setCooldown(spellType, input);
                            sender.sendMessage(message);
                        }
                    } catch (NumberFormatException e) {
                        sender.sendMessage(WandsPlugin.PREFIX + "Invalid number");
                    }
                } else {
                    int cooldown = spellType.behavior.getCooldown();
                    sender.sendMessage(WandsPlugin.PREFIX + "Cooldown of " + spellType.name + " is +" + cooldown + " second" + ((cooldown == 1) ? "" : "s"));
                }
        } else return false;
        return true;
    }
}
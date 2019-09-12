package me.dylan.wands.commandhandler.commands;

import me.dylan.wands.WandsPlugin;
import me.dylan.wands.config.ConfigurableData;
import me.dylan.wands.spell.SpellType;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public class TweakSpell implements CommandExecutor {
    private final ConfigurableData configurableData;

    public TweakSpell(ConfigurableData configurableData) {
        this.configurableData = configurableData;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length > 0) {
            SpellType spellType = SpellType.getSpellType(args[0]);
            if (spellType == null) {
                sender.sendMessage(WandsPlugin.PREFIX + "§7§l" + args[0] + " §ris not a spell!");
            } else if (args.length > 1) {
                String option = args[1].toLowerCase();
                if ("cooldown".equals(option) || "damage".equals(option)) {
                    if (args.length > 2) {
                        try {
                            int value = Integer.parseInt(args[2]);
                            String message = WandsPlugin.PREFIX + spellType.name + "'s " + option + " is now default value +" + value;
                            if ("cooldown".equals(option)) {
                                if (value < 100 && value >= 0) {
                                    configurableData.tweakCooldown(spellType, value);
                                    sender.sendMessage(message);
                                    return true;
                                } else {
                                    sender.sendMessage(WandsPlugin.PREFIX + "Cooldown must be between 0 and 99");
                                }
                            } else {
                                int minVal = -spellType.behavior.entityDamage;
                                if (value < 100 && value >= minVal) {
                                    configurableData.tweakDamage(spellType, value);
                                    sender.sendMessage(message);
                                    return true;
                                } else {
                                    sender.sendMessage(WandsPlugin.PREFIX + "Damage must be between " + minVal + " and 99");
                                }
                            }
                        } catch (NumberFormatException e) {
                            sender.sendMessage(WandsPlugin.PREFIX + "Tweak can only be set to a full number!");
                        }
                    }
                } else {
                    sender.sendMessage(WandsPlugin.PREFIX + "§rCannot tweak " + option);
                }
            }
        }
        if (args.length <= 2) {
            StringBuilder stringBuilder = new StringBuilder("/" + label);
            for (String arg : args) {
                stringBuilder.append(" ").append(arg);
            }
            sender.sendMessage(stringBuilder + "§c§o<--[MISSING ARGUMENT]");
        }
        return true;
    }
}
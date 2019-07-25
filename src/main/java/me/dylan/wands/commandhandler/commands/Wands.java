package me.dylan.wands.commandhandler.commands;

import me.dylan.wands.Main;
import me.dylan.wands.commandhandler.BaseCommand;
import me.dylan.wands.pluginmeta.ConfigurableData;
import me.dylan.wands.pluginmeta.ObtainableItem;
import me.dylan.wands.spell.SpellType;
import me.dylan.wands.spell.handler.Behaviour;
import me.dylan.wands.util.ItemUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import javax.annotation.Nonnull;
import java.util.StringJoiner;

public class Wands extends BaseCommand {
    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command cmd, @Nonnull String label, @Nonnull String[] args) {
        switch (args.length) {
            case 1:
                switch (args[0]) {
                    case "inspect":
                        if (isPlayer(sender)) {
                            Player player = (Player) sender;
                            ItemStack itemStack = player.getInventory().getItemInMainHand();
                            player.sendMessage("Spells: [" + (ItemUtil.getPersistentData(itemStack, "Spells", PersistentDataType.STRING).orElse("empty")) + "]");
                        }
                        return true;
                    case "disable":
                        if (checkPerm(sender, "toggle")) {
                            Main.getPlugin().getConfigurableData().allowMagicUse(false);
                            sender.sendMessage(Main.PREFIX + "All wands are now disabled.");
                        }
                        return true;
                    case "enable":
                        if (checkPerm(sender, "toggle")) {
                            Main.getPlugin().getConfigurableData().allowMagicUse(true);
                            sender.sendMessage(Main.PREFIX + "All wands are now enabled.");
                        }
                        return true;
                    case "spells":
                        if (checkPerm(sender, "list")) {
                            SpellType[] spellTypes = SpellType.values();
                            StringJoiner stringJoiner = new StringJoiner(", ");
                            for (SpellType spellType : spellTypes) {
                                stringJoiner.add(spellType.getName());
                            }
                            sender.sendMessage("§6Spells (" + spellTypes.length + "): §r" + stringJoiner);
                        }
                        return true;
                    case "info":
                        sender.sendMessage("§e---- §6Wands§e ----");
                        sender.sendMessage("§6Created by: §e_JustDylan_");
                        sender.sendMessage("§6Current version:§e " + Main.getPlugin().getDescription().getVersion());
                        return true;
                    case "get":
                        if (checkPerm(sender, "get")) {
                            if (sender instanceof Player) {
                                ObtainableItem.openInventory((Player) sender);
                            } else
                                sender.sendMessage(Main.PREFIX + "You must be a player in order to perform this action!");
                        }
                        return true;
                    case "getconfig":
                        if (checkPerm(sender, "viewconfig")) {
                            ConfigurableData cd = Main.getPlugin().getConfigurableData();
                            sender.sendMessage("§6magic cooldown time:§r " + cd.getMagicCooldownTime());
                            sender.sendMessage("§6allow magic use:§r " + (cd.isMagicUseAllowed() ? "§a" : "§c") + cd.isMagicUseAllowed());
                            sender.sendMessage("§6allow self harm use:§r " + (cd.isSelfHarmAllowed() ? "§a" : "§c") + cd.isSelfHarmAllowed());
                        }
                        return true;
                }
                return false;
            case 2:
                if (args[0].equalsIgnoreCase("get")) {
                    if (checkPerm(sender, "get")) {
                        try {
                            ObtainableItem value = ObtainableItem.valueOf(args[1].toUpperCase());
                            if (sender instanceof Player)
                                ((Player) sender).getInventory().addItem(value.getItemStack());
                            else {
                                sender.sendMessage(Main.PREFIX + "You must be a player in order to perform this action!");
                            }
                        } catch (IllegalArgumentException e) {
                            sender.sendMessage(Main.PREFIX + "Wand does not exist!");
                        }
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("spells")) {
                    if (checkPerm(sender, "list")) {
                        SpellType spellType;
                        try {
                            spellType = SpellType.valueOf(args[1].toUpperCase());
                            Behaviour behaviour = spellType.castable.getBehaviour();
                            if (behaviour == null) {
                                sender.sendMessage(Main.PREFIX + "Spell has no behaviour!");
                            } else {
                                sender.sendMessage("§e ---- §6" + args[1].toUpperCase() + "§e ----§r\n" + behaviour);
                            }
                        } catch (IllegalArgumentException e) {
                            sender.sendMessage(Main.PREFIX + "Spell does not exist!");
                        }
                    }
                    return true;
                }
                return false;
            case 3:
                if (args[0].equalsIgnoreCase("set")) {
                    if (args[1].equalsIgnoreCase("cooldown")) {
                        if (checkPerm(sender, "setcooldown")) {
                            try {
                                int i = Integer.parseInt(args[2]);
                                if (i < 0) {
                                    sender.sendMessage(Main.PREFIX + "Cooldown can't be a negative number!");
                                    return true;
                                }
                                Main.getPlugin().getConfigurableData().setMagicCooldownTime(i);
                                String message = Main.PREFIX + "Cooldown has been set to " + i + " second" + ((i != 1) ? "s" : "");
                                sender.sendMessage(message);
                            } catch (NumberFormatException e) {
                                sender.sendMessage(Main.PREFIX + "Cooldown can only be set to a full number!");
                            }
                        }
                        return true;
                    } else if (args[1].equalsIgnoreCase("selfharm")) {
                        if (checkPerm(sender, "setselfharm")) {
                            boolean b = Boolean.parseBoolean(args[2]);
                            if (b) {
                                sender.sendMessage(Main.PREFIX + "Player can now harm himself with wands");
                            } else {
                                sender.sendMessage(Main.PREFIX + "Player can no longer harm himself with wands");
                            }
                            Main.getPlugin().getConfigurableData().allowSelfHarm(b);
                        }
                        return true;
                    }
                }
        }
        return false;
    }

}

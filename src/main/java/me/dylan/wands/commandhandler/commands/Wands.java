package me.dylan.wands.commandhandler.commands;

import me.dylan.wands.Main;
import me.dylan.wands.PreSetItem;
import me.dylan.wands.commandhandler.BaseCommand;
import me.dylan.wands.config.ConfigurableData;
import me.dylan.wands.spell.SpellInstance;
import me.dylan.wands.spell.types.Base;
import me.dylan.wands.util.ItemUtil;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.StringJoiner;

public class Wands extends BaseCommand {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, @NotNull String[] args) {
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
                            SpellInstance[] spellInstances = SpellInstance.values();
                            StringJoiner stringJoiner = new StringJoiner(", ");
                            for (SpellInstance spellInstance : spellInstances) {
                                stringJoiner.add(spellInstance.getName());
                            }
                            sender.sendMessage("§6Spells (" + spellInstances.length + "): §r" + stringJoiner);
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
                                PreSetItem.openInventory((Player) sender);
                            } else
                                sender.sendMessage(Main.PREFIX + "You must be a player in order to perform this action!");
                        }
                        return true;
                    case "getconfig":
                        if (checkPerm(sender, "viewconfig")) {
                            ConfigurableData cd = Main.getPlugin().getConfigurableData();
                            sender.sendMessage("§6magic cooldown time:§r " + (cd.getMagicCooldownTime() / 1000) + " seconds");
                            sender.sendMessage("§6allow magic use:§r " + (cd.isMagicUseAllowed() ? "§a" : "§c") + cd.isMagicUseAllowed());
                            sender.sendMessage("§6allow self harm use:§r " + (cd.isSelfHarmAllowed() ? "§a" : "§c") + cd.isSelfHarmAllowed());
                            sender.sendMessage("§6wands usage requires permissoin:§r " + (cd.doesCastingRequirePermission() ? "§a" : "§c") + cd.doesCastingRequirePermission());
                        }
                        return true;
                    case "getcdwands":
                        if (checkPerm(sender, "getcdwands") && isPlayer(sender)) {
                            Player player = (Player) sender;
                            ItemStack item = PreSetItem.MAGIC_WAND.getItemStack();
                            ItemMeta meta = item.getItemMeta();
                            item = new ItemStack(Material.BLAZE_ROD);
                            item.setItemMeta(meta);
                            ItemUtil.setName(item, "&cEmpire Wand");
                            player.getInventory().addItem(item);
                            item = PreSetItem.CURSED_BOW.getItemStack();
                            ItemUtil.setName(item, "&cEmpire Bow");
                            player.getInventory().addItem(item);
                            return true;
                        }
                        return false;
                }
                return false;
            case 2:
                if (args[0].equalsIgnoreCase("get")) {
                    if (checkPerm(sender, "get")) {
                        try {
                            PreSetItem value = PreSetItem.valueOf(args[1].toUpperCase());
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
                        SpellInstance spellInstance;
                        try {
                            spellInstance = SpellInstance.valueOf(args[1].toUpperCase());
                            Base baseType = spellInstance.castable.getBaseType();
                            if (baseType == null) {
                                sender.sendMessage(Main.PREFIX + "Spell has no behaviour!");
                            } else {
                                sender.sendMessage("§e ---- §6" + args[1].toUpperCase() + "§e ----§r\n" + baseType);
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
                                int x = i * 1000;
                                if (args[2].length() <= 2) {
                                    if (i >= 0) {
                                        Main.getPlugin().getConfigurableData().setMagicCooldownTime(x);
                                        sender.sendMessage(Main.PREFIX + "Cooldown has been set to " + i + " second" + ((i != 1) ? "s" : ""));
                                    } else {
                                        sender.sendMessage(Main.PREFIX + "Number can't be negative!");
                                    }
                                } else {
                                    sender.sendMessage(Main.PREFIX + "Max value is 99");
                                }
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
                    } else if (args[1].equalsIgnoreCase("restriction")) {
                        if (checkPerm(sender, "restrict")) {
                            boolean b = Boolean.parseBoolean(args[2]);
                            if (b) {
                                sender.sendMessage(Main.PREFIX + "Only players with §7wands.use §rcan use wands now");
                            } else {
                                sender.sendMessage(Main.PREFIX + "All players can use wands now");
                            }
                            Main.getPlugin().getConfigurableData().requirePermissionForCasting(b);
                        }
                        return true;
                    }
                }
        }
        return false;
    }

}

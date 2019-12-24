package me.dylan.wands.commandhandler.commands;

import me.dylan.wands.PreSetItem;
import me.dylan.wands.WandsPlugin;
import me.dylan.wands.commandhandler.BaseCommand;
import me.dylan.wands.config.ConfigHandler;
import me.dylan.wands.spell.SpellCompound;
import me.dylan.wands.spell.SpellType;
import me.dylan.wands.spell.spellbuilders.Behavior;
import me.dylan.wands.utils.ItemUtil;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.StringJoiner;

public class Wands extends BaseCommand {
    private final ConfigHandler configHandler;
    private final String version;

    public Wands(ConfigHandler configHandler, String version) {
        this.configHandler = configHandler;
        this.version = version;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        switch (args.length) {
            case 1:
                switch (args[0]) {
                    case "inspect":
                        if (isPlayer(sender)) {
                            Player player = (Player) sender;
                            ItemStack itemStack = player.getInventory().getItemInMainHand();
                            player.sendMessage(WandsPlugin.PREFIX + "\nLegacy Spells: [" + (ItemUtil.getPersistentData(itemStack, SpellCompound.TAG_SPELLS_LIST, PersistentDataType.STRING).orElse("empty")) + "]");
                            player.sendMessage("\nSpells: " + (Arrays.toString(ItemUtil.getPersistentData(itemStack, SpellCompound.TAG_SPELLS_LIST, PersistentDataType.INTEGER_ARRAY).orElse(new int[0]))));
                        }
                        return true;
                    case "disable":
                        if (checkPerm(sender, "toggle")) {
                            configHandler.allowMagicUse(false);
                            sender.sendMessage(WandsPlugin.PREFIX + "All wands are now disabled.");
                        }
                        return true;
                    case "enable":
                        if (checkPerm(sender, "toggle")) {
                            configHandler.allowMagicUse(true);
                            sender.sendMessage(WandsPlugin.PREFIX + "All wands are now enabled.");
                        }
                        return true;
                    case "spells":
                        if (checkPerm(sender, "list")) {
                            SpellType[] spellTypes = SpellType.values();
                            StringJoiner stringJoiner = new StringJoiner(", ");
                            for (SpellType spellType : spellTypes) {
                                stringJoiner.add(spellType.name);
                            }
                            sender.sendMessage("§6Spells (" + spellTypes.length + "):§r " + stringJoiner);
                        }
                        return true;
                    case "info":
                        sender.sendMessage("§e---- §6Wands§e ----");
                        sender.sendMessage("§6Created by: §e_JustDylan_");
                        sender.sendMessage("§6Current version:§e " + version);
                        return true;
                    case "get":
                        if (checkPerm(sender, "get")) {
                            if (sender instanceof Player) {
                                PreSetItem.openInventory((Player) sender);
                            } else
                                sender.sendMessage(WandsPlugin.PREFIX + "You must be a player in order to perform this action!");
                        }
                        return true;
                    case "getconfig":
                        if (checkPerm(sender, "viewconfig")) {
                            ConfigHandler cd = configHandler;
                            sender.sendMessage("§6magic cooldown time:§r " + (cd.getSpellCooldown() / 1000) + " seconds");
                            sender.sendMessage("§6allow magic use:§r " + (cd.isMagicEnabled() ? "§a" : "§c") + cd.isMagicEnabled());
                            sender.sendMessage("§6wands usage requires permissoin:§r " + (cd.doesCastingRequirePermission() ? "§a" : "§c") + cd.doesCastingRequirePermission());
                        }
                        return true;
                    case "getcdwands":
                        if (checkPerm(sender, "getcdwands") && isPlayer(sender)) {
                            InventoryHolder player = (InventoryHolder) sender;
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
                if ("get".equalsIgnoreCase(args[0])) {
                    if (checkPerm(sender, "get")) {
                        try {
                            PreSetItem value = PreSetItem.valueOf(args[1].toUpperCase());
                            if (sender instanceof Player)
                                ((InventoryHolder) sender).getInventory().addItem(value.getItemStack());
                            else {
                                sender.sendMessage(WandsPlugin.PREFIX + "You must be a player in order to perform this action!");
                            }
                        } catch (IllegalArgumentException e) {
                            sender.sendMessage(WandsPlugin.PREFIX + "Wand does not exist!");
                        }
                    }
                    return true;
                }
                if ("spells".equalsIgnoreCase(args[0])) {
                    if (checkPerm(sender, "list")) {
                        SpellType spellType;
                        try {
                            spellType = SpellType.valueOf(args[1].toUpperCase());
                            Behavior behavior = spellType.behavior;
                            if (behavior == null) {
                                sender.sendMessage(WandsPlugin.PREFIX + "Spell has no behaviour!");
                            } else {
                                sender.sendMessage("§e ---- §6" + args[1].toUpperCase() + "§e ----§r\n" + behavior);
                            }
                        } catch (IllegalArgumentException e) {
                            sender.sendMessage(WandsPlugin.PREFIX + "Spell does not exist!");
                        }
                    }
                    return true;
                }
                return false;
            case 3:
                if ("set".equalsIgnoreCase(args[0])) {
                    if ("cooldown".equalsIgnoreCase(args[1])) {
                        if (checkPerm(sender, "setcooldown")) {
                            try {
                                int in = Integer.parseInt(args[2]);
                                if (args[2].length() <= 2 && isInRange(sender, 0, 99, in)) {
                                    configHandler.setSpellCooldown(in);
                                    sender.sendMessage(WandsPlugin.PREFIX + "Cooldown has been set to " + in + " second" + ((in != 1) ? "s" : ""));
                                }
                            } catch (NumberFormatException e) {
                                sender.sendMessage(WandsPlugin.PREFIX + "Cooldown can only be set to a full number!");
                            }
                        }
                        return true;
                    } else if ("restriction".equalsIgnoreCase(args[1])) {
                        if (checkPerm(sender, "restrict")) {
                            boolean result = Boolean.parseBoolean(args[2]);
                            if (result) {
                                sender.sendMessage(WandsPlugin.PREFIX + "Only players with §7wands.use §rcan use wands now");
                            } else {
                                sender.sendMessage(WandsPlugin.PREFIX + "All players can use wands now");
                            }
                            configHandler.requirePermissionForCasting(result);
                        }
                        return true;
                    }
                }
        }
        return false;
    }

}

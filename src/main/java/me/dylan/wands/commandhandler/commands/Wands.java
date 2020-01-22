package me.dylan.wands.commandhandler.commands;

import me.dylan.wands.PreSetItem;
import me.dylan.wands.WandsPlugin;
import me.dylan.wands.commandhandler.CommandUtils;
import me.dylan.wands.commandhandler.Permissions;
import me.dylan.wands.config.ConfigHandler;
import me.dylan.wands.spell.ItemBuilder;
import me.dylan.wands.spell.SpellCompound;
import me.dylan.wands.spell.SpellType;
import me.dylan.wands.spell.spellbuilders.Behavior;
import me.dylan.wands.utils.ItemUtil;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.StringJoiner;

public class Wands implements CommandExecutor {
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
                switch (args[0].toLowerCase()) {
                    case "update":
                        if (CommandUtils.checkPermOrNotify(sender, Permissions.UPDATE)) {
                            WandsPlugin.getInstance().getUpdater().checkForUpdates(sender, false);
                        }
                        return true;
                    case "inspect":
                        if (CommandUtils.isPlayerOrNotify(sender)) {
                            Player player = (Player) sender;
                            ItemStack itemStack = player.getInventory().getItemInMainHand();
                            if (CommandUtils.isWandOrNotify(sender, itemStack)) {
                                if (ItemUtil.hasPersistentData(itemStack, SpellCompound.TAG_SPELLS_LIST, PersistentDataType.STRING)) {
                                    player.sendMessage(WandsPlugin.PREFIX_TOP + "Legacy Spells: [" + (ItemUtil.getPersistentData(itemStack, SpellCompound.TAG_SPELLS_LIST, PersistentDataType.STRING).orElse("none")) + "]");
                                }
                                if (ItemUtil.hasPersistentData(itemStack, SpellCompound.TAG_SPELLS_LIST, PersistentDataType.INTEGER_ARRAY)) {
                                    player.sendMessage("Spells: " + (Arrays.toString(ItemUtil.getPersistentData(itemStack, SpellCompound.TAG_SPELLS_LIST, PersistentDataType.INTEGER_ARRAY).orElse(new int[0]))));
                                }
                            }
                        }
                        return true;
                    case "disable":
                        if (CommandUtils.checkPermOrNotify(sender, Permissions.DISABLE_PLUGIN)) {
                            if (configHandler.enableMagic(false)) {
                                sender.sendMessage(WandsPlugin.PREFIX + "All wands are now disabled.");
                            } else {
                                sender.sendMessage(WandsPlugin.PREFIX + "All wands are alreadt disabled.");
                            }
                        }
                        return true;
                    case "enable":
                        if (CommandUtils.checkPermOrNotify(sender, Permissions.ENABLE_PLUGIN)) {
                            if (configHandler.enableMagic(true)) {
                                sender.sendMessage(WandsPlugin.PREFIX + "All wands are now enabled.");
                            } else {
                                sender.sendMessage(WandsPlugin.PREFIX + "All wands are already enabled.");
                            }
                        }
                        return true;
                    case "spells":
                        if (CommandUtils.checkPermOrNotify(sender, Permissions.LIST_SPELLS)) {
                            SpellType[] spellTypes = SpellType.values();
                            StringJoiner stringJoiner = new StringJoiner(", ");
                            for (SpellType spellType : spellTypes) {
                                stringJoiner.add(spellType.getDisplayName());
                            }
                            sender.sendMessage("§6Spells (" + spellTypes.length + "):§r " + stringJoiner);
                        }
                        return true;
                    case "info":
                        sender.sendMessage(WandsPlugin.PREFIX_TOP + "Created by: §e_JustDylan_§r\nContributor(s): §ejetp250§r\nCurrent version:§e v" + version);
                        return true;
                    case "get":
                        if (CommandUtils.checkPermOrNotify(sender, Permissions.GET_WAND)) {
                            if (sender instanceof Player) {
                                PreSetItem.openInventory((Player) sender);
                            } else
                                sender.sendMessage(WandsPlugin.PREFIX + "You must be a player in order to perform this action!");
                        }
                        return true;
                    case "settings":
                        if (CommandUtils.checkPermOrNotify(sender, Permissions.SETTINGS)) {
                            sender.sendMessage(WandsPlugin.PREFIX_TOP);
                            sender.sendMessage("magic cooldown time:§r " + (configHandler.getGlobalSpellCooldown()) + " seconds");
                            sender.sendMessage("allow magic use:§r " + (configHandler.isMagicEnabled() ? "§atrue" : "§cfalse"));
                            sender.sendMessage("wands usage requires permission:§r " + (configHandler.doesCastingRequirePermission() ? "§atrue" : "§cfalse"));
                            sender.sendMessage("send update notifications:§r " + (configHandler.areNotificationsEnabled() ? "§atrue" : "§cfalse"));
                        }
                        return true;
                }
                return false;
            case 2:
                switch (args[0].toLowerCase()) {
                    case "getscroll":
                        if (CommandUtils.checkPermOrNotify(sender, Permissions.GET_SCROLL) && CommandUtils.isPlayerOrNotify(sender)) {
                            SpellType spellType = SpellType.fromString(args[1]);
                            if (spellType != null) {
                                Player player = (Player) sender;
                                ItemStack itemStack = ItemBuilder.from(Material.MOJANG_BANNER_PATTERN)
                                        .named(spellType.name + " scroll")
//                                        .withLore("When in survival click on this scroll", "and drag it over to a wand", "and click on the wand to apply", "the scroll")
                                        .glowing()
                                        .hideFlags()
                                        .blockWandRegistration()
                                        .build();
                                ItemUtil.setPersistentData(itemStack, "spell", PersistentDataType.INTEGER, spellType.id);
                                player.getInventory().addItem(itemStack);
                            } else {
                                sender.sendMessage(WandsPlugin.PREFIX + "Spell does not exist!");
                            }
                        }
                        return true;
                    case "update":
                        if ("download".equalsIgnoreCase(args[1]) && CommandUtils.checkPermOrNotify(sender, Permissions.UPDATE_DOWNLOAD)) {
                            WandsPlugin.getInstance().getUpdater().checkForUpdates(sender, true);
                        }
                        return true;
                    case "get":
                        if (CommandUtils.checkPermOrNotify(sender, Permissions.GET_WAND)) {
                            try {
                                PreSetItem value = PreSetItem.valueOf(args[1].toUpperCase());
                                if (CommandUtils.isPlayerOrNotify(sender)) {
                                    ((InventoryHolder) sender).getInventory().addItem(value.getItemStack());
                                }
                            } catch (IllegalArgumentException e) {
                                sender.sendMessage(WandsPlugin.PREFIX + "Wand does not exist!");
                            }
                        }
                        return true;
                    case "spells":
                        if (CommandUtils.checkPermOrNotify(sender, Permissions.LIST_SPELLS)) {
                            SpellType spellType = SpellType.fromString(args[1]);
                            if (spellType != null) {
                                Behavior behavior = spellType.behavior;
                                if (behavior == null) {
                                    sender.sendMessage(WandsPlugin.PREFIX + "Spell has no behaviour!");
                                } else {
                                    sender.sendMessage("§e ---- §6" + args[1].toUpperCase() + "§e ----§r\n" + behavior);
                                }
                            } else {
                                sender.sendMessage(WandsPlugin.PREFIX + "Spell does not exist!");
                            }
                        }
                        return true;
                }
                return false;
            case 3:
                if ("settings".equalsIgnoreCase(args[0])) {
                    if ("cooldown".equalsIgnoreCase(args[1])) {
                        if (CommandUtils.checkPermOrNotify(sender, Permissions.SETTINGS_COOLDOWN)) {
                            try {
                                int in = Integer.parseInt(args[2]);
                                if (args[2].length() <= 2 && CommandUtils.isInRangeOrNotify(sender, in)) {
                                    configHandler.setGlobalSpellCooldown(in);
                                    sender.sendMessage(WandsPlugin.PREFIX + "Cooldown has been set to " + in + " second" + ((in != 1) ? "s" : ""));
                                }
                            } catch (NumberFormatException e) {
                                sender.sendMessage(WandsPlugin.PREFIX + "Cooldown can only be set to a full number!");
                            }
                        }
                        return true;
                    } else if ("restriction".equalsIgnoreCase(args[1])) {
                        if (CommandUtils.checkPermOrNotify(sender, Permissions.SETTINGS_RESTRICT)) {
                            boolean result = Boolean.parseBoolean(args[2]);
                            if (result) {
                                sender.sendMessage(WandsPlugin.PREFIX + "Only players with §7" + Permissions.USE + " §rcan use wands now");
                            } else {
                                sender.sendMessage(WandsPlugin.PREFIX + "All players can use wands now");
                            }
                            configHandler.requirePermissionForCasting(result);
                        }
                        return true;
                    } else if ("notifications".equalsIgnoreCase(args[1])) {
                        if (CommandUtils.checkPermOrNotify(sender, Permissions.SETTINGS_NOTIFICATIONS)) {
                            boolean result = Boolean.parseBoolean(args[2]);

                            if (configHandler.enableNotifications(result)) {
                                sender.sendMessage(WandsPlugin.PREFIX + (result ? "Enabled" : "Disabled") + " update notifications");
                            } else {
                                if (result) {
                                    sender.sendMessage(WandsPlugin.PREFIX + "Notifications are already enabled");
                                } else {
                                    sender.sendMessage(WandsPlugin.PREFIX + "Notifications are already disabled");
                                }
                            }
                        }
                        return true;
                    }
                }
                return false;
        }
        return false;
    }

}

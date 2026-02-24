package me.dylan.wands.command;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import me.dylan.wands.PreSetItem;
import me.dylan.wands.WandsPlugin;
import me.dylan.wands.config.ConfigHandler;
import me.dylan.wands.spell.ItemBuilder;
import me.dylan.wands.spell.SpellCompound;
import me.dylan.wands.spell.SpellType;
import me.dylan.wands.spell.accessories.ItemTag;
import me.dylan.wands.spell.spellbuilders.Behavior;
import me.dylan.wands.spell.util.SpellInteractionUtil;
import me.dylan.wands.utils.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.Set;
import java.util.StringJoiner;

import static me.dylan.wands.utils.Common.quantify;

@CommandAlias("wands|wd")
@Description("The main command for the wands plugin.")
public class WandsCommand extends BaseCommand {
    private final ConfigHandler configHandler;

    public WandsCommand(ConfigHandler configHandler) {
        this.configHandler = configHandler;
    }

    @Subcommand("inspect")
    public void onInspect(Player player) {
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (CommandUtils.isWandOrNotify(player, itemStack)) {
            if (ItemUtil.hasPersistentData(itemStack, SpellCompound.TAG_SPELLS_LIST, PersistentDataType.STRING)) {
                player.sendMessage(WandsPlugin.PREFIX_TOP + "Legacy Spells: [" + (ItemUtil.getPersistentData(itemStack, SpellCompound.TAG_SPELLS_LIST, PersistentDataType.STRING).orElse("none")) + "]");
            }
            if (ItemUtil.hasPersistentData(itemStack, SpellCompound.TAG_SPELLS_LIST, PersistentDataType.INTEGER_ARRAY)) {
                player.sendMessage("Spells: " + (Arrays.toString(ItemUtil.getPersistentData(itemStack, SpellCompound.TAG_SPELLS_LIST, PersistentDataType.INTEGER_ARRAY).orElse(new int[0]))));
            }
        }
    }

    @Subcommand("disable")
    @CommandPermission(Permissions.DISABLE_PLUGIN)
    public void onDisable(CommandSender sender) {
        if (configHandler.enableMagic(false)) {
            sender.sendMessage(WandsPlugin.PREFIX + "All wands are now disabled.");
        } else {
            sender.sendMessage(WandsPlugin.PREFIX + "All wands are already disabled.");
        }
    }

    @Subcommand("enable")
    @CommandPermission(Permissions.ENABLE_PLUGIN)
    public void onEnable(CommandSender sender) {
        if (configHandler.enableMagic(true)) {
            sender.sendMessage(WandsPlugin.PREFIX + "All wands are now enabled.");
        } else {
            sender.sendMessage(WandsPlugin.PREFIX + "All wands are already enabled.");
        }
    }

    @Subcommand("spells")
    @CommandPermission(Permissions.LIST_SPELLS)
    public void onSpells(CommandSender sender) {
        SpellType[] spellTypes = SpellType.values();
        StringJoiner stringJoiner = new StringJoiner(", ");
        for (SpellType spellType : spellTypes) {
            stringJoiner.add(spellType.getDisplayName());
        }
        sender.sendMessage("§6Spells (" + spellTypes.length + "):§r " + stringJoiner);
    }

    @Subcommand("spells")
    @CommandPermission(Permissions.LIST_SPELLS)
    public void onSpellsInfo(CommandSender sender, SpellType spellType) {
        Behavior behavior = spellType.getBehavior();
        if (behavior == null) {
            sender.sendMessage(WandsPlugin.PREFIX + "Spell has no behaviour!");
        } else {
            sender.sendMessage("§e ---- §6" + spellType + "§e ----§r\n" + behavior);
        }
    }

    @Subcommand("get")
    @CommandPermission(Permissions.GET_WAND)
    public void onGet(Player player) {
        try {
            PreSetItem[] value = PreSetItem.values();
            Inventory inventory = Bukkit.createInventory(player, InventoryType.CHEST, "Wands");
            inventory.addItem(Arrays.stream(value).map(PreSetItem::getItemStack).toArray(ItemStack[]::new));
            player.openInventory(inventory);
        } catch (IllegalArgumentException e) {
            player.sendMessage(WandsPlugin.PREFIX + "Wand does not exist!");
        }
    }

    @Subcommand("get")
    @CommandPermission(Permissions.GET_WAND)
    public void onGetWand(Player player, PreSetItem item) {
        player.getInventory().addItem(item.getItemStack());
    }

    @Subcommand("getscroll")
    @CommandPermission(Permissions.GET_SCROLL)
    public void onGetScroll(Player player, SpellType spellType) {
        ItemStack itemStack = ItemBuilder.from(Material.MOJANG_BANNER_PATTERN)
                .named("§5§k#§r §f" + spellType.getCastType().getDisplayName() + " Scroll §5§k#")
                .addLore("§7Contains: " + spellType.getDisplayName())
                .withWandAffinity(spellType.getAffinityTypes())
//                                        .withLore("When in survival click on this scroll", "and drag it over to a wand", "and click on the wand to apply", "the scroll")
                .glowing()
                .hideFlags()
                .blockWandRegistration()
                .build();
        ItemUtil.setPersistentData(itemStack, "spell", PersistentDataType.INTEGER, spellType.getId());
        player.getInventory().addItem(itemStack);
    }

    @Subcommand("update check")
    @CommandPermission(Permissions.UPDATE_CHECK)
    public void onUpdateCheck(CommandSender sender) {
        WandsPlugin.getInstance().getUpdater().checkForUpdates(sender, false);
    }

    @Subcommand("update download")
    @CommandPermission(Permissions.UPDATE_CHECK)
    public void onUpdateDownload(CommandSender sender) {
        WandsPlugin.getInstance().getUpdater().checkForUpdates(sender, true);
    }

    @Subcommand("bind")
    @Description("Binds spells to the held wand.")
    @CommandPermission(Permissions.MANAGE_SPELLS)
    @CommandCompletion("@unbound_spells")
    public void onBind(Player player, SpellType spellType) {
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (CommandUtils.isWandOrNotify(player, itemStack)) {
            String itemName = ItemUtil.getName(itemStack);
            Set<SpellType> compound = SpellCompound.getCompound(itemStack);
            if (compound.add(spellType)) {
                SpellCompound.apply(compound, itemStack);
                player.sendMessage(WandsPlugin.PREFIX + "Successfully added §7§l" + spellType.getDisplayName() + "§r to " + itemName);
            } else {
                player.sendMessage(WandsPlugin.PREFIX + itemName + "§r already contains §7§l" + spellType);
            }
        }
    }

    @Subcommand("bindall")
    @Description("Binds all spells to the held wand.")
    @CommandPermission(Permissions.MANAGE_SPELLS)
    public void onBindAll(Player player) {
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (CommandUtils.isWandOrNotify(player, itemStack)) {
            Set<SpellType> compound = SpellCompound.getCompound(itemStack);
            compound.addAll(Arrays.asList(SpellType.values()));
            SpellCompound.apply(compound, itemStack);
            player.sendMessage(WandsPlugin.PREFIX + "Successfully added all spells to " + ItemUtil.getName(itemStack));
        }
    }

    @Subcommand("unbind")
    @Description("Unbinds spells from the held wand.")
    @CommandPermission(Permissions.MANAGE_SPELLS)
    @CommandCompletion("@bound_spells")
    public void onUnBind(Player player, SpellType spellType) {
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (CommandUtils.isWandOrNotify(player, itemStack)) {
            String itemName = ItemUtil.getName(itemStack);
            Set<SpellType> compound = SpellCompound.getCompound(itemStack);
            if (compound.remove(spellType)) {
                SpellCompound.apply(compound, itemStack);
                player.sendMessage(WandsPlugin.PREFIX + "Successfully removed §7§l" + spellType.getDisplayName() + "§r from " + itemName);
            } else {
                player.sendMessage(WandsPlugin.PREFIX + itemName + "§r doesn't contain spell §7§l" + spellType);
            }
        }
    }

    @Subcommand("unbindall")
    @Description("Unbinds spells from the held wand.")
    @CommandPermission(Permissions.MANAGE_SPELLS)
    public void onUnBind(Player player) {
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (CommandUtils.isWandOrNotify(player, itemStack)) {
            String itemName = ItemUtil.getName(itemStack);
            Set<SpellType> compound = SpellCompound.getCompound(itemStack);
            if (!compound.isEmpty()) {
                compound.clear();
                SpellCompound.apply(compound, itemStack);
                player.sendMessage(WandsPlugin.PREFIX + "Successfully removed all spells from " + itemName);
            } else {
                player.sendMessage(WandsPlugin.PREFIX + itemName + " §ris already empty!");
            }
        }
    }

    @Subcommand("create")
    @Description("Transforms held item into a wand. You can use & color codes.")
    @CommandPermission(Permissions.CREATE_WAND)
    public void onCreateWand(Player player, String name) {
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (itemStack.getType() != Material.AIR) {
            if (ItemTag.IS_WAND.isTagged(itemStack)) {
                player.sendMessage(WandsPlugin.PREFIX + ItemUtil.getName(itemStack) + "§r is already a wand!");
            } else {
                if (!ItemTag.CANNOT_REGISTER.isTagged(itemStack)) {
                    ItemTag.IS_WAND.tag(itemStack);
                    ItemUtil.setName(itemStack, name);
                    player.sendMessage(WandsPlugin.PREFIX + ItemUtil.getName(itemStack) + "§r is registered as wand");
                    player.sendMessage(WandsPlugin.PREFIX + "use §8/bind <spell>§r to bind spells");
                } else {
                    player.sendMessage(WandsPlugin.PREFIX + ItemUtil.getName(itemStack) + "§r can't be registered!");
                }
            }
        } else {
            player.sendMessage(WandsPlugin.PREFIX + "You are not holding an item!");
        }
    }

    @Subcommand("rename")
    @Description("Renames held wand. You can use & color codes.")
    @CommandPermission(Permissions.CREATE_WAND)
    public void onRenameWand(Player player, String name) {
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (itemStack.getType() != Material.AIR) {
            String displayName = ItemUtil.getName(itemStack);
            if (ItemTag.IS_WAND.isTagged(itemStack)) {
                ItemUtil.setName(itemStack, name);
                player.sendMessage(WandsPlugin.PREFIX + "§ritem has been renamed to " + ItemUtil.getName(itemStack));
            } else {
                player.sendMessage(WandsPlugin.PREFIX + displayName + "§r is not a wand");
            }
        } else {
            player.sendMessage(WandsPlugin.PREFIX + "You are not holding an item!");
        }
    }


    @Subcommand("clear")
    @Description("Transforms wand into normal item.")
    @CommandPermission(Permissions.CREATE_WAND)
    public void onClearWand(Player player) {
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (CommandUtils.isWandOrNotify(player, itemStack)) {
            SpellInteractionUtil.undoWand(itemStack);
            ItemUtil.setName(itemStack, "");
            player.sendMessage(WandsPlugin.PREFIX + "Successfully reverted " + itemStack.getType() + " to initial state");
        }
    }

    @Subcommand("settings")
    @CommandPermission(Permissions.SETTINGS)
    public class Settings extends BaseCommand {
        @Default
        public void onSettings(CommandSender sender) {
            sender.sendMessage(WandsPlugin.PREFIX_TOP);
            sender.sendMessage("magic cooldown time:§r §a" + quantify(configHandler.getGlobalSpellCooldown(), "second"));
            sender.sendMessage("allow magic use:§r " + (configHandler.isMagicEnabled() ? "§atrue" : "§cfalse"));
            sender.sendMessage("wands usage requires permission:§r " + (configHandler.doesCastingRequirePermission() ? "§atrue" : "§cfalse"));
            sender.sendMessage("send update notifications:§r " + (configHandler.areNotificationsEnabled() ? "§atrue" : "§cfalse"));
        }

        @Subcommand("cooldown_global")
        @CommandPermission(Permissions.SETTINGS_COOLDOWN)
        public void onCooldown(CommandSender sender) {
            int cooldown = configHandler.getGlobalSpellCooldown();
            sender.sendMessage(WandsPlugin.PREFIX + "Global cooldown is " + quantify(cooldown, "second"));
        }

        @Subcommand("cooldown_global")
        @CommandPermission(Permissions.SETTINGS_COOLDOWN)
        public void onCooldown(CommandSender sender, int cooldown) {
            if (CommandUtils.isInRangeOrNotify(sender, cooldown)) {
                configHandler.setGlobalSpellCooldown(cooldown);
                sender.sendMessage(WandsPlugin.PREFIX + "Global cooldown has been set to " + quantify(cooldown, "second"));
            }
        }

        @Subcommand("cooldown_spell")
        @CommandPermission(Permissions.SETTINGS_COOLDOWN)
        public void onCooldown(CommandSender sender, SpellType spellType) {
            int cooldown = spellType.getBehavior().getCooldown();
            sender.sendMessage(WandsPlugin.PREFIX + "Cooldown of §7§l" + spellType.getDisplayName() + "§r is " + quantify(cooldown, "second"));
        }

        @Subcommand("cooldown_spell")
        @Description("Tweak the cooldown of a spell.")
        @CommandPermission(Permissions.SETTINGS_COOLDOWN)
        public void onCooldown(CommandSender sender, SpellType spellType, int cooldown) {
            int total = cooldown + configHandler.getGlobalSpellCooldown();
            String message = WandsPlugin.PREFIX + "§7§l" + spellType.getDisplayName() + "§r's total cooldown is now " + quantify(total, "second");
            if (CommandUtils.isInRangeOrNotify(sender, cooldown)) {
                configHandler.setSpellCooldown(spellType, cooldown);
                sender.sendMessage(message);
            }
        }

        @Subcommand("restrict")
        @CommandPermission(Permissions.SETTINGS_RESTRICT)
        @CommandCompletion("true|false")
        public void onRestrict(CommandSender sender, boolean restrict) {
            if (restrict) {
                sender.sendMessage(WandsPlugin.PREFIX + "Only players with §7" + Permissions.USE + " §rcan use wands now");
            } else {
                sender.sendMessage(WandsPlugin.PREFIX + "All players can use wands now");
            }
            configHandler.requirePermissionForCasting(restrict);
        }

        @Subcommand("notifications")
        @CommandPermission(Permissions.SETTINGS_NOTIFICATIONS)
        @CommandCompletion("true|false")
        public void onNotifications(CommandSender sender, boolean notifications) {
            if (configHandler.enableNotifications(notifications)) {
                sender.sendMessage(WandsPlugin.PREFIX + (notifications ? "Enabled" : "Disabled") + " update notifications");
            } else {
                if (notifications) {
                    sender.sendMessage(WandsPlugin.PREFIX + "Notifications are already enabled");
                } else {
                    sender.sendMessage(WandsPlugin.PREFIX + "Notifications are already disabled");
                }
            }
        }
    }
}

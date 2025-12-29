package me.dylan.wands.spell.util;

import me.dylan.wands.WandsPlugin;
import me.dylan.wands.command.Permissions;
import me.dylan.wands.config.ConfigHandler;
import me.dylan.wands.spell.BrowseParticle;
import me.dylan.wands.spell.CooldownManager;
import me.dylan.wands.spell.SpellCompound;
import me.dylan.wands.spell.SpellType;
import me.dylan.wands.spell.accessories.ItemTag;
import me.dylan.wands.spell.spellbuilders.Behavior;
import me.dylan.wands.utils.ItemUtil;
import me.dylan.wands.utils.PlayerUtil;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public final class SpellInteractionUtil {
    public static final String TAG_SPELL_BROWSE_PARTICLES = "SpellBrowseParticles";
    private static final ConfigHandler config;
    private static final CooldownManager cooldownManager;
    private static final String TAG_SPELL_INDEX = "SpellIndex";

    static {
        WandsPlugin wandsPlugin = WandsPlugin.getInstance();
        config = wandsPlugin.getConfigHandler();
        cooldownManager = wandsPlugin.getCooldownManager();
    }

    private SpellInteractionUtil() {
    }

    public static boolean canUseMagic(Player player) {
        if (config.doesCastingRequirePermission()) {
            if (!player.hasPermission(Permissions.USE)) {
                PlayerUtil.sendActionBar(player, "§cinsufficient permissions");
                return false;
            }
        }
        return true;
    }

    public static void undoWand(ItemStack itemStack) {
        ItemTag.IS_WAND.unTag(itemStack);
        ItemUtil.removePersistentData(itemStack, TAG_SPELL_INDEX);
        ItemUtil.removePersistentData(itemStack, SpellCompound.TAG_SPELLS_LIST);
        ItemUtil.removePersistentData(itemStack, TAG_SPELL_BROWSE_PARTICLES);
    }

    private static void setIndex(ItemStack itemStack, int index) {
        ItemUtil.setPersistentData(itemStack, TAG_SPELL_INDEX, PersistentDataType.INTEGER, index);
    }

    private static int getIndex(ItemStack itemStack) {
        return ItemUtil.getPersistentData(itemStack, TAG_SPELL_INDEX, PersistentDataType.INTEGER)
                .orElse(0);
    }

    private static Optional<BrowseParticle> getSpellBrowseParticle(ItemStack itemStack) {
        Optional<String> stringOptional = ItemUtil.getPersistentData(itemStack, TAG_SPELL_BROWSE_PARTICLES, PersistentDataType.STRING);
        if (stringOptional.isPresent()) {
            try {
                return Optional.of(BrowseParticle.valueOf(stringOptional.get()));
            } catch (IllegalArgumentException ignored) {
            }
        }
        return Optional.empty();
    }

    public static @Nullable SpellType getSelectedSpell(ItemStack itemStack) {
        int[] indices = SpellCompound.getIndices(itemStack);
        if (indices.length == 0) return null;
        int index = getIndex(itemStack);
        if (index >= indices.length || index < 0) {
            setIndex(itemStack, 0);
            index = 0;
        }
        return SpellType.getSpellById(indices[index]);
    }

    public static void nextSpell(Player player, ItemStack itemStack) {
        int[] indices = SpellCompound.getIndices(itemStack);
        int length = indices.length;
        if (length == 0) {
            PlayerUtil.sendActionBar(player, WandsPlugin.PREFIX + "No spells are bound!");
            return;
        }

        length--;

        int index = getIndex(itemStack);
        if (player.isSneaking()) {
            index = index >= 1 ? --index : length;
        } else {
            index = index < length ? ++index : 0;
        }

        setIndex(itemStack, index);

        SpellType spell = SpellType.getSpellById(indices[index]);

        if (spell != null) {
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.MASTER, 0.5F, 0.5F);
            PlayerUtil.sendActionBar(player, "§6Selected " + spell.getCastType().getDisplayName() + ": §7§l" + spell.getDisplayName());
            getSpellBrowseParticle(itemStack).orElse(BrowseParticle.DEFAULT).displayAt(player.getLocation());
        }
    }

    public static void showSelectedSpell(Player player, ItemStack itemStack) {
        SpellType spell = getSelectedSpell(itemStack);
        if (spell != null) {
            PlayerUtil.sendActionBar(player, "§6Selected " + spell.getCastType().getDisplayName() + ": §7§l" + spell.getDisplayName());
        }
    }

    public static void castSpell(@NotNull Player caster, String wandDisplayName, @NotNull SpellType spell) {
        if (cooldownManager.canCast(caster, spell)) {
            Behavior behavior = spell.getBehavior();
            if (behavior != null) {
                if (behavior.cast(caster, wandDisplayName)) {
                    cooldownManager.updateLastUsed(caster);
                }
            } else {
                PlayerUtil.sendActionBar(caster, "No behaviour found for spell!");
            }
        }
    }
}

package me.dylan.wands.spell.util;

import me.dylan.wands.WandsPlugin;
import me.dylan.wands.config.ConfigurableData;
import me.dylan.wands.spell.BrowseParticle;
import me.dylan.wands.spell.CooldownManager;
import me.dylan.wands.spell.SpellCompound;
import me.dylan.wands.spell.SpellType;
import me.dylan.wands.spell.accessories.ItemTag;
import me.dylan.wands.spell.types.Behavior;
import me.dylan.wands.utils.ItemUtil;
import me.dylan.wands.utils.PlayerUtil;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public final class SpellInteractionUtil {
    public static final String TAG_SPELL_BROWSE_PARTICLES = "SpellBrowseParticles";
    private static final ConfigurableData config;
    private static final CooldownManager cooldownManager;
    private static final String TAG_SPELL_INDEX = "SpellIndex";
    private static final String TAG_SPELLS_LIST = "Spells";

    static {
        WandsPlugin wandsPlugin = JavaPlugin.getPlugin(WandsPlugin.class);
        config = wandsPlugin.getConfigurableData();
        cooldownManager = wandsPlugin.getCooldownManager();
    }

    private SpellInteractionUtil() {
        throw new UnsupportedOperationException("Instantiating util class");
    }

    public static boolean canUse(Player player) {
        if (config.doesCastingRequirePermission()) {
            if (!player.hasPermission("wands.use")) {
                PlayerUtil.sendActionBar(player, "§cinsufficient permissions");
                return false;
            }
        }
        return true;
    }

    public static void undoWand(ItemStack itemStack) {
        ItemTag.IS_WAND.untag(itemStack);
        ItemUtil.removePersistentData(itemStack, TAG_SPELL_INDEX);
        ItemUtil.removePersistentData(itemStack, TAG_SPELLS_LIST);
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
        SpellCompound compound = new SpellCompound(itemStack);
        if (compound.isEmpty()) return null;
        int index = getIndex(itemStack);
        if (index <= compound.size()) {
            return compound.get(index);
        } else {
            setIndex(itemStack, 0);
            return compound.get(0);
        }
    }

    public static void nextSpell(Player player, ItemStack itemStack) {
        List<SpellType> spells = new SpellCompound(itemStack).getSpells();
        int length = spells.size();
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

        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.MASTER, 0.5F, 0.5F);
        PlayerUtil.sendActionBar(player, "§6Current spell: §7§l" + spells.get(index).name);
        getSpellBrowseParticle(itemStack).ifPresent(particle -> particle.displayAt(player.getLocation()));
    }

    public static void showSelectedSpell(Player player, ItemStack itemStack) {
        SpellType spell = getSelectedSpell(itemStack);
        if (spell != null) {
            PlayerUtil.sendActionBar(player, "§6Current spell: §7§l" + spell.name);
        }
    }

    public static void castSpell(@NotNull Player caster, String wandDisplayName, @NotNull SpellType spell) {
        if (cooldownManager.canCast(caster, spell)) {
            Behavior behavior = spell.behavior;
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

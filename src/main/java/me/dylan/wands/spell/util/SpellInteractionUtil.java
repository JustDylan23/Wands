package me.dylan.wands.spell.util;

import me.dylan.wands.Main;
import me.dylan.wands.config.ConfigurableData;
import me.dylan.wands.miscellaneous.utils.ItemUtil;
import me.dylan.wands.spell.BrowseParticle;
import me.dylan.wands.spell.CooldownManager;
import me.dylan.wands.spell.ItemTag;
import me.dylan.wands.spell.SpellType;
import me.dylan.wands.spell.tools.SpellCompound;
import me.dylan.wands.spell.types.Behavior;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public final class SpellInteractionUtil {
    public static final String TAG_SPELL_BROWSE_PARTICLES = "SpellBrowseParticles";
    private static final ConfigurableData config = Main.getPlugin().getConfigurableData();
    private static final String TAG_SPELL_INDEX = "SpellIndex";
    private static final String TAG_SPELLS_LIST = "Spells";

    private SpellInteractionUtil() {
        throw new UnsupportedOperationException("Instantiating util class");
    }

    public static boolean canUse(Player player) {
        if (config.doesCastingRequirePermission()) {
            if (!player.hasPermission("wands.use")) {
                player.sendActionBar("§cinsufficient permissions");
                return false;
            }
        }
        return true;
    }

    // todo implement
    @SuppressWarnings("unused")
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

    @Nullable
    public static SpellType getSelectedSpell(ItemStack itemStack) {
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
            player.sendActionBar(Main.PREFIX + "No spells are bound!");
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
        player.sendActionBar("§6Current spell: §7§l" + spells.get(index).spellData.getDisplayName());
        getSpellBrowseParticle(itemStack).ifPresent(particle -> particle.displayAt(player.getLocation()));
    }

    public static void showSelectedSpell(Player player, ItemStack itemStack) {
        SpellType spell = getSelectedSpell(itemStack);
        if (spell != null) {
            player.sendActionBar("§6Current spell: §7§l" + spell.spellData.getDisplayName());
        }
    }

    public static void castSpell(@NotNull Player caster, String wandDisplayName, @NotNull SpellType spell) {
        CooldownManager cooldownManager = Main.getPlugin().getCooldownManager();
        if (cooldownManager.canCast(caster)) {
            Behavior behavior = spell.spellData.getBehavior();
            if (behavior != null) {
                if (behavior.cast(caster, wandDisplayName)) {
                    cooldownManager.updateLastUsed(caster);
                }
            } else {
                caster.sendActionBar("No behaviour found for spell!");
            }
        }
    }
}

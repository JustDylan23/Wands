package me.dylan.wands.utils;

import me.dylan.wands.Wands;
import me.dylan.wands.enums.Spell;
import me.dylan.wands.model.CastableSpell;
import me.dylan.wands.plugindata.SpellRegistry;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.Optional;

public class WandUtil {

    private static final String TAG_SPELL_INDEX = "SpellIndex";
    private static final String TAG_SPELLS_LIST = "Spells";
    private static final String TAG_VERIFIED = "IsWand";

    private static final SpellRegistry SPELL_REGISTRY = Wands.getPlugin().getSpellRegistry();

    private WandUtil() {
        throw new UnsupportedOperationException();
    }

    public static void setAsWand(ItemStack itemStack) {
        ItemUtil.setPersistentData(itemStack, TAG_VERIFIED, PersistentDataType.BYTE, (byte) 0);
        ItemUtil.setPersistentData(itemStack, TAG_SPELL_INDEX, PersistentDataType.INTEGER, 1);
    }

    public static boolean isWand(ItemStack itemStack) {
        return ItemUtil.hasPersistentData(itemStack, TAG_VERIFIED, PersistentDataType.BYTE);
    }

    public static void setSpells(ItemStack itemStack, Spell... spells) {
        int[] ids = Arrays.stream(spells).mapToInt(Spell::getId).toArray();
        ItemUtil.setPersistentData(itemStack, TAG_SPELLS_LIST, PersistentDataType.INTEGER_ARRAY, ids);
    }

    private static int getIndex(ItemStack itemStack) {
        return ItemUtil.getPersistentData(itemStack, TAG_SPELL_INDEX, PersistentDataType.INTEGER)
                .orElse(0);
    }

    private static void setIndex(ItemStack itemStack, int index) {
        ItemUtil.setPersistentData(itemStack, TAG_SPELL_INDEX, PersistentDataType.INTEGER, index);
    }

    private static CastableSpell[] getSpells(ItemStack itemStack) {
        int[] spells = ItemUtil.getPersistentData(itemStack, TAG_SPELLS_LIST, PersistentDataType.INTEGER_ARRAY)
                .orElse(new int[]{});
        return Arrays.stream(spells).mapToObj(SPELL_REGISTRY::getSpell).toArray(CastableSpell[]::new);
    }

    private static Optional<CastableSpell> getSelectedSpell(ItemStack itemStack) {
        CastableSpell[] spells = getSpells(itemStack);
        int index = getIndex(itemStack);
        if (index < spells.length) return Optional.of(spells[index]);
        setIndex(itemStack, 0);
        return Optional.empty();
    }

    public static void castSpell(Player player, ItemStack itemStack) {
        Optional<CastableSpell> spell = getSelectedSpell(itemStack);
        if (spell.isPresent()) {
            spell.get().cast(player);
        } else {
            player.sendActionBar(Wands.PREFIX + "No spells are bound");
        }
    }

    public static void nextSpell(Player player, ItemStack itemStack) {
        int index = getIndex(itemStack);
        CastableSpell[] spells = getSpells(itemStack);
        int length = spells.length;

        if (length-- == 0) return;
        //index goes forward
        if (!player.isSneaking()) {
            index = (index < length) ? ++index : 0;
        }
        //index goes backwards
        else {
            index = (index > 1) ? --index : length;
        }

        setIndex(itemStack, index);

        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.MASTER, 0.5F, 0.5F);
        player.sendActionBar("§6Current spell: §7§l" + spells[index].getName());
    }
}

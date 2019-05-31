package me.dylan.wands.util;

import me.dylan.wands.Main;
import me.dylan.wands.spell.BaseSpell;
import me.dylan.wands.spell.Spell;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.Optional;
import java.util.StringJoiner;

public class WandUtil {

    private static final String TAG_SPELL_INDEX = "SpellIndex";
    private static final String TAG_SPELLS_LIST = "Spells";
    private static final String TAG_VERIFIED = "IsWand";

    private WandUtil() {
        throw new UnsupportedOperationException();
    }

    public static void setAsWand(ItemStack itemStack) {
        ItemUtil.setPersistentData(itemStack, TAG_VERIFIED, PersistentDataType.BYTE, (byte) 0);
        ItemUtil.setPersistentData(itemStack, TAG_SPELL_INDEX, PersistentDataType.INTEGER, 0);
    }

    public static boolean isWand(ItemStack itemStack) {
        return ItemUtil.hasPersistentData(itemStack, TAG_VERIFIED, PersistentDataType.BYTE);
    }

    public static void setSpells(ItemStack itemStack, Spell... spells) {
        StringJoiner stringJoiner = new StringJoiner(",");
        for (Spell spell : spells) {
            stringJoiner.add(spell.toString());
        }
        ItemUtil.setPersistentData(itemStack, TAG_SPELLS_LIST, PersistentDataType.STRING, stringJoiner.toString());
    }

    private static int getIndex(ItemStack itemStack) {
        return ItemUtil.getPersistentData(itemStack, TAG_SPELL_INDEX, PersistentDataType.INTEGER)
                .orElse(0);
    }

    private static void setIndex(ItemStack itemStack, int index) {
        ItemUtil.setPersistentData(itemStack, TAG_SPELL_INDEX, PersistentDataType.INTEGER, index);
    }

    private static BaseSpell[] getSpells(ItemStack itemStack) {
        Optional<String> spells = ItemUtil.getPersistentData(itemStack, TAG_SPELLS_LIST, PersistentDataType.STRING);
        if (spells.isPresent()) {
            return Spell.getSpells(spells.get());
        }
        return new BaseSpell[]{};
    }

    private static Optional<BaseSpell> getSelectedSpell(ItemStack itemStack) {
        BaseSpell[] spells = getSpells(itemStack);
        int index = getIndex(itemStack);
        if (index < spells.length) {
            return Optional.of(spells[index]);
        }
        setIndex(itemStack, 0);
        return Optional.empty();
    }

    public static void castSpell(Player player, ItemStack itemStack) {
        Optional<BaseSpell> spell = getSelectedSpell(itemStack);
        if (spell.isPresent()) {
            spell.get().cast(player);
        } else {
            player.sendActionBar(Main.PREFIX + "No spells are bound!");
        }

    }

    public static void nextSpell(Player player, ItemStack itemStack) {
        int index = getIndex(itemStack);
        BaseSpell[] spells = getSpells(itemStack);
        int length = spells.length;
        if (length-- == 0) {
            return;
        }

        if (player.isSneaking()) {
            index = (index >= 1) ? --index : length;
        } else {
            index = (index < length) ? ++index : 0;
        }

        setIndex(itemStack, index);

        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.MASTER, 0.5F, 0.5F);
        player.sendActionBar("§6Current baseSpell: §7§l" + spells[index].getName());
    }
}

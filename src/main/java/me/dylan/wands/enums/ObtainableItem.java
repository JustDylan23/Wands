package me.dylan.wands.enums;

import me.dylan.wands.customitems.ArtifactBow;
import me.dylan.wands.customitems.ArtifactDagger;
import me.dylan.wands.utils.ItemUtil;
import me.dylan.wands.utils.WandUtil;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public enum ObtainableItem {
    ASSASSINS_DAGGER(ItemUtil.getItemStack(() -> {
        ItemStack itemStack = new ItemStack(Material.MUSIC_DISC_MALL);
        ItemUtil.setName(itemStack, "&dAssassin's &8Dagger");
        ItemUtil.setPersistentData(itemStack, ArtifactDagger.ID_TAG, PersistentDataType.BYTE, (byte) 1);
        return itemStack;
    })),

    CURSED_BOW(ItemUtil.getItemStack(() -> {
        ItemStack itemStack = new ItemStack(Material.BOW);
        ItemUtil.setName(itemStack, "&cCursed Bow");
        ItemUtil.setItemMeta(itemStack, meta -> {
            meta.setUnbreakable(true);
            meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
            meta.addItemFlags(ItemFlag.values());
        });
        ItemUtil.setPersistentData(itemStack, ArtifactBow.ID_TAG, PersistentDataType.BYTE, (byte) 1);
        return itemStack;
    })),

    GOD_WAND(ItemUtil.getItemStack(() -> {
        ItemStack itemStack = new ItemStack(Material.BLAZE_ROD);
        ItemUtil.setName(itemStack, "&eGod Wand");
        WandUtil.setAsWand(itemStack);
        WandUtil.setSpells(itemStack, Spell.COMET, Spell.SPARK, Spell.CONFUSE, Spell.LAUNCH, Spell.POISON_WAVE);
        return itemStack;
    })),

    BLOOD_WAND(ItemUtil.getItemStack(() -> {
        ItemStack itemStack = new ItemStack(Material.NETHER_WART);
        ItemUtil.setName(itemStack, "&cBlood Wand");
        WandUtil.setAsWand(itemStack);
        WandUtil.setSpells(itemStack, Spell.BLOOD_WAVE, Spell.BLOOD_SPARK, Spell.BLOOD_EXPLODE, Spell.BLOOD_STUN);
        return itemStack;
    }));

    private static final Supplier<String[]> names = () -> Arrays.stream(values()).map(Enum::toString).toArray(String[]::new);

    private final ItemStack itemStack;

    ObtainableItem(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public static String[] getNames() {
        return names.get();
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
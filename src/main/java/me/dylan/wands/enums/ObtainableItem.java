package me.dylan.wands.enums;

import me.dylan.wands.utils.WandUtil;
import me.dylan.wands.wrappers.ItemWrapper;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public enum ObtainableItem {
    ASSASSINS_DAGGER(new ItemWrapper(Material.MUSIC_DISC_MALL)
            .setName("&dAssassin's &8Dagger")
            .setNbtTagInt("therosDagger", 1)
            .setItemMeta(meta -> meta.addItemFlags(ItemFlag.values()))
            .getItemStack()
    ),

    CURSED_BOW(new ItemWrapper(Material.BOW)
            .setName("&cCursed Bow")
            .setNbtTagInt("empireBow", 1)
            .setItemMeta(meta -> {
                meta.setUnbreakable(true);
                meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
                meta.addItemFlags(ItemFlag.values());
            })
            .getItemStack()
    ),

    GOD_WAND(new WandUtil.Builder(Material.BLAZE_ROD,
            Spell.COMET, Spell.SPARK, Spell.CONFUSE, Spell.LAUNCH, Spell.POISON_WAVE)
            .setName("&eGod Wand")
            .getItemStack()
    ),

    BLOOD_WAND(new WandUtil.Builder(Material.NETHER_WART,
            Spell.BLOOD_WAVE, Spell.BLOOD_SPARK, Spell.BLOOD_EXPLODE, Spell.BLOOD_STUN)
            .setName("&cBlood Wand")
            .getItemStack()
    );

    private static final String[] names;

    static {
        names = Arrays.stream(values()).map(Enum::toString).toArray(String[]::new);
    }

    private final ItemStack itemStack;

    ObtainableItem(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public static String[] getNames() {
        return names;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
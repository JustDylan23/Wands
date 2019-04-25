package me.dylan.wands;

import me.dylan.wands.spellfoundation.Spell;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;

public enum ObtainableItem {
    ASSASSINS_DAGGER(new ItemWrapper(Material.MUSIC_DISC_MALL).builder(builder -> {
        builder.setName("&dAssassin's &8Dagger");
        builder.setNbtTagInt("therosDagger", 1);
        builder.setItemMeta(meta -> meta.addItemFlags(ItemFlag.values()));
    }).getItemStack()),

    CURSED_BOW(new ItemWrapper(Material.BOW).builder(builder -> {
        builder.setName("&cCursed Bow");
        builder.setNbtTagInt("empireBow", 1);
        builder.setItemMeta(meta -> {
            meta.setUnbreakable(true);
            meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
            meta.addItemFlags(ItemFlag.values());
        });
    }).getItemStack()),

    GOD_WAND(new WandWrapper.Builder(Material.BLAZE_ROD).builder(builder -> builder.setName("&eGod Wand"))
            .setSpells(Spell.values()).getItemStack()),

    BLOOD_WAND(new WandWrapper.Builder(Material.NETHER_WART).builder(builder -> builder.setName("&cBlood Wand"))
            .setSpells(Spell.BLOOD_WAVE, Spell.BLOOD_SPARK, Spell.BLOOD_EXPLODE, Spell.BLOOD_STUN).getItemStack());

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
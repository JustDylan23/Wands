package me.dylan.wands;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

public enum MagicalItems {
    THEROS_DAGGER(new ItemUtil(Material.MUSIC_DISC_MALL).builder(builder -> {
        builder.setName("&8Theros Dagger");
        builder.setNbtTagInt("therosDagger", 1);
    }).getItemStack()),

    EMPIRE_BOW(new ItemUtil(Material.BOW).builder(builder -> {
        builder.setName("&cEmpire Bow");
        builder.setNbtTagInt("empireBow", 1);
        builder.setItemMeta(meta -> {
            meta.setUnbreakable(true);
            meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
        });
    }).getItemStack()),

    EMPIRE_WAND(new WandItem.Builder(Material.BLAZE_ROD).builder(builder -> {
        builder.setName("&cEmpire Wand");
    }).setSpells(Spell.values()).getItemStack());

    private final ItemStack itemStack;

    MagicalItems(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
package me.dylan.wands;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum MagicalItems {
    THEROS_DAGGER(new ItemUtil(new ItemStack(Material.MUSIC_DISC_MALL)).setName("&8Theros Dagger")
            .setNbtTagInt("therosDagger", 1).getItemStack()),

    EMPIRE_BOW(new WandItem(new ItemStack(Material.BOW)).setName("&eEmpire Bow")
            .setNbtTagInt("empireBow", 1).getItemStack()),

    EMPIRE_WAND(new WandItem(new ItemStack(Material.BLAZE_ROD)).markAsWand().setSpells(Spell.values())
            .setName("&cEmpire Wand").getItemStack());

    private final ItemStack itemStack;

    MagicalItems(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
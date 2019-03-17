package me.dylan.wands;

import me.dylan.wands.SpellFoundation.Spell;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

public enum InGameItems {
    THEROS_DAGGER(new ItemUtil(Material.MUSIC_DISC_MALL).builder(builder -> {
        builder.setName("&dAssasin's &8Dagger");
        builder.setNbtTagInt("therosDagger", 1);
        builder.setItemMeta(meta -> meta.addItemFlags(ItemFlag.values()));
    }).getItemStack()),

    EMPIRE_BOW(new ItemUtil(Material.BOW).builder(builder -> {
        builder.setName("&cCursed Bow");
        builder.setNbtTagInt("empireBow", 1);
        builder.setItemMeta(meta -> {
            meta.setUnbreakable(true);
            meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
            meta.addItemFlags(ItemFlag.values());
        });
    }).getItemStack()),

    EMPIRE_WAND(new WandItem.Builder(Material.BLAZE_ROD).builder(builder -> builder.setName("&eGod Wand"))
            .setSpells(Spell.values()).getItemStack()),

    BLOOD_WAND(new WandItem.Builder(Material.NETHER_WART).builder(builder -> builder.setName("&cBlood Wand"))
    .setSpells(Spell.BLOOD_WAVE, Spell.BLOOD_SPARK).getItemStack());

    private final ItemStack itemStack;

    InGameItems(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }
}
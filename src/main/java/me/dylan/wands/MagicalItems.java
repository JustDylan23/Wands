package me.dylan.wands;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum MagicalItems {
    THEROS_DAGGER {
        @Override
        public ItemStack getItemStack() {
            ItemUtil therosDagger = new ItemUtil(new ItemStack(Material.MUSIC_DISC_MALL));
            therosDagger.setNbtTagInt("therosdagger", 1);
            return therosDagger.setName("&8Theros Dagger").getItemStack();
        }
    },
    EMPIRE_WAND {
        @Override
        public ItemStack getItemStack() {
            WandItem item = new WandItem(new ItemStack(Material.BLAZE_ROD));
            item.setName("&cEmpire Wand");
            return item.markAsWand().setSpells(1, 2, 3, 4).getItemStack();
        }
    };

    public abstract ItemStack getItemStack();
}
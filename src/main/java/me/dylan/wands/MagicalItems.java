package me.dylan.wands;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum MagicalItems {
    THEROS_DAGGER {
        @Override
        public ItemStack getItemStack() {
            return new ItemUtil(new ItemStack(Material.MUSIC_DISC_MALL)).setName("&8Theros Dagger")
                    .setNbtTagInt("therosDagger", 1).getItemStack();
        }
    },
    EMPIRE_BOW {
        @Override
        public ItemStack getItemStack() {
            return new WandItem(new ItemStack(Material.BOW)).setName("&eEmpire Bow")
                    .setNbtTagInt("empireBow", 1).getItemStack();
        }
    },
    EMPIRE_WAND {
        @Override
        public ItemStack getItemStack() {
            return new WandItem(new ItemStack(Material.BLAZE_ROD)).markAsWand().setSpells(1, 2, 3, 4)
                    .setName("&cEmpire Wand").getItemStack();
        }
    };

    public abstract ItemStack getItemStack();
}
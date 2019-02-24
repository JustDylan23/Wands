package me.dylan.wands;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum MagicalItems {
    THEROS_DAGGER {
        private final ItemStack itemStack = new ItemUtil(new ItemStack(Material.MUSIC_DISC_MALL)).setName("&8Theros Dagger")
                .setNbtTagInt("therosDagger", 1).getItemStack();

        @Override
        public ItemStack getItemStack() {
            return itemStack;
        }
    },
    EMPIRE_BOW {
        private final ItemStack itemStack = new WandItem(new ItemStack(Material.BOW)).setName("&eEmpire Bow")
                .setNbtTagInt("empireBow", 1).getItemStack();

        @Override
        public ItemStack getItemStack() {
            return itemStack;
        }
    },
    EMPIRE_WAND {
        private final ItemStack itemStack = new WandItem(new ItemStack(Material.BLAZE_ROD)).markAsWand().setSpells(Spell.values())
                .setName("&cEmpire Wand").getItemStack();

        @Override
        public ItemStack getItemStack() {
            return itemStack;
        }
    };

    public abstract ItemStack getItemStack();
}
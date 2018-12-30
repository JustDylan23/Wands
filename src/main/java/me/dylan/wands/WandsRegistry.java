package me.dylan.wands;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class WandsRegistry {

    private final Map<Integer, ItemStack> wandsRegister = new HashMap<>();
    private final Map<Integer, int[]> spellRegister = new HashMap<>();

    public void registerWand(ItemStack wand, int index) {
        wandsRegister.put(index, wand);
    }

    public ItemStack getWand(int index) {
        return wandsRegister.get(index);
    }

    public void bindSpells(int wandID, int... spellIDs) {
        spellRegister.put(wandID, spellIDs);
    }

    public int[] getSpellIDs(int wandID) {
        return spellRegister.get(wandID);
    }
}

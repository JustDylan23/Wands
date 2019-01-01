package me.dylan.wands;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public final class WandItem extends AdvancedItemStack {

   private String spellIndexTag = "SpellIndex";
   private String spellsListTag= "Spells";
   private String verifiedTag = "VerifiedAsWand";

    public WandItem(ItemStack itemStack) {
        super(itemStack);
    }

    public WandItem(Material material) {
        super(material);
    }

    public WandItem setSpellIndex(int index) {
        setNBTTag(spellIndexTag, index);
        return this;
    }

    public int getSpellIndex() {
        return getNBTTagInt(spellIndexTag, null);
    }

    public WandItem setSpells(int... spells) {
        setNBTTagIntArray(spellsListTag, spells);
        return this;
    }

    public Map<Integer, Spell> getSpells() {
        int[] spells = getNBTTagIntArray(spellsListTag);
        Map<Integer, Spell> spellHashMap = new HashMap<>();
        SpellRegistry spellRegistry = Wands.getInstance().getSpellRegistry();
        int i = 0;
        for (int spellID : spells) {
            spellHashMap.put(++i, spellRegistry.getSpell(spellID));
        }
        return spellHashMap;
    }

    public int getSpellSize() {
        return getNBTTagIntArray(spellsListTag).length;
    }

    public Spell getSelectedSpell() {
        return getSpells().get(getSpellIndex());
    }

    public WandItem markAsWand() {
        this.setNBTTag(verifiedTag, 1);
        return this;
    }

    public boolean isMarkedAsWand() {
        return hasNBTTag(verifiedTag);
    }
}

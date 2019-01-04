package me.dylan.wands;

import net.minecraft.server.v1_13_R2.NBTTagInt;
import net.minecraft.server.v1_13_R2.NBTTagIntArray;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public final class WandItem extends ItemUtil {

    private final String spellIndexTag = "SpellIndex";
    private final String spellsListTag = "Spells";
    private final String verifiedTag = "VerifiedAsWand";



    public WandItem(ItemStack itemStack) {
        super(itemStack);
    }

    public WandItem setSpellIndex(int index) {
        setNbtTag(spellIndexTag, new NBTTagInt(index));
        return this;
    }

    public int getSpellIndex() {
        if (hasNbtTag(spellIndexTag)) {
            return getNbtTag(tag -> tag.getInt(spellIndexTag));
        }
        setSpellIndex(1);
        return 1;
    }

    public WandItem setSpells(int... spells) {
        setNbtTag(spellsListTag, new NBTTagIntArray(spells));
        return this;
    }

    public Map<Integer, Spell> getSpells() {
        int[] spells = getNbtTag(tag -> tag.getIntArray(spellsListTag));
        Map<Integer, Spell> spellHashMap = new HashMap<>();
        SpellRegistry spellRegistry = Wands.getInstance().getSpellRegistry();
        int i = 0;
        for (int spellID : spells) {
            spellHashMap.put(++i, spellRegistry.getSpell(spellID));
        }
        return spellHashMap;
    }

    public int getSpellSize() {
        return getNbtTag(tag -> tag.getIntArray(spellsListTag)).length;
    }


    public Spell getSelectedSpell() {
        return getSpells().get(getSpellIndex());
    }

    public WandItem markAsWand() {
        this.setNbtTag(verifiedTag, new NBTTagInt(1));
        return this;
    }

    public boolean isMarkedAsWand() {
        return hasNbtTag(verifiedTag);
    }
}

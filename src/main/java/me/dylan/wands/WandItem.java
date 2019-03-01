package me.dylan.wands;

import org.bukkit.inventory.ItemStack;

import java.util.*;

@SuppressWarnings({"WeakerAccess", "UnusedReturnValue", "RedundantSuppression"})
public final class WandItem extends ItemUtil<WandItem> {

    private final String spellIndexTag = "SpellIndex";
    private final String spellsListTag = "Spells";
    private final String verifiedTag = "VerifiedAsWand";

    public WandItem(ItemStack itemStack) {
        super(itemStack);
    }

    @Override
    public WandItem getInstance() {
        return this;
    }

    public WandItem setSpellIndex(int index) {
        setNbtTagInt(spellIndexTag, index);
        return this;
    }

    public int getSpellIndex() {
        if (hasNbtTag(spellIndexTag)) {
            return getNbtTag(tag -> tag.getInt(spellIndexTag));
        }
        setSpellIndex(1);
        return 1;
    }

    @Deprecated
    public WandItem setSpells(int... spells) {
        setNbtTagIntArray(spellsListTag, spells);
        return this;
    }

    public WandItem setSpells(Spell... spells) {
        List<Integer> ids = new LinkedList<>();
        for (Spell spell : spells) {
            ids.add(spell.getId());
        }
        setNbtTagIntArray(spellsListTag, ids.stream().filter(Objects::nonNull).mapToInt(Integer::valueOf).toArray());
        return this;
    }

    public Map<Integer, CastableSpell> getSpells() {
        int[] spells = getNbtTag(tag -> tag.getIntArray(spellsListTag));
        Map<Integer, CastableSpell> spellHashMap = new HashMap<>();
        SpellRegistry spellRegistry = Wands.getPlugin().getSpellRegistry();
        int i = 0;
        for (int spellId : spells) {
            spellHashMap.put(++i, spellRegistry.getSpell(spellId));
        }
        return spellHashMap;
    }

    public int getSpellSize() {
        return getNbtTag(tag -> tag.getIntArray(spellsListTag)).length;
    }


    public CastableSpell getSelectedSpell() {
        return getSpells().get(getSpellIndex());
    }

    public WandItem markAsWand() {
        this.setNbtTagInt(verifiedTag, 1);
        return this;
    }

    public boolean isMarkedAsWand() {
        return hasNbtTag(verifiedTag);
    }
}

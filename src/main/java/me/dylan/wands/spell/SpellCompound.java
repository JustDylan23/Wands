package me.dylan.wands.spell;

import me.dylan.wands.utils.ItemUtil;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SpellCompound {
    public static final String TAG_SPELLS_LIST = "Spells";

    private final ArrayList<Integer> spellIds = new ArrayList<>();

    public static int[] getIndices(ItemStack itemStack) {
        return ItemUtil.getPersistentData(itemStack, TAG_SPELLS_LIST, PersistentDataType.INTEGER_ARRAY).orElse(new int[0]);
    }

    public SpellCompound(ItemStack itemStack) {
        ItemUtil.getPersistentData(itemStack, TAG_SPELLS_LIST, PersistentDataType.INTEGER_ARRAY).ifPresent(ints -> {
            Set<Integer> integerSet = new HashSet<>();

            boolean foundInvalid = false;
            for (int id : ints) {
                if (integerSet.contains(id)) {
                    foundInvalid = true;
                } else {
                    integerSet.add(id);
                    spellIds.add(id);
                }
            }

            if (foundInvalid) {
                apply(itemStack);
            }
        });
    }

    public void apply(ItemStack itemStack) {
        if (spellIds.isEmpty()) {
            ItemUtil.removePersistentData(itemStack, TAG_SPELLS_LIST);
        } else {
            int[] array = new int[spellIds.size()];
            int i = 0;
            for (Integer spellId : spellIds) {
                array[i] = spellId;
                i++;
            }
            ItemUtil.setPersistentData(
                    itemStack,
                    TAG_SPELLS_LIST,
                    PersistentDataType.INTEGER_ARRAY,
                    array
            );
        }
    }

    public SpellType[] getSpells() {
        SpellType[] spells = new SpellType[spellIds.size()];
        int count = 0;
        for (int spellId : spellIds) {
            spells[count] = SpellType.getSpellById(spellId);
            count++;
        }
        return spells;
    }

    public SpellType get(int index) {
        return SpellType.getSpellById(spellIds.get(index));
    }

    public boolean add(@NotNull SpellType spell) {
        if (spellIds.contains(spell.id)) return false;
        spellIds.add(spell.id);
        return true;
    }

    public void addAll(@NotNull SpellType... spells) {
        for (SpellType spell : spells) {
            add(spell);
        }
    }

    public boolean remove(@NotNull SpellType spell) {
        return spellIds.remove(spell.id) != null;
    }

    public boolean clear() {
        if (isEmpty()) return false;
        spellIds.clear();
        return true;
    }

    public void addAllSpells() {
        spellIds.clear();
        for (SpellType value : SpellType.values()) {
            spellIds.add(value.id);
        }
    }

    public int size() {
        return spellIds.size();
    }

    public boolean isEmpty() {
        return spellIds.isEmpty();
    }
}

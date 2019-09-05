package me.dylan.wands.spell.tools;

import me.dylan.wands.miscellaneous.utils.ItemUtil;
import me.dylan.wands.spell.SpellType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class SpellCompound {
    private static final String TAG_SPELLS_LIST = "Spells";

    private final List<SpellType> spells = new ArrayList<>();

    public SpellCompound(ItemStack itemStack) {
        Optional<String> persistentData = ItemUtil.getPersistentData(itemStack, TAG_SPELLS_LIST, PersistentDataType.STRING);
        if (persistentData.isPresent()) {
            for (String spell : persistentData.get().split(",")) {
                SpellType spellType = SpellType.getSpellType(spell);
                if (spellType != null) {
                    spells.add(spellType);
                }
            }
        }
    }

    public void apply(ItemStack itemStack) {
        if (spells.isEmpty()) {
            ItemUtil.removePersistentData(itemStack, TAG_SPELLS_LIST);
        } else {
            StringJoiner stringJoiner = new StringJoiner(",");
            spells.forEach(spell -> stringJoiner.add(spell.toString()));
            ItemUtil.setPersistentData(itemStack, TAG_SPELLS_LIST, PersistentDataType.STRING, stringJoiner.toString());
        }
    }

    public List<@NotNull SpellType> getSpells() {
        return Collections.unmodifiableList(this.spells);
    }

    public SpellType get(int index) {
        return spells.get(index);
    }

    public boolean add(@NotNull SpellType spell) {
        if (spells.contains(spell)) return false;
        spells.add(spell);
        return true;
    }

    @SuppressWarnings("OverloadedVarargsMethod")
    public void add(@NotNull SpellType... spells) {
        for (SpellType spell : spells) {
            add(spell);
        }
    }

    public boolean remove(@NotNull SpellType spell) {
        return spells.remove(spell);
    }

    public boolean clear() {
        if (isEmpty()) return false;
        spells.clear();
        return true;
    }

    public void addAll() {
        spells.clear();
        Collections.addAll(spells, SpellType.values());
    }

    public int size() {
        return spells.size();
    }

    public boolean isEmpty() {
        return spells.isEmpty();
    }
}

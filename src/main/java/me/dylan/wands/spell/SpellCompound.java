package me.dylan.wands.spell;

import me.dylan.wands.utils.ItemUtil;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public class SpellCompound {
    public static final String TAG_SPELLS_LIST = "Spells";

    public static int[] getIndices(ItemStack itemStack) {
        return ItemUtil.getPersistentData(itemStack, TAG_SPELLS_LIST, PersistentDataType.INTEGER_ARRAY).orElse(new int[0]);
    }

    /**
     * Transforms all the spell IDs stored on the item to a usable Set.
     *
     * @param itemStack The ItemStack that will have its {@link org.bukkit.persistence.PersistentDataContainer} read for IDs.
     * @return Compound of all spells bound to the {@link ItemStack} parameter.
     */
    public static Set<@NotNull SpellType> getCompound(ItemStack itemStack) {
        int[] ids = SpellCompound.getIndices(itemStack);
        Set<SpellType> spells = new LinkedHashSet<>(ids.length);
        for (int id : ids) {
            SpellType spellType = SpellType.getSpellById(id);
            if (spellType != null) {
                spells.add(spellType);
            }
        }
        return spells;
    }

    /**
     * After changing the contents of the compound this method should be used to apply the changes.
     * the changes to the desired {@link ItemStack}
     *
     * @param spells    A Set, preferably LinkedHashSet containing spells.
     * @param itemStack {@link ItemStack} that the spell compound will be applied to.
     */
    public static void apply(Set<@Nullable SpellType> spells, ItemStack itemStack) {
        if (spells.isEmpty()) {
            ItemUtil.removePersistentData(itemStack, TAG_SPELLS_LIST);
        } else {
            spells.remove(null);
            ItemUtil.setPersistentData(
                    itemStack,
                    TAG_SPELLS_LIST,
                    PersistentDataType.INTEGER_ARRAY,
                    spells.stream().filter(Objects::nonNull).mapToInt(spell -> spell.id).toArray()
            );
        }
    }
}

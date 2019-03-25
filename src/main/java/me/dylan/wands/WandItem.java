package me.dylan.wands;

import me.dylan.wands.spellfoundation.CastableSpell;
import me.dylan.wands.spellfoundation.Spell;
import me.dylan.wands.spellfoundation.SpellRegistry;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@SuppressWarnings({"WeakerAccess", "UnusedReturnValue", "RedundantSuppression"})
public final class WandItem extends ItemUtil {

    private static final String TAG_SPELL_INDEX = "SpellIndex";
    private static final String TAG_SPELLS_LIST = "Spells";
    private static final String TAG_VERIFIED = "IsWand";

    private WandItem(ItemStack itemStack) {
        super(itemStack);
    }

    @Nullable
    public static WandItem wrapIfWand(ItemStack itemStack) {
        WandItem wandItem = new WandItem(itemStack);
        if (wandItem.isMarkedAsWand()) return wandItem;
        return null;
    }

    public int getSpellIndex() {
        if (hasNbtTag(TAG_SPELL_INDEX)) {
            return getNbtTag(tag -> tag.getInt(TAG_SPELL_INDEX));
        }
        setSpellIndex(1);
        return 1;
    }

    public WandItem setSpellIndex(int index) {
        setNbtTagInt(TAG_SPELL_INDEX, index);
        return this;
    }

    public Map<Integer, CastableSpell> getSpells() {
        int[] spells = getNbtTag(tag -> tag.getIntArray(TAG_SPELLS_LIST));
        Map<Integer, CastableSpell> spellHashMap = new HashMap<>();
        SpellRegistry spellRegistry = Wands.getPlugin().getSpellRegistry();
        int i = 0;
        for (int spellId : spells) {
            spellHashMap.put(++i, spellRegistry.getSpell(spellId));
        }
        return spellHashMap;
    }

    public int getSpellCount() {
        return getNbtTag(tag -> tag.getIntArray(TAG_SPELLS_LIST)).length;
    }

    public CastableSpell getSelectedSpell() {
        return getSpells().get(getSpellIndex());
    }

    public boolean isMarkedAsWand() {
        return hasNbtTag(TAG_VERIFIED);
    }

    public void castSpell(Player player) {
        CastableSpell spell = getSelectedSpell();
        if (spell != null) {
            spell.cast(player);
        }
    }

    public void nextSpell(Player player) {
        int index = getSpellIndex();
        int maxValue = getSpellCount();

        if (maxValue == 0) return;

        if (!player.isSneaking()) {
            index = index < maxValue ? index + 1 : 1;
        } else {
            index = index > 1 ? index - 1 : maxValue;
        }
        setSpellIndex(index);

        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.MASTER, 0.5F, 0.5F);
        player.sendActionBar("§6Current spell: §7§l" + getSelectedSpell().getName());
    }

    public static class Builder extends ItemUtil {
        public Builder(ItemStack itemStack) {
            super(itemStack);
            setNbtTagInt(TAG_VERIFIED, 1);
        }

        public Builder(Material material) {
            this(new ItemStack(material));
        }

        public Builder setSpells(Spell... spells) {
            int[] ids = new int[spells.length];
            for (int i = 0; i < spells.length; ++i)
                ids[i] = spells[i].getId();
            setNbtTagIntArray(TAG_SPELLS_LIST, ids);
            return this;
        }

        @Override
        public Builder builder(Consumer<ItemUtil> consumer) {
            consumer.accept(this);
            return this;
        }

        public WandItem build() {
            return new WandItem(getItemStack());
        }
    }
}

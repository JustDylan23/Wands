package me.dylan.wands.wrappers;

import me.dylan.wands.Wands;
import me.dylan.wands.enums.Spell;
import me.dylan.wands.spellfoundation.CastableSpell;
import me.dylan.wands.spellfoundation.SpellRegistry;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings({"WeakerAccess", "UnusedReturnValue", "RedundantSuppression"})
public final class WandWrapper extends ItemWrapper {

    private static final String TAG_SPELL_INDEX = "SpellIndex";
    private static final String TAG_SPELLS_LIST = "Spells";
    private static final String TAG_VERIFIED = "IsWand";

    private WandWrapper(ItemStack itemStack) {
        super(itemStack);
    }

    public static Optional<WandWrapper> wrapIfWand(ItemStack itemStack) {
        WandWrapper wandWrapper = new WandWrapper(itemStack);
        if (wandWrapper.isMarkedAsWand()) return Optional.of(wandWrapper);
        return Optional.empty();
    }

    public int getSpellIndex() {
        if (hasNbtTag(TAG_SPELL_INDEX)) {
            return getNbtTag(tag -> tag.getInt(TAG_SPELL_INDEX));
        }
        setSpellIndex(1);
        return 1;
    }

    public WandWrapper setSpellIndex(int index) {
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

    public Optional<CastableSpell> getSelectedSpell() {
        return Optional.ofNullable(getSpells().get(getSpellIndex()));
    }

    public boolean isMarkedAsWand() {
        return hasNbtTag(TAG_VERIFIED);
    }

    public void castSpell(Player player) {
        getSelectedSpell().ifPresent(spell -> spell.cast(player));
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
        Optional<CastableSpell> selectedSpell = getSelectedSpell();
        String spellName = selectedSpell.isPresent() ? selectedSpell.get().getName() : "";
        player.sendActionBar("§6Current spell: §7§l" + spellName);
    }

    public static final class Builder extends ItemWrapper {
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
    }
}

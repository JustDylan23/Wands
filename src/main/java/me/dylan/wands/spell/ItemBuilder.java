package me.dylan.wands.spell;

import me.dylan.wands.spell.accessories.ItemTag;
import me.dylan.wands.spell.util.SpellInteractionUtil;
import me.dylan.wands.utils.ItemUtil;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public final class ItemBuilder {
    private final ItemStack itemStack;

    private ItemBuilder(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public static ItemBuilder from(Material material) {
        return new ItemBuilder(new ItemStack(material));
    }

    public ItemBuilder tag(ItemTag itemTag) {
        itemTag.tag(itemStack);
        return this;
    }

    public ItemBuilder blockWandRegistration() {
        ItemTag.CANNOT_REGISTER.tag(itemStack);
        return this;
    }

    public ItemBuilder named(String name) {
        ItemUtil.setName(itemStack, name);
        return this;
    }

    public ItemBuilder flavorText(String... text) {
        ItemUtil.setItemMeta(itemStack, meta -> meta.setLore(Arrays.asList(text)));
        return this;
    }

    public ItemBuilder withSpells(SpellType... spells) {
        ItemTag.IS_WAND.tag(itemStack);
        SpellCompound compound = new SpellCompound(itemStack);
        compound.addAll(spells);
        compound.apply(itemStack);
        return this;
    }

    public ItemBuilder withSpellBrowseParticles(@NotNull BrowseParticle browseParticles) {
        ItemUtil.setPersistentData(itemStack, SpellInteractionUtil.TAG_SPELL_BROWSE_PARTICLES, PersistentDataType.STRING, browseParticles.toString());
        return this;
    }

    public ItemBuilder enchant(Enchantment enchantment, int level, boolean ignoreLevelRestriction) {
        ItemUtil.setItemMeta(itemStack, meta -> meta.addEnchant(enchantment, level, ignoreLevelRestriction));
        return this;
    }

    public ItemBuilder glowing() {
        ItemUtil.setItemMeta(itemStack, meta -> {
            meta.addEnchant(Enchantment.LURE, 0, true);
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        });
        return this;
    }

    public ItemBuilder hideFlags() {
        ItemUtil.setItemMeta(itemStack, meta -> meta.addItemFlags(ItemFlag.values()));
        return this;
    }

    public ItemBuilder unbreakable() {
        ItemUtil.setItemMeta(itemStack, meta -> meta.setUnbreakable(true));
        return this;
    }

    public ItemStack build() {
        return itemStack;
    }
}

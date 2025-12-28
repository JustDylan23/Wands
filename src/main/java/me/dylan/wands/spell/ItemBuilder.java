package me.dylan.wands.spell;

import me.dylan.wands.spell.accessories.ItemTag;
import me.dylan.wands.spell.spells.AffinityType;
import me.dylan.wands.spell.util.SpellInteractionUtil;
import me.dylan.wands.utils.ItemUtil;
import me.dylan.wands.utils.KeyFactory;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.*;

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

    public ItemBuilder addLore(String... text) {
        ItemUtil.setItemMeta(itemStack, meta -> {
            List<String> lore = meta.getLore();
            if (lore == null) {
                lore = new ArrayList<>();
            }
            Collections.addAll(lore, text);
            meta.setLore(lore);
        });
        return this;
    }

    public ItemBuilder withSpells(SpellType... spells) {
        ItemTag.IS_WAND.tag(itemStack);
        Set<SpellType> compound = SpellCompound.getCompound(itemStack);
        compound.addAll(Arrays.asList(spells));
        SpellCompound.apply(compound, itemStack);
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

    public ItemBuilder withoutAttackDamage() {
        ItemUtil.setItemMeta(itemStack, meta -> meta.addAttributeModifier(
                Attribute.ATTACK_DAMAGE, new AttributeModifier(KeyFactory.getOrCreateKey("attack_damage"), 0, Operation.MULTIPLY_SCALAR_1, EquipmentSlotGroup.ANY)
        ));
        return this;
    }

    public ItemStack build() {
        return itemStack;
    }

    /**
     * Intended for use on wands
     * @param affinityType type of scrolls that can be applied to wand
     * @return self
     */
    public ItemBuilder withCastAffinity(AffinityType affinityType) {
        addLore("", "§7Combat class: " + affinityType.getDisplayName());
        ItemUtil.setPersistentData(itemStack, AffinityType.PERSISTENT_DATA_KEY_WAND, PersistentDataType.INTEGER, affinityType.getId());
        return this;
    }

    /**
     * Intended for use on scrolls
     * @param affinityType types of wand scroll can be used on
     * @return self
     */
    public ItemBuilder withWandAffinity(AffinityType... affinityType) {
        int[] intArray = new int[affinityType.length];
        addLore("", "§7Affinity for classes:");
        for (int i = 0; i < affinityType.length; i++) {
            AffinityType type = affinityType[i];
            intArray[i] = type.getId();
            addLore("§7- " + type.getDisplayName());
        }
        ItemUtil.setPersistentData(itemStack, AffinityType.PERSISTENT_DATA_KEY_SCROLL, PersistentDataType.INTEGER_ARRAY, intArray);
        return this;
    }

}

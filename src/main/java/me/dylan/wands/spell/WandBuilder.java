package me.dylan.wands.spell;

import me.dylan.wands.util.ItemUtil;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class WandBuilder {
    private final ItemStack itemStack;

    private WandBuilder(Material material) {
        this.itemStack = new ItemStack(material);
        SpellManagementUtil.setAsWand(itemStack);
        ItemUtil.setItemMeta(itemStack, itemMeta -> {
            itemMeta.setUnbreakable(true);
            itemMeta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE);
        });
    }

    @NotNull

    public static WandBuilder from(Material material) {
        return new WandBuilder(material);
    }

    public WandBuilder named(String name) {
        ItemUtil.setName(itemStack, name);
        return this;
    }

    public WandBuilder glowing() {
        ItemUtil.makeGlow(itemStack);
        return this;
    }

    public WandBuilder withSpells(SpellInstance... spellInstances) {
        SpellManagementUtil.setSpells(itemStack, spellInstances);
        return this;
    }

    public WandBuilder withSpellBrowseParticles(BrowseParticle browseParticles) {
        SpellManagementUtil.setSpellBrowseParticles(itemStack, browseParticles);
        return this;
    }

    public ItemStack build() {
        SpellManagementUtil.blockModification(itemStack);
        return itemStack;
    }
}

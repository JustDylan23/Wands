package me.dylan.wands.spell;

import me.dylan.wands.miscellaneous.utils.ItemUtil;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

public enum ItemTag {
    IS_WAND("IsWand"),
    IS_DAGGER("artifact-dagger"),
    IS_CURSED_BOW("artifact-bow"),
    CANNOT_REGISTER("BlockRegistration");

    private final String tag;

    ItemTag(String tag) {
        this.tag = tag;
    }

    public boolean isTagged(ItemStack itemStack) {
        return ItemUtil.hasPersistentData(itemStack, tag, PersistentDataType.BYTE);
    }

    public void tag(ItemStack itemStack) {
        ItemUtil.setPersistentData(itemStack, tag, PersistentDataType.BYTE, (byte) 1);
    }

    public void untag(ItemStack itemStack) {
        ItemUtil.removePersistentData(itemStack, tag);
    }
}

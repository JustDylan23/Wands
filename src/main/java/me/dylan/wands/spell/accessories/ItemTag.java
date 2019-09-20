package me.dylan.wands.spell.accessories;

import me.dylan.wands.utils.ItemUtil;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

public enum ItemTag {
    IS_WAND("IsWand"),
    IS_DAGGER("artifact-dagger"),
    IS_CURSED_BOW("artifact-bow"),
    CANNOT_REGISTER("BlockRegistration");

    private final String tag;

    ItemTag(String tag) {
        this.tag = tag;
    }

    public boolean isTagged(@NotNull ItemStack itemStack) {
        return ItemUtil.hasPersistentData(itemStack, tag, PersistentDataType.BYTE);
    }

    public void tag(@NotNull ItemStack itemStack) {
        ItemUtil.setPersistentData(itemStack, tag, PersistentDataType.BYTE, (byte) 1);
    }

    public void unTag(@NotNull ItemStack itemStack) {
        ItemUtil.removePersistentData(itemStack, tag);
    }
}

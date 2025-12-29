package me.dylan.wands.command;

import co.aikar.commands.BukkitCommandCompletionContext;
import me.dylan.wands.spell.SpellCompound;
import me.dylan.wands.spell.SpellType;
import me.dylan.wands.spell.accessories.ItemTag;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class BindComplete {
    public static Collection<String> getBoundSpells(BukkitCommandCompletionContext context) {
        Set<SpellType> compound = SpellCompound.getCompound(context.getPlayer().getInventory().getItemInMainHand());
        return compound.stream().map(Enum::name).sorted().toList();
    }

    public static Collection<String> getUnboundSpells(BukkitCommandCompletionContext context) {
        ItemStack itemStack = context.getPlayer().getInventory().getItemInMainHand();
        if (ItemTag.IS_WAND.isTagged(itemStack)) {
            List<SpellType> compound = new ArrayList<>(Arrays.asList(SpellType.values()));
            compound.removeAll(SpellCompound.getCompound(itemStack));
            return compound.stream().map(Enum::name).sorted().toList();
        }
        return Collections.emptyList();
    }
}


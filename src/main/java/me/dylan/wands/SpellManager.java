package me.dylan.wands;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public final class SpellManager implements Listener {

    @EventHandler
    public void playerInteractEvent(PlayerInteractEvent event) {
        if (!Wands.ENABLED) return;
        Player player = event.getPlayer();
        ItemStack r = player.getInventory().getItemInMainHand();
        if (r != null) {
        AdvancedItemStack tool = new AdvancedItemStack(player.getInventory().getItemInMainHand());
            if (tool.hasNBTTag("verified")) {
                event.setCancelled(true);
                Action a = event.getAction();
                if (event.getHand().equals(EquipmentSlot.HAND)) {
                    if (a == Action.LEFT_CLICK_AIR || a == Action.LEFT_CLICK_BLOCK) {
                        onCast(player);
                    } else if (a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK) {
                        onSelect(player);
                    }
                }
            }
        }
    }

    private Spell getSelectedSpell(Player player) {
        AdvancedItemStack item = new AdvancedItemStack(player.getInventory().getItemInMainHand());
        return Wands.getInstance().getSpellRegistry().getSpell(item.getNBTTagInt("SpellIndex", 1));
    }

    private void setSelectedSpellIndex(Player player, int index) {
        AdvancedItemStack item = new AdvancedItemStack(player.getInventory().getItemInMainHand());
        item.setNBTTag("SpellIndex", index);
        player.getInventory().getItemInMainHand().setItemMeta(item.getItemMeta());
    }

    private void onSelect(Player player) {
        int maxValue = Wands.getInstance().getSpellRegistry().size();
        AdvancedItemStack item = new AdvancedItemStack(player.getInventory().getItemInMainHand());
        int selectorIndex = item.getNBTTagInt("SpellIndex", 0);
        if (!player.isSneaking()) {
            setSelectedSpellIndex(player, selectorIndex < maxValue ? selectorIndex + 1 : 1);
        } else {
            setSelectedSpellIndex(player, selectorIndex > 1 ? selectorIndex - 1 : maxValue);
        }
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.MASTER, 0.5F, 0.5F);
        player.sendActionBar(ChatColor.translateAlternateColorCodes('&', "&6Current spell: &7&l" + getSelectedSpell(player).getName()));
    }

    private void onCast(Player player) {
        getSelectedSpell(player).cast(player);
    }
}


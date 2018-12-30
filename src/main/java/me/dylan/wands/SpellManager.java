package me.dylan.wands;

import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public final class SpellManager implements Listener {

    private final Map<Player, Integer> spellIndex = new HashMap<>();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        spellIndex.put(event.getPlayer(), 1);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        try {
            spellIndex.remove(player);
        } catch (Exception e) {
            Wands.sendConsole("Could not remove player " + player.getDisplayName() + " from hashmap");
        }

    }

    @EventHandler
    public void playerInteractEvent(PlayerInteractEvent event) {
        if (!Wands.ENABLED) return;
        Player player = event.getPlayer();
        AdvancedItemStack tool = (AdvancedItemStack) player.getInventory().getItemInMainHand();
        if (tool != null) {
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
        spellIndex.putIfAbsent(player, 1);
        int index = spellIndex.get(player);
        return Wands.getInstance().getSpellRegistry().getSpell(index);
    }

    private void onSelect(Player player) {
        spellIndex.putIfAbsent(player, 1);
        int maxValue = Wands.getInstance().getSpellRegistry().size();
        int selectorIndex = spellIndex.get(player);
        if (!player.isSneaking()) {
            selectorIndex = selectorIndex < maxValue ? selectorIndex + 1 : 1;
        } else {
            selectorIndex = selectorIndex > 1 ? selectorIndex - 1 : maxValue;
        }

        spellIndex.put(player, selectorIndex);
        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.MASTER, 0.5F, 0.5F);
        player.sendActionBar(ChatColor.translateAlternateColorCodes('&', "&6Current spell: &7&l" + getSelectedSpell(player).getName()));
    }

    private void onCast(Player player) {
        getSelectedSpell(player).cast(player);
    }
}


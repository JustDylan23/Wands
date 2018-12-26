package me.dylan.wands;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
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

public class SpellManager implements Listener {

    private int maxValue;
    private final Map<Player, Integer> spellIndex = new HashMap<>();

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        spellIndex.put(event.getPlayer(), 1);
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        Player p = event.getPlayer();

        try {
            spellIndex.remove(p);
        } catch (Exception e) {
            Wands.sendConsole(" Could not remove player " + p.getDisplayName() + " from hashmap");
            Bukkit.getConsoleSender().sendMessage(Wands.PREFIX + " Could not remove player " + p.getDisplayName() + " from hashmap");
        }

    }

    @EventHandler
    public void playerInteractEvent(PlayerInteractEvent e) {
        if (!Wands.ENABLED) return;
        Player player = e.getPlayer();
        ItemStack tool = player.getInventory().getItemInMainHand();
        if (tool.getType().equals(Material.BLAZE_ROD)) {
            if (tool.getItemMeta().hasDisplayName()) {
                if (tool.getItemMeta().getDisplayName().equals("§cEmpire Wand")) {
                    e.setCancelled(true);
                    Action a = e.getAction();
                    if (a != Action.RIGHT_CLICK_AIR && a != Action.RIGHT_CLICK_BLOCK) {
                        if (a.equals(Action.LEFT_CLICK_AIR) || a.equals(Action.LEFT_CLICK_BLOCK)) {
                            onCast(player);
                        }
                    } else if (e.getHand().equals(EquipmentSlot.HAND)) {
                        onSelect(player);
                    }
                }
            }
        }
    }

    private void ensureIndexSet(Player player) {
        spellIndex.putIfAbsent(player, 1);
    }

    private Spell getSelectedSpell(Player player) {
        ensureIndexSet(player);
        int index = spellIndex.get(player);
        return Wands.getInstance().getSpellRegistery().getSpell(index);
    }

    private void onSelect(Player player) {
        ensureIndexSet(player);
        maxValue = Wands.getInstance().getSpellRegistery().size();
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


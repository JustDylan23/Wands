package me.dylan.wands.spells;

import me.dylan.wands.Wands;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

public class TherosDagger implements Listener {
    private boolean hasDagger(Player player) {
        ItemStack tool = player.getInventory().getItemInMainHand();
        if (tool.getType().equals(Material.MUSIC_DISC_MALL)) {
            if (tool.getItemMeta().hasDisplayName()) {
                return tool.getItemMeta().getDisplayName().equals("§8Theros Dagger");
            }
        }
        return false;
    }

    @EventHandler
    public void onSpringToggle(PlayerToggleSprintEvent e) {
        Player player = e.getPlayer();
        if (hasDagger(player)) {
            Bukkit.getScheduler().runTaskLater(Wands.getInstance(), () -> {
                springParticles(player);
            }, 1);
        }

    }

    private void springParticles(Player player) {
        if (player.isSprinting()) {
            Bukkit.getScheduler().runTaskLater(Wands.getInstance(), () -> {
                Location loc = player.getLocation();
                loc.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 1, 0.1, 0.1, 0.1, 0.1, null, true);
                loc.getWorld().spawnParticle(Particle.SMOKE_NORMAL, loc, 1, 0.1, 0.1, 0.1, 0.1, null, true);
                springParticles(player);
            }, 1);
        }
    }
}

package me.dylan.wands.artifacts;

import me.dylan.wands.AdvancedItemStack;
import me.dylan.wands.Spell;
import me.dylan.wands.Wands;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

public final class TherosDagger implements Listener {

    private boolean hasDagger(Player player) {
        ItemStack tool = player.getInventory().getItemInMainHand();
        if (tool != null) {
            AdvancedItemStack itemStack = new AdvancedItemStack(tool);
            return itemStack.hasNBTTag("therosdagger");
        }
        return false;
    }

    @EventHandler
    public void onSpringToggle(PlayerToggleSprintEvent event) {
        Player player = event.getPlayer();
        if (hasDagger(player)) {
            Bukkit.getScheduler().runTaskLater(Wands.getInstance(), () -> {
                jumpParticles(player);
            }, 1);
        }

    }

    @EventHandler
    public void onAttack(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            LivingEntity victim = (LivingEntity) event.getEntity();
            if (hasDagger(player)) {
                event.setCancelled(true);
                victim.removePotionEffect(PotionEffectType.SPEED);
                victim.removePotionEffect(PotionEffectType.BLINDNESS);
                victim.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 1, false), true);
                victim.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 5, true ), true);
            }
        }
    }

    @EventHandler
    public void onRightClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (hasDagger(player)) {
            if (event.getHand().equals(EquipmentSlot.HAND)) {
                Action a = event.getAction();
                if (a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK) {
                    Vector direction = player.getLocation().getDirection().normalize();
                    double y = 1;
                    direction.multiply(1.5).setY(y);
                    player.setVelocity(direction);
                }
            }
        }
    }

    private void jumpParticles(Player player) {
        if (player.isSprinting()) {
            Bukkit.getScheduler().runTaskLater(Wands.getInstance(), () -> {
                Location loc = player.getLocation();
                loc.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 1, 0.1, 0.1, 0.1, 0.1, null, true);
                loc.getWorld().spawnParticle(Particle.SMOKE_NORMAL, loc, 1, 0.1, 0.1, 0.1, 0.1, null, true);
                jumpParticles(player);
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 20, 4, true), true);
            }, 1);
        } else {
            player.removePotionEffect(PotionEffectType.SPEED);
        }
    }
}

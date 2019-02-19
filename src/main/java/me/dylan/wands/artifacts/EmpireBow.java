package me.dylan.wands.artifacts;

import me.dylan.wands.ItemUtil;
import me.dylan.wands.Wands;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class EmpireBow implements Listener {

    private final String chargedBow = "hasChargedBow";
    private final String cursedArrow = "cursedArrow";

    private final Plugin plugin = Wands.getInstance();

    private boolean hasBow(Player player) {
        if (!Wands.getStatus()) return false;
        ItemStack tool = player.getInventory().getItemInMainHand();
        if (tool != null) {
            ItemUtil itemUtil = new ItemUtil(tool);
            return itemUtil.hasNbtTag("empireBow");
        }
        return false;
    }

    @EventHandler
    public void takeAim(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (hasBow(player)) {
            if (event.getHand() != null) {
                if (event.getHand().equals(EquipmentSlot.HAND)) {
                    Action a = event.getAction();
                    if (a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK) {
                        player.sendMessage("1");
                        ItemStack tool = player.getInventory().getItemInMainHand();
                        player.sendMessage("2");
                        player.removeMetadata(chargedBow, plugin);
                        player.sendMessage("3");
                        ArrayList<Integer> i = new ArrayList<>();
                        i.add(0);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                i.set(0, i.get(0) + 1);
                                if (player.getInventory().getItemInMainHand() == tool) {
                                    player.sendMessage("i:" + i);
                                    if (i.get(0) == 24) {
                                        player.setMetadata(chargedBow, new FixedMetadataValue(plugin, true));
                                        player.sendMessage("charged");
                                    }
                                } else {
                                    cancel();
                                    player.sendMessage("cancelled");
                                }
                            }
                        }.runTaskTimer(Wands.getInstance(), 1, 1);
                    }
                }
            }
        }
    }

    @EventHandler
    public void shoot(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (player.hasMetadata(chargedBow)) {
                player.removeMetadata(chargedBow, plugin);
                Entity arrow = event.getProjectile();
                trail(arrow, loc -> {
                    loc.getWorld().spawnParticle(Particle.SPELL_WITCH, loc, 10, 0.5, 0.5, 0.5, 0.05, null, true);
                    loc.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, loc, 10, 0.5, 0.5, 0.5, 0.15, null, true);
                });
            }
        }
    }

    private void trail(Entity entity, Consumer<Location> consumer) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity.isValid()) {
                    consumer.accept(entity.getLocation());
                } else cancel();
            }
        }.runTaskTimer(Wands.getInstance(), 1, 1);
    }

    @EventHandler
    public void projectileHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();
        if (projectile.getShooter() instanceof Player) {
            if (projectile.hasMetadata(cursedArrow)) {
                Location loc = projectile.getLocation();
                Iterable<Damageable> entities = loc.getWorld()
                        .getNearbyEntities(loc, 3, 3, 3).stream()
                        .filter(Damageable.class::isInstance)
                        .map(Damageable.class::cast)
                        .collect(Collectors.toList());
                entities.forEach(entity -> {
                    entity.damage(6, (Entity) projectile.getShooter());
                    ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 80, 3, false));
                });
                projectile.remove();
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
            event.setCancelled(true);
        }
    }
}

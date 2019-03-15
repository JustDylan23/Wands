package me.dylan.wands.artifacts;

import me.dylan.wands.ItemUtil;
import me.dylan.wands.Wands;
import me.dylan.wands.spellbehaviour.SpellBehaviour;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class EmpireBow implements Listener {

    private final String cursedArrow = "cursedArrow";
    private final Plugin plugin = Wands.getPlugin();

    private boolean hasBow(Player player) {
        ItemStack tool = player.getInventory().getItemInMainHand();
        if (tool != null) {
            ItemUtil itemUtil = new ItemUtil(tool);
            return itemUtil.hasNbtTag("empireBow");
        }
        return false;
    }

    private final Set<Player> drawing = new HashSet<>();
    private final Set<Player> hasDrawn = new HashSet<>();

    @EventHandler
    public void onDraw(PlayerInteractEvent event) {
        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            Player player = event.getPlayer();
            if (hasBow(player)) {
                if (player.getGameMode() == GameMode.CREATIVE || player.getInventory().contains(Material.ARROW)) {
                    drawing.add(player);
                    player.sendActionBar("§6Charging [§a|§6|||]");
                    new BukkitRunnable() {
                        int count;

                        @Override
                        public void run() {
                            count++;
                            if (drawing.contains(player)) {
                                if (count == 10) player.sendActionBar("§6Charging [§a||§6||]");
                                if (count == 20) player.sendActionBar("§6Charging [§a|||§6|]");
                                if (count == 30) {
                                    player.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.MASTER, 1F, 1F);
                                    player.sendActionBar("§aCharged §6[§a||||§6]");
                                    hasDrawn.add(player);
                                    drawing.remove(player);
                                    cancel();
                                }
                            } else cancel();
                        }
                    }.runTaskTimer(Wands.getPlugin(), 1, 1);
                }
            }
        }

    }

    @EventHandler
    public void onShoot(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (drawing.contains(player)) {
                drawing.remove(player);
                player.sendActionBar("§cCancelled shot");
                event.setCancelled(true);
                return;
            }
            if (hasDrawn.contains(player)) {
                hasDrawn.remove(player);
                Entity projectile = event.getProjectile();
                projectile.setMetadata(cursedArrow, new FixedMetadataValue(plugin, true));
                Location location = player.getLocation();
                location.getWorld().playSound(location, Sound.ENTITY_ENDER_DRAGON_FLAP, SoundCategory.MASTER, 4F, 1F);
                location.getWorld().playSound(player.getLocation(), Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.MASTER, 4F, 0.1F);
                location.getWorld().spawnParticle(Particle.SPELL_WITCH, location.add(0, 1, 0), 30, 1, 1, 1, 0F, null, true);
                location.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, location.add(0, 1, 0), 100, 1, 1, 1, 0F, null, true);
                trail(projectile, loc -> {
                    loc.getWorld().spawnParticle(Particle.SPELL_WITCH, loc, 10, 0.5, 0.5, 0.5, 0.05, null, true);
                    loc.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, loc, 10, 0.5, 0.5, 0.5, 0.15, null, true);
                });
            }
        }
    }

    @EventHandler
    public void onChangeSlot(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        if (drawing.contains(player)) {
            player.sendActionBar(" ");
        }
        drawing.remove(player);
    }

    @EventHandler
    public void leave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        drawing.remove(player);
        hasDrawn.remove(player);
    }

    private void trail(Entity entity, Consumer<Location> consumer) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (entity.isValid()) {
                    consumer.accept(entity.getLocation());
                } else cancel();
            }
        }.runTaskTimer(Wands.getPlugin(), 1, 1);
    }

    @EventHandler
    public void projectileHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();
        if (projectile.getShooter() instanceof Player) {
            if (projectile.hasMetadata(cursedArrow)) {
                Location location = projectile.getLocation();
                location.getWorld().playSound(location, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, SoundCategory.MASTER, 5F, 1F);
                location.getWorld().playSound(location, Sound.ITEM_TRIDENT_RETURN, SoundCategory.MASTER, 5F, 1F);
                location.getWorld().spawnParticle(Particle.SMOKE_NORMAL, location, 30, 0.4, 0.4, 0.4, 0.2, null, true);
                SpellBehaviour.getNearbyDamageables((Player) projectile.getShooter(), location, 3)
                        .forEach(entity -> {
                            SpellBehaviour.damage(7, (Player) projectile.getShooter(), entity);
                            ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 80, 3, false), true);
                        });
                projectile.remove();
            }
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
            if (event.getDamager().hasMetadata(cursedArrow)) {
                event.setCancelled(true);
            }
        }
    }
}

package me.dylan.wands.customitems;

import me.dylan.wands.Main;
import me.dylan.wands.MouseClickListeners.ClickEvent;
import me.dylan.wands.MouseClickListeners.RightClickListener;
import me.dylan.wands.spell.SpellEffectUtil;
import me.dylan.wands.spell.SpellManagementUtil;
import me.dylan.wands.util.ItemUtil;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Consumer;

public class CursedBow implements Listener, RightClickListener {

    public final static String ID_TAG = "artifact-bow";
    private final String cursedArrow = UUID.randomUUID().toString();
    private final Main plugin = Main.getPlugin();
    private final Set<Player> drawing = new HashSet<>();
    private final Set<Player> hasDrawn = new HashSet<>();
    private final PotionEffect slow = new PotionEffect(PotionEffectType.SLOW, 60, 3, false);

    public CursedBow() {
        plugin.getMouseClickListeners().addRightClickListener(this);
    }

    private int hasBow(Player player) {
        PlayerInventory inventory = player.getInventory();
        if (ItemUtil.hasPersistentData(inventory.getItemInMainHand(), ID_TAG, PersistentDataType.BYTE)) {
            return 1;
        } else if (ItemUtil.hasPersistentData(inventory.getItemInOffHand(), ID_TAG, PersistentDataType.BYTE)) {
            return 2;
        } else {
            return 0;
        }
    }

    @EventHandler
    private void onBlockBreak(BlockBreakEvent event) {
        if (hasBow(event.getPlayer()) == 1) event.setCancelled(true);
    }

    @Override
    public void onRightClick(ClickEvent event) {
        Player player = event.getPlayer();
        if (hasBow(player) == 1 && SpellManagementUtil.canUse(player) && (player.getGameMode() == GameMode.CREATIVE || player.getInventory().contains(Material.ARROW))) {
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
            }.runTaskTimer(Main.getPlugin(), 1, 1);
        }
    }

    @EventHandler
    private void onShoot(EntityShootBowEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (hasBow(player) == 2) {
                event.setCancelled(true);
                player.sendActionBar("§ccan't fire from offhand");
            }
            if (drawing.contains(player)) {
                drawing.remove(player);
                player.sendActionBar("§ccancelled shot");
                event.setCancelled(true);
            } else if (hasDrawn.contains(player)) {
                hasDrawn.remove(player);
                Entity projectile = event.getProjectile();
                projectile.setMetadata(cursedArrow, new FixedMetadataValue(plugin,
                        player.getInventory().getItemInMainHand().getItemMeta().getDisplayName()));
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
    private void onChangeSlot(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        if (drawing.contains(player)) {
            player.sendActionBar("§cCancelled shot");
        }
        drawing.remove(player);
    }

    @EventHandler
    private void leave(PlayerQuitEvent event) {
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
        }.runTaskTimer(Main.getPlugin(), 1, 1);
    }

    @EventHandler
    private void projectileHit(ProjectileHitEvent event) {
        Projectile projectile = event.getEntity();
        if (projectile.getShooter() instanceof Player) {
            if (projectile.hasMetadata(cursedArrow)) {
                Location location = projectile.getLocation();
                location.getWorld().playSound(location, Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, SoundCategory.MASTER, 5F, 1F);
                location.getWorld().playSound(location, Sound.ITEM_TRIDENT_RETURN, SoundCategory.MASTER, 5F, 1F);
                location.getWorld().spawnParticle(Particle.SMOKE_NORMAL, location, 30, 0.4, 0.4, 0.4, 0.2, null, true);
                SpellEffectUtil.getNearbyLivingEntities((Player) projectile.getShooter(), location, 3)
                        .forEach(entity -> {
                            SpellEffectUtil.damageEffect((Player) projectile.getShooter(), entity, 8, projectile.getMetadata(cursedArrow).get(0).asString());
                            entity.addPotionEffect(slow, true);
                        });
                projectile.remove();
            }
        }
    }

    @EventHandler
    private void onDamage(EntityDamageByEntityEvent event) {
        if (event.getCause() == EntityDamageEvent.DamageCause.PROJECTILE) {
            if (event.getDamager().hasMetadata(cursedArrow)) {
                event.setCancelled(true);
            }
        }
    }
}

package me.dylan.wands.customitems;

import me.dylan.wands.MouseClickListeners.ClickEvent;
import me.dylan.wands.MouseClickListeners.RightClickListener;
import me.dylan.wands.spell.accessories.ItemTag;
import me.dylan.wands.spell.util.SpellInteractionUtil;
import me.dylan.wands.utils.Common;
import me.dylan.wands.utils.PlayerUtil;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class AssassinDagger implements Listener, RightClickListener {
    private final String tagLeap = UUID.randomUUID().toString();
    private final String tagSneak = UUID.randomUUID().toString();
    private final String tagSprint = UUID.randomUUID().toString();
    private final PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, 10, 4, true);

    private boolean hasDagger(Player player) {
        return isDagger(player.getInventory().getItemInMainHand()) && SpellInteractionUtil.canUseMagic(player);
    }

    private boolean isDagger(@NotNull ItemStack itemStack) {
        return ItemTag.IS_DAGGER.isTagged(itemStack);
    }

    @EventHandler
    private void onBlockBreak(BlockBreakEvent event) {
        if (hasDagger(event.getPlayer())) event.setCancelled(true);
    }

    @EventHandler
    private void onSpringToggle(PlayerToggleSprintEvent event) {
        Player player = event.getPlayer();
        if (hasDagger(player) && event.isSprinting()) {
            sprintEffect(player);
        }
    }

    private void sprintEffect(Player player) {
        if (!player.hasMetadata(tagSprint)) {
            player.setMetadata(tagSprint, Common.getMetadataValueTrue());
            BukkitRunnable bukkitRunnable = new BukkitRunnable() {
                public void run() {
                    if (player.isSprinting()) {
                        Location loc = player.getLocation();
                        World world = loc.getWorld();
                        world.spawnParticle(Particle.SMOKE_LARGE, loc, 1, 0.1, 0.1, 0.1, 0.1, null, true);
                        world.spawnParticle(Particle.SMOKE_NORMAL, loc, 1, 0.1, 0.1, 0.1, 0.1, null, true);
                        player.addPotionEffect(speed, true);
                    } else {
                        cancel();
                        Common.removeMetaData(player, tagSprint);
                        player.removePotionEffect(PotionEffectType.SPEED);
                    }
                }
            };
            Common.runTaskTimer(bukkitRunnable, 1, 1);
        }
    }

    @EventHandler
    private void onAttack(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            if (event.getEntity() instanceof LivingEntity) {
                LivingEntity victim = (LivingEntity) event.getEntity();
                if (hasDagger(player)) {
                    victim.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 100, 0, false), true);
                    victim.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 100, 5, true), true);
                    event.setDamage(8);
                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GUARDIAN_HURT, SoundCategory.MASTER, 3.0F, 1.0F);
                    player.getWorld().playSound(player.getLocation(), Sound.ENTITY_GUARDIAN_HURT, SoundCategory.MASTER, 3.0F, 0.3F);
                }
            }
        }
    }

    @Override
    public void onRightClick(ClickEvent event) {
        Player player = event.getPlayer();
        if (hasDagger(player)) {
            if (!player.hasMetadata(tagLeap) && !player.hasMetadata(tagSneak)) {
                player.setMetadata(tagLeap, Common.getMetadataValueTrue());
                event.cancel();
                player.getWorld().playSound(player.getLocation(), Sound.ENTITY_LLAMA_SWAG, SoundCategory.MASTER, 3.0F, 1.0F);
                Vector direction = player.getLocation().getDirection().setY(0).normalize().setY(1.2);
                player.setVelocity(direction);
                BukkitRunnable bukkitRunnable = new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (player.isOnGround()) {
                            Common.removeMetaData(player, tagLeap);
                            cancel();
                        }
                    }
                };
                Common.runTaskTimer(bukkitRunnable, 5, 1);
            }
        }
    }

    @EventHandler
    private void fallDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player && event.getCause() == EntityDamageEvent.DamageCause.FALL && entity.hasMetadata(tagLeap)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onSneakToggle(PlayerToggleSneakEvent event) {
        Player player = event.getPlayer();
        if (hasDagger(player) && event.isSneaking() && player.isOnGround()) {
            cover(player);
            return;
        }
        if (player.hasMetadata(tagSneak)) {
            uncover(player, "§6You are §cVisible");
        }
    }

    @EventHandler
    private void onTakingDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            Player player = (Player) entity;
            if (player.hasMetadata(tagSneak)) {
                uncover(player, "§6You have been §cUncovered");
                Location location = player.getLocation();
                location.getWorld().spawnParticle(Particle.VILLAGER_ANGRY, location, 5, 1, 1, 1, 1, null, true);

            }
        }
    }

    private void cover(Player player) {
        if (!player.hasMetadata(tagSneak)) {
            player.setMetadata(tagSneak, Common.getMetadataValueTrue());
            Location location = player.getLocation();
            World world = location.getWorld();
            world.playSound(location, Sound.ENTITY_ZOMBIE_VILLAGER_CURE, 1.0f, 2.00f);
            world.spawnParticle(Particle.SMOKE_LARGE, location, 15, 0.5, 0.2, 0.5, 0.1, null, true);
            world.spawnParticle(Particle.ENCHANTMENT_TABLE, location, 20, 0.5, 0.5, 0.5, 0.1, null, true);
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 6000, 0, true), true);
            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 6000, 0, true), false);
            PlayerUtil.sendActionBar(player, "§6You are §aInvisible");
        }
    }

    private void uncover(Player player, String message) {
        Common.removeMetaData(player, tagSneak);
        player.removePotionEffect(PotionEffectType.INVISIBILITY);
        player.removePotionEffect(PotionEffectType.REGENERATION);
        Location location = player.getLocation();
        location.getWorld().playSound(location, Sound.ITEM_ARMOR_EQUIP_ELYTRA, 1.0f, 0.8f);
        PlayerUtil.sendActionBar(player, message);
    }

    @EventHandler
    private void onChangeSlot(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = player.getInventory().getItem(event.getNewSlot());
        if (itemStack != null && isDagger(itemStack)) {
            if (player.isSneaking()) {
                cover(player);
            } else if (player.isSprinting()) {
                sprintEffect(player);
            }
        }
    }
}

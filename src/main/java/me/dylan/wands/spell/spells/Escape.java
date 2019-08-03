package me.dylan.wands.spell.spells;

import me.dylan.wands.ListenerRegistry;
import me.dylan.wands.sound.RepeatableSound;
import me.dylan.wands.sound.SoundEffect;
import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.SpellEffectUtil;
import me.dylan.wands.spell.types.Base;
import me.dylan.wands.util.Common;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Escape extends Base implements Castable, Listener {

    public static final Castable INSTANCE = new Escape();
    private final String tagEscaping = UUID.randomUUID().toString();
    private final SoundEffect sound = RepeatableSound.from(Sound.ENTITY_ENDER_DRAGON_FLAP, 1, 0, 4, 4, 4, 4, 4);

    private final Set<Player> leaping = new HashSet<>();

    @SuppressWarnings("deprecation")
    private Escape() {
        ListenerRegistry.addListener(this);
        plugin.addDisableLogic(() -> leaping.forEach(e -> e.teleport(SpellEffectUtil.getFirstGroundBlockUnder(e.getLocation()))));
    }

    @Override
    public Base getBaseType() {
        return this;
    }

    @Override
    public boolean cast(@NotNull Player player, @NotNull String weaponName) {
        if (player.hasMetadata(tagEscaping)) {
            return false;
        } else {
            player.setMetadata(tagEscaping, Common.METADATA_VALUE_TRUE);
            leaping.add(player);
            World world = player.getWorld();
            Vector vector = player.getLocation().getDirection().normalize().multiply(3);
            double y = vector.getY();
            vector.setY(y <= 0 ? 1.5 : 1.5 + (y * 0.2));
            player.setVelocity(vector);
            sound.play(player);
            new BukkitRunnable() {
                int count;

                @Override
                public void run() {
                    count++;
                    if (!player.isValid() || player.isOnGround()) {
                        cancel();
                        Bukkit.getScheduler().runTaskLater(plugin, () -> player.removeMetadata(tagEscaping, plugin), 10L);
                        leaping.remove(player);
                        Location loc = player.getLocation();
                        loc.createExplosion(0);
                        world.playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, SoundCategory.MASTER, 4, 1);
                        world.spawnParticle(Particle.EXPLOSION_HUGE, loc, 0, 0.0, 0.0, 0.0, 0.0, null, true);
                        world.spawnParticle(Particle.SPELL_WITCH, loc, 40, 1, 1, 1, 1, null, true);
                        world.spawnParticle(Particle.SMOKE_LARGE, loc, 50, 2, 2, 2, 0.05, null, true);
                        SpellEffectUtil.getNearbyLivingEntities(player, loc, 4).forEach(entity -> {
                            entity.damage(6);
                            entity.setFireTicks(40);
                            entity.setVelocity(entity.getLocation().subtract(player.getLocation()).toVector().setY(0.3).normalize());
                        });
                    } else {
                        Location ploc = player.getLocation();
                        world.spawnParticle(Particle.SPELL_WITCH, ploc, 10, 0.5, 0.5, 0.5, 0.15, null, true);
                        world.spawnParticle(Particle.SMOKE_LARGE, ploc, 4, 0.2, 0.22, 0.2, 0.08, null, true);
                        if (count == 20) {
                            Vector vector = ploc.getDirection().normalize().multiply(3);
                            player.setVelocity(vector);
                        }
                    }
                }
            }.runTaskTimer(plugin, 5, 1);
        }
        return true;
    }

    @EventHandler
    private void fallDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player && event.getCause() == EntityDamageEvent.DamageCause.FALL && entity.hasMetadata(tagEscaping)) {
            event.setCancelled(true);
        }
    }
}

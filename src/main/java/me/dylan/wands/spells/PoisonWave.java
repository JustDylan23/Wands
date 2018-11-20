package me.dylan.wands.spells;

import me.dylan.wands.Spell;
import org.bukkit.*;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import java.util.Collection;

public class PoisonWave extends Spell implements Listener {
    private final Plugin plugin;

    public PoisonWave(Plugin plugin) {
        super("PoisonWave");
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public void cast(Player player) {
        Location sLoc = getLocationInfrontOf(player, 1);
        sLoc.getWorld().playSound(sLoc, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.MASTER, 4.0F, 1.0F);

        Location pLoc = player.getEyeLocation();
        final Vector direction = player.getLocation().getDirection().normalize();
        Location loc;
        Collection<Entity> nearby = sLoc.getWorld().getNearbyEntities(sLoc, 2.2, 2.2, 2.2);
        for (int i = 0; i < 15; i++) {
            loc = direction.clone().multiply(i).toLocation(player.getWorld()).add(pLoc);
            loc.getWorld().spawnParticle(Particle.SPELL_MOB, loc, 15, 1, 1, 1, 0, null, true);

            for (Entity entity : loc.getWorld().getNearbyEntities(loc, 2.2, 2.2, 2.2)) {
                if (!nearby.contains(entity) && entity instanceof Damageable) {
                    nearby.add(entity);
                    damageEntity(player, (Damageable) entity, 1);
                    ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.POISON, 60, 4, false));
                }
            }
        }
    }

}

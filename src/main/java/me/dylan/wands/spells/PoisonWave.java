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

import java.lang.reflect.Array;
import java.util.ArrayList;
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
        player.getWorld().playSound(sLoc, Sound.ENTITY_EVOKER_CAST_SPELL, SoundCategory.MASTER, 4.0F, 1.0F);

        Location pLoc = player.getEyeLocation();
        Vector direction = player.getLocation().getDirection().normalize();

        ArrayList<Location> locations = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            locations.add(direction.clone().multiply(i).toLocation(player.getWorld()).add(pLoc));
        }
        Collection<Entity> nearby = new ArrayList<>();
        nearby.add(player); //makes player immune

        int i = 0;
        for (Location loc : locations) {
            i++;
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                loc.getWorld().spawnParticle(Particle.SPELL_MOB, loc, 15, 1, 1, 1, 0, null, true);
                loc.getWorld().spawnParticle(Particle.SMOKE_NORMAL, loc, 5, 1, 1, 1, 0.05, null, true);
                for (Entity entity : loc.getWorld().getNearbyEntities(loc, 2.2, 2.2, 2.2)) {
                    if (!nearby.contains(entity) && entity instanceof Damageable) {
                        nearby.add(entity);
                        damageEntity(player, (Damageable) entity, 1);
                        ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.POISON, 60, 4, false));
                    }
                }

            }, i);

        }
    }

}

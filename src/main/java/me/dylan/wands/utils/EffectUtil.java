package me.dylan.wands.utils;

import me.dylan.wands.Wands;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class EffectUtil {

    private static final Wands plugin = Wands.getPlugin();

    private EffectUtil() {
        throw new UnsupportedOperationException();
    }

    public static Iterable<Damageable> getNearbyDamageables(Player player, Location loc, double radius) {
        return loc.getWorld()
                .getNearbyEntities(loc, radius, radius, radius).stream()
                .filter(Damageable.class::isInstance)
                .filter(entity -> !entity.equals(player))
                .map(Damageable.class::cast)
                .collect(Collectors.toList());
    }

    public static void damage(int damage, Entity attacker, Damageable victim) {
        victim.damage(damage);
        EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(attacker, victim, EntityDamageEvent.DamageCause.ENTITY_ATTACK, damage);
        victim.setLastDamageCause(event);
    }

    public static void runTaskLater(Runnable runnable, int... delays) {
        int delay = 0;
        for (int d : delays) {
            delay += d;
            Bukkit.getScheduler().runTaskLater(plugin, runnable, delay);
        }
    }

    public static void spawnColoredParticle(Particle particle, Location location, int count, double x, double y, double z, int r, int g, int b, boolean rainbow) {
        switch (particle) {
            case REDSTONE:
            case SPELL_MOB:
            case SPELL_MOB_AMBIENT:
                if (rainbow) {
                    int c = (count > 0) ? count : 1; //if the count is 0 it will color
                    location.getWorld().spawnParticle(particle, location, c, x, y, z, 1, null);
                } else {
                    float red = (r <= 0) ? Float.MIN_NORMAL : (r / 255.0f);
                    float green = (g <= 0) ? 0 : (g / 255.0f);
                    float blue = (b <= 0) ? 0 : (b / 255.0f);
                    for (int i = 0; count > i; i++) {
                        location.getWorld().spawnParticle(particle, randomizeLoc(location, x, y, z), 0, red, green, blue, 1, null);
                    }
                }
        }
    }

    private static Location randomizeLoc(Location location, double x, double y, double z) {
        return location.clone().add(randomize(x), randomize(y), randomize(z));
    }

    private static double randomize(double i) {
        return ThreadLocalRandom.current().nextDouble() * i * 2.0 - i;
    }
}

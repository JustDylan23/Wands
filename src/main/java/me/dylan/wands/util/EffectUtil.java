package me.dylan.wands.util;

import me.dylan.wands.Main;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class EffectUtil {

    private static final Main plugin = Main.getPlugin();
    private static final Vector NO_VELOCITY = new Vector();

    private EffectUtil() {
        throw new UnsupportedOperationException();
    }

    public static Iterable<LivingEntity> getNearbyLivingEntities(Player player, Location loc, double radius) {
        return loc.getWorld()
                .getNearbyEntities(loc, radius, radius, radius).stream()
                .filter(LivingEntity.class::isInstance)
                .filter(entity -> !entity.equals(player))
                .map(LivingEntity.class::cast)
                .collect(Collectors.toList());
    }

    public static void runTaskLater(Runnable runnable, int... delays) {
        int delay = 0;
        for (int i : delays) {
            delay += i;
            Bukkit.getScheduler().runTaskLater(plugin, runnable, delay);
        }
    }

    public static void spawnColoredParticle(Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, int r, int g, int b, boolean rainbow) {
        switch (particle) {
            case REDSTONE:
            case SPELL_MOB:
            case SPELL_MOB_AMBIENT:
                if (rainbow) {
                    location.getWorld().spawnParticle(particle, location, (count > 0) ? count : 1, offsetX, offsetY, offsetZ, 1, null, true);
                } else {
                    float red = Math.max(Float.MIN_NORMAL, r / 255F);
                    float green = Math.max(0, g / 255F);
                    float blue = Math.max(0, b / 255F);
                    for (int i = 0; count > i; i++) {
                        location.getWorld().spawnParticle(particle, randomizeLoc(location, offsetX, offsetY, offsetZ), 0, red, green, blue, 1, null, true);
                    }
                }
        }
    }

    public static Location randomizeLoc(Location location, double x, double y, double z) {
        return location.clone().add(randomize(x), randomize(y), randomize(z));
    }

    private static double randomize(double d) {
        return ThreadLocalRandom.current().nextDouble() * d * 2.0 - d;
    }

    public static void removeVelocity(Entity entity) {
        entity.setVelocity(NO_VELOCITY);
    }
}

package me.dylan.wands.spell;

import com.destroystokyo.paper.block.TargetBlockInfo;
import me.dylan.wands.Main;
import me.dylan.wands.util.Common;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.io.Console;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class SpellEffectUtil {
    private static final Main plugin = Main.getPlugin();

    private SpellEffectUtil() {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("ConstantConditions")
    public static Location getSpellLocation(int effectDistance, Player player) {
        if (effectDistance == 0) return player.getLocation();
        Entity entity = player.getTargetEntity(effectDistance);
        if (entity != null) {
            return entity.getLocation().add(0, 0.5, 0).toCenterLocation();
        }
        TargetBlockInfo info = player.getTargetBlockInfo(effectDistance);
        if (info.getBlock().getType() == Material.AIR) {
            return info.getBlock().getLocation().toCenterLocation();
        }
        return info.getRelativeBlock().getLocation().toCenterLocation();
    }

    public static Location[] getCircleFrom(Location location, float radius) {
        int density = (int) StrictMath.ceil(radius * 2 * Math.PI);
        double increment = (2 * Math.PI) / density;
        Location[] locations = new Location[density];
        double angle = 0;
        World world = location.getWorld();
        double originX = location.getX();
        double originY = location.getY();
        double originZ = location.getZ();
        for (int i = 0; i < density; i++) {
            angle += increment;
            double newX = originX + (radius * Math.cos(angle));
            double newZ = originZ + (radius * Math.sin(angle));
            locations[i] = new Location(world, newX, originY, newZ);
        }
        return locations;
    }

    public static List<LivingEntity> getNearbyLivingEntities(Player player, Location loc, double radius) {
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

}

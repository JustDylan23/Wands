package me.dylan.wands;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;

import java.util.concurrent.ThreadLocalRandom;

public class ParticleLib {

    private static double randomize(double x) {
        return ThreadLocalRandom.current().nextDouble() * x * 2 - x;
    }

    private static Location randomizeLoc(Location location, double x, double y, double z) {
        return location.clone().add(randomize(x), randomize(y), randomize(z));
    }

    public static void spawnParticle(Particle particle, Location location, int count, double speed, double x, double y, double z, int r, int g, int b, boolean rainbow) {
        switch (particle) {
            case REDSTONE:
            case SPELL_MOB:
            case SPELL_MOB_AMBIENT:
                if (rainbow) {
                    int c = (count > 0) ? count : 1;
                    location.getWorld().spawnParticle(particle, location, c, x, y, z, 1, null);
                } else {
                    for (int i = 0; count > i; i++) {
                        location.getWorld().spawnParticle(particle, randomizeLoc(location, x, y, z), 0, r / 255F, g / 255F, b / 255F, 1, null);
                    }
                }
                break;
            default:
                spawnParticle(particle, location, count, speed, x, y, z);
        }
    }

    public static void spawnParticle(Particle particle, Location location, int count, double speed, double x, double y, double z, Material material) {
        switch (particle) {
            case ITEM_CRACK:
            case BLOCK_DUST:
            case FALLING_DUST:
                location.getWorld().spawnParticle(particle, location, count, x, y, z, speed, material.createBlockData());
                break;
            default:
                spawnParticle(particle, location, count, speed, x, y, z);
        }
    }

    public static void spawnParticle(Particle particle, Location location, int count, double speed, double x, double y, double z) {
        location.getWorld().spawnParticle(particle, location, count, x, y, z, speed, null);
    }
}

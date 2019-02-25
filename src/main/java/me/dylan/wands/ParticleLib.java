package me.dylan.wands;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;

import java.util.Random;

public class ParticleLib {

    private static final Random r = new Random();
    private static double rn(double x) {
        x *= 100;
        return (r.nextInt((int) x * 2) - x) / 100D;
    }

    private static Location rl(Location location, double x, double y, double z) {
        return location.clone().add(rn(x), rn(y), rn(z));
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
                        location.getWorld().spawnParticle(particle, rl(location, x, y, z), 0, r / 255F, g / 255F, b / 255F, 1, null);
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

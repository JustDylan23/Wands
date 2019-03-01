package me.dylan.wands;

import org.bukkit.Location;
import org.bukkit.Particle;

import java.util.concurrent.ThreadLocalRandom;

public final class ParticleUtil {

    public static void spawnColoredParticle(Particle particle, Location location, int count, double speed, double x, double y, double z, int r, int g, int b, boolean rainbow) {
        switch (particle) {
            case REDSTONE:
            case SPELL_MOB:
            case SPELL_MOB_AMBIENT:
                if (rainbow) {
                    int c = (count > 0) ? count : 1; //if the count is 0 it will color
                    location.getWorld().spawnParticle(particle, location, c, x, y, z, 1, null);
                } else {
                    for (int i = 0; count > i; i++) {
                        location.getWorld().spawnParticle(particle, randomizeLoc(location, x, y, z), 0, r / 255F, g / 255F, b / 255F, 1, null);
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

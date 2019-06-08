package me.dylan.wands.spell;

import org.bukkit.Location;
import org.bukkit.Particle;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("WeakerAccess")
public class ParticleCollection {
    private final List<ParticleHolder> list = new ArrayList<>();

    public <T> ParticleCollection addParticle(Particle particle, int count, double offsetX, double offsetY, double offsetZ, double extra, T data) {
        list.add(new ParticleHolder(particle, count, offsetX, offsetY, offsetZ, extra, data));
        return this;
    }

    public void spawnParticlesAt(Location location) {
        list.forEach(p -> location.getWorld().spawnParticle(p.particle, location, p.count, p.x, p.y, p.z, p.extra, p.data));
    }

    private static class ParticleHolder {
        final Particle particle;
        final int count;
        final double x, y, z, extra;
        final Object data;

        <T> ParticleHolder(Particle particle, int count, double x, double y, double z, double extra, T data) {
            this.particle = particle;
            this.count = count;
            this.x = x;
            this.y = y;
            this.z = z;
            this.extra = extra;
            this.data = data;
        }
    }
}

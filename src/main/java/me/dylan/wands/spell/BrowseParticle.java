package me.dylan.wands.spell;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;

import java.util.function.Consumer;

public enum BrowseParticle {
    DEFAULT(loc -> {
        loc.getWorld().spawnParticle(Particle.SPELL_WITCH, loc, 10, 0.5, 0.5, 0.5, 1);
    }),
    PARTICLE_BLOOD(loc -> {
        loc.getWorld().spawnParticle(Particle.BLOCK_CRACK, loc, 10, 0.5, 0.5, 0.5, 1, Material.REDSTONE_BLOCK.createBlockData());
    }),
    PARTICLE_ICE(loc -> {
        loc.getWorld().spawnParticle(Particle.SNOW_SHOVEL, loc, 10, 0.5, 0.5, 0.5, 1);
    }),
    PARTICLE_DARK(loc -> {
        loc.getWorld().spawnParticle(Particle.SMOKE_NORMAL, loc, 10, 0.5, 0.5, 0.5, 0.03);
    }),
    PARTICLE_FIRE(loc -> {
        loc.getWorld().spawnParticle(Particle.FLAME, loc, 8, 0.5, 0.5, 0.5, 0.03);
        loc.getWorld().spawnParticle(Particle.SMOKE_NORMAL, loc, 10, 0.5, 0.5, 0.5, 0.03);
    });

    private final Consumer<Location> consumer;

    BrowseParticle(Consumer<Location> consumer) {
        this.consumer = consumer;
    }

    void spawn(Location location) {
        location.add(0, 1, 0);
        consumer.accept(location);
        location.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, location, 15, 0.5, 0.5, 0.5, 1);
    }
}

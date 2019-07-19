package me.dylan.wands.spell;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;

import java.util.function.BiConsumer;

public enum BrowseParticle {
    DEFAULT((loc, world) -> {
        world.spawnParticle(Particle.SPELL_WITCH, loc, 10, 0.5, 0.5, 0.5, 1, null, true);
        world.spawnParticle(Particle.ENCHANTMENT_TABLE, loc, 15, 0.5, 0.5, 0.5, 1, null, true);
    }),
    PARTICLE_BLOOD((loc, world) -> {
        world.spawnParticle(Particle.BLOCK_CRACK, loc, 10, 0.5, 0.5, 0.5, 1, Material.REDSTONE_BLOCK.createBlockData(), true);
        world.spawnParticle(Particle.ENCHANTMENT_TABLE, loc, 15, 0.5, 0.5, 0.5, 1, null, true);
    }),
    PARTICLE_ICE((loc, world) -> {
        world.spawnParticle(Particle.SNOW_SHOVEL, loc, 10, 0.5, 0.5, 0.5, 1, null, true);
        world.spawnParticle(Particle.ENCHANTMENT_TABLE, loc, 15, 0.5, 0.5, 0.5, 1, null, true);
    }),
    PARTICLE_DARK((loc, world) -> {
        world.spawnParticle(Particle.SMOKE_NORMAL, loc, 10, 0.5, 0.5, 0.5, 0.03, null, true);
        world.spawnParticle(Particle.ENCHANTMENT_TABLE, loc, 15, 0.5, 0.5, 0.5, 1, null, true);
    }),
    PARTICLE_FIRE((loc, world) -> {
        world.spawnParticle(Particle.FLAME, loc, 8, 0.5, 0.5, 0.5, 0.03, null, true);
        world.spawnParticle(Particle.SMOKE_NORMAL, loc, 10, 0.5, 0.5, 0.5, 0.03, null, true);
        world.spawnParticle(Particle.ENCHANTMENT_TABLE, loc, 15, 0.5, 0.5, 0.5, 1);
    }),
    PARTICLE_CORRUPTED((loc, world) -> {
        world.spawnParticle(Particle.SPELL_MOB, loc, 4, 0.5, 0.5, 0.5, 1, null, false);
        world.spawnParticle(Particle.SPELL_MOB_AMBIENT, loc, 10, 0.5, 0.5, 0.5, 1, null, false);
    });

    private final BiConsumer<Location, World> consumer;

    BrowseParticle(BiConsumer<Location, World> consumer) {
        this.consumer = consumer;
    }

    void spawn(Location location) {
        location.add(0, 1, 0);
        consumer.accept(location, location.getWorld());
    }
}

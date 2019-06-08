package me.dylan.wands.spell;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;

public enum BrowseParticle {
    DEFAULT(new ParticleCollection()
            .addParticle(Particle.SPELL_WITCH, 10, 0.5, 0.5, 0.5, 1, null)
            .addParticle(Particle.ENCHANTMENT_TABLE, 10, 0.5, 0.5, 0.5, 1, null)
    ),
    PARTICLE_BLOOD(new ParticleCollection()
            .addParticle(Particle.BLOCK_CRACK, 10, 0.5, 0.5, 0.5, 1, Material.REDSTONE_BLOCK.createBlockData())
            .addParticle(Particle.ENCHANTMENT_TABLE, 10, 0.5, 0.5, 0.5, 1, null)
    );

    private final ParticleCollection particleCollection;

    BrowseParticle(ParticleCollection particleCollection) {
        this.particleCollection = particleCollection;
    }

    void spawn(Location location) {
        particleCollection.spawnParticlesAt(location);
    }
}

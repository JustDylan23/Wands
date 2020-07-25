package me.dylan.wands.spell;

import me.dylan.wands.spell.spells.blood.BloodMagicConstants;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.jetbrains.annotations.NotNull;

import java.util.function.BiConsumer;

@SuppressWarnings("unused")
public enum BrowseParticle {
    DEFAULT((loc, world) -> {
        world.spawnParticle(Particle.ENCHANTMENT_TABLE, loc, 15, 0.5, 0.5, 0.5, 1, null, true);
    }),
    MORTAL_BLADE((loc, world) -> {
        world.spawnParticle(Particle.BLOCK_CRACK, loc, 10, 0.5, 0.5, 0.5, 1, BloodMagicConstants.BLOCK_CRACK_REDSTONE, true);
        world.spawnParticle(Particle.SMOKE_NORMAL, loc, 10, 0.5, 0.5, 0.5, 0.03, null, true);
    }),
    PARTICLE_BLOOD((loc, world) -> {
        world.spawnParticle(Particle.BLOCK_CRACK, loc, 10, 0.5, 0.5, 0.5, 1, BloodMagicConstants.BLOCK_CRACK_REDSTONE, true);
        world.spawnParticle(Particle.ENCHANTMENT_TABLE, loc, 15, 0.5, 0.5, 0.5, 1, null, true);
    }),
    PARTICLE_CORRUPTED((loc, world) -> {
        world.spawnParticle(Particle.SPELL_MOB, loc, 4, 0.5, 0.5, 0.5, 1, null, false);
        world.spawnParticle(Particle.SPELL_MOB_AMBIENT, loc, 10, 0.5, 0.5, 0.5, 1, null, false);
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
    PARTICLE_ICE((loc, world) -> {
        world.spawnParticle(Particle.SNOW_SHOVEL, loc, 10, 0.5, 0.5, 0.5, 1, null, true);
        world.spawnParticle(Particle.ENCHANTMENT_TABLE, loc, 15, 0.5, 0.5, 0.5, 1, null, true);
    }),
    PARTICLE_MEPHI((loc, world) -> {
        world.spawnParticle(Particle.SMOKE_NORMAL, loc, 10, 0.5, 0.5, 0.5, 0.03, null, true);
        world.spawnParticle(Particle.VILLAGER_HAPPY, loc, 4, 0.5, 0.5, 0.5, 1, null, true);
    }),
    WITCH((loc, world) -> {
        world.spawnParticle(Particle.SPELL_WITCH, loc, 10, 0.5, 0.5, 0.5, 1, null, true);
        world.spawnParticle(Particle.ENCHANTMENT_TABLE, loc, 15, 0.5, 0.5, 0.5, 1, null, true);
    });

    private final BiConsumer<Location, World> consumer;

    BrowseParticle(BiConsumer<Location, World> consumer) {
        this.consumer = consumer;
    }

    public void displayAt(@NotNull Location location) {
        location.add(0, 1, 0);
        consumer.accept(location, location.getWorld());
    }
}

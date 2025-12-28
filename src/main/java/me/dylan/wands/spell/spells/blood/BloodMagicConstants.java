package me.dylan.wands.spell.spells.blood;

import me.dylan.wands.spell.accessories.SpellInfo;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;

import java.util.function.BiConsumer;

public class BloodMagicConstants {
    public static final BlockData BLOCK_CRACK_REDSTONE = Material.REDSTONE_BLOCK.createBlockData();

    static final BiConsumer<Location, SpellInfo> BLOOD_EXPLOSION_EFFECTS = (loc, spellInfo) -> {
        World world = spellInfo.world();
        world.spawnParticle(Particle.LARGE_SMOKE, loc, 20, 1, 1, 1, 0.1, null, true);
        world.spawnParticle(Particle.BLOCK, loc, 15, 1, 1, 1, 0.15, BLOCK_CRACK_REDSTONE, true);
        world.spawnParticle(Particle.EXPLOSION_EMITTER, loc, 0, 0.0, 0.0, 0.0, 0.0, null, true);
        world.createExplosion(loc, 0.0f);
    };
}

package me.dylan.wands.spell.spells.fire;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.spellbuilders.Behavior;
import me.dylan.wands.spell.spellbuilders.BlockProjectile;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;

public class FlameThrower implements Castable {
    @Override
    public Behavior createBehaviour() {
        return BlockProjectile.newBuilder(Material.FIRE, 2.0F)
                .setCastSound(Sound.ENTITY_BLAZE_SHOOT)
                .setProjectileAmount(8)
                .setProjectileShootDelay(3)
                .setEntityEffects((entity, spellInfo) -> entity.setFireTicks(120))
                .setEntityDamage(6)
                .setSpellEffectRadius(3.0F)
                .setSpellRelativeEffects((loc, spellInfo) -> {
                    World world = spellInfo.world();
                    world.spawnParticle(Particle.FLAME, loc, 2, 0.8, 0.8, 0.8, 0.1, null, true);
                    world.spawnParticle(Particle.SMOKE_LARGE, loc, 1, 0.3, 0.3, 0.3, 0.1, null, true);
                })
                .build();
    }
}
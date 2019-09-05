package me.dylan.wands.spell.spells;

import me.dylan.wands.spell.SpellData;
import me.dylan.wands.spell.tools.KnockBack;
import me.dylan.wands.spell.types.Behavior;
import me.dylan.wands.spell.types.Spark;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;

public class BloodExplode implements SpellData {
    private final Behavior behavior;

    public BloodExplode() {
        this.behavior = Spark.newBuilder(Behavior.Target.MULTI)
                .setEntityDamage(10)
                .setKnockBack(KnockBack.EXPLOSION)
                .setSpellEffectRadius(3.0F)
                .setEntityEffects(entity -> entity.setFireTicks(80))
                .setSpellRelativeEffects((loc, world) -> {
                    world.spawnParticle(Particle.SMOKE_LARGE, loc, 20, 1, 1, 1, 0.1, null, true);
                    world.spawnParticle(Particle.BLOCK_CRACK, loc, 15, 1, 1, 1, 0.15, Material.REDSTONE_BLOCK.createBlockData(), true);
                    world.spawnParticle(Particle.EXPLOSION_HUGE, loc, 0, 0.0, 0.0, 0.0, 0.0, null, true);
                    loc.createExplosion(0.0f);
                })
                .setCastSound(Sound.ENTITY_FIREWORK_ROCKET_BLAST)
                .setEffectDistance(30)
                .build();
    }

    @Override
    public Behavior getBehavior() {
        return behavior;
    }
}

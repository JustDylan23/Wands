package me.dylan.wands.spell.spells;

import me.dylan.wands.knockback.KnockBack;
import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.types.Base;
import me.dylan.wands.spell.types.Spark;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;

public enum BloodExplode implements Castable {
    INSTANCE;
    private final Base baseType;

    BloodExplode() {
        this.baseType = Spark.newBuilder(Base.Target.MULTI)
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
    public Base getBaseType() {
        return baseType;
    }
}

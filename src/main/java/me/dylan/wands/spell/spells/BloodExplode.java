package me.dylan.wands.spell.spells;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.tools.KnockBack;
import me.dylan.wands.spell.tools.SpellInfo;
import me.dylan.wands.spell.types.Behavior;
import me.dylan.wands.spell.types.Spark;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;

import java.util.function.BiConsumer;

public class BloodExplode implements Castable {
    static final BiConsumer<Location, SpellInfo> BLOOD_EXPLOSION_EFFECTS = (loc, spellInfo) -> {
        World world = spellInfo.world();
        world.spawnParticle(Particle.SMOKE_LARGE, loc, 20, 1, 1, 1, 0.1, null, true);
        world.spawnParticle(Particle.BLOCK_CRACK, loc, 15, 1, 1, 1, 0.15, BloodSpark.BLOCK_CRACK_REDSTONE, true);
        world.spawnParticle(Particle.EXPLOSION_HUGE, loc, 0, 0.0, 0.0, 0.0, 0.0, null, true);
        world.createExplosion(loc, 0.0f);
    };

    @Override
    public Behavior createBehaviour() {
        return Spark.newBuilder(Behavior.Target.MULTI)
                .setEntityDamage(10)
                .setKnockBack(KnockBack.EXPLOSION)
                .setSpellEffectRadius(3.0F)
                .setEntityEffects((entity, spellInfo) -> entity.setFireTicks(80))
                .setSpellRelativeEffects(BLOOD_EXPLOSION_EFFECTS)
                .setCastSound(Sound.ENTITY_FIREWORK_ROCKET_BLAST)
                .setEffectDistance(30)
                .build();
    }
}

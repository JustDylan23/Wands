package me.dylan.wands.spell.spells;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.accessories.KnockBack;
import me.dylan.wands.spell.types.Behavior;
import me.dylan.wands.spell.types.Circle;
import me.dylan.wands.spell.types.Circle.CirclePlacement;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;

public class FireTwister implements Castable {
    @Override
    public Behavior createBehaviour() {
        return Circle.newBuilder(CirclePlacement.TARGET)
                .setCircleRadius(3)
                .setEntityDamage(7)
                .setSpellEffectRadius(3.0F)
                .setEffectDistance(30)
                .setEntityEffects((entity, spellInfo) -> {
                    entity.getWorld().createExplosion(entity.getLocation(), 0.0f);
                    entity.setFireTicks(60);
                })
                .setMetersPerTick(2)
                .setCastSound(Sound.ENTITY_BLAZE_SHOOT)
                .setSpellRelativeEffects((loc, spellInfo) -> {
                    World world = spellInfo.world();
                    world.spawnParticle(Particle.SMOKE_LARGE, loc, 5, 0.3, 0.3, 0.3, 0.1, null, true);
                    world.spawnParticle(Particle.FLAME, loc, 2, 0.3, 0.3, 0.3, 0.1, null, true);
                    world.spawnParticle(Particle.LAVA, loc, 2, 0.3, 0.3, 0.3, 0, null, true);
                })
                .setKnockBack(KnockBack.SIDEWAYS)

                .setCircleHeight(1)
                .build();
    }
}
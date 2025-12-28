package me.dylan.wands.spell.spells.gravity;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.spellbuilders.Behavior;
import me.dylan.wands.spell.spellbuilders.Circle;
import me.dylan.wands.spell.spellbuilders.Circle.CirclePlacement;
import me.dylan.wands.spell.spells.AffinityType;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;

public class MephiAway implements Castable {
    @Override
    public AffinityType[] getAffinityTypes() {
        return new AffinityType[]{AffinityType.GRAVITY_MAGIC};
    }

    @Override
    public Behavior createBehaviour() {
        return Circle.newBuilder(CirclePlacement.RELATIVE)
                .setCircleRadius(5)
                .setEntityDamage(8)
                .setSpellEffectRadius(5.0F)
                .setEntityEffects((entity, spellInfo) -> entity.getWorld().createExplosion(entity.getLocation(), 0.0f))
                .setMetersPerTick(3)
                .setCastSound(Sound.ENTITY_ENDER_DRAGON_FLAP)
                .setSpellRelativeEffects((loc, spellInfo) -> {
                    World world = spellInfo.world();
                    world.spawnParticle(Particle.SMOKE, loc, 5, 0.3, 0.3, 0.3, 0.1, null, true);
                    world.spawnParticle(Particle.LARGE_SMOKE, loc, 3, 0.3, 0.3, 0.3, 0.1, null, true);
                    world.spawnParticle(Particle.HAPPY_VILLAGER, loc, 2, 0.3, 0.3, 0.3, 0.1, null, true);
                })
                .setKnockBack(1.3F, 0.5F)
                .setCircleHeight(1)
                .build();
    }
}

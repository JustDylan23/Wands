package me.dylan.wands.spell.spells.weather;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.spellbuilders.Behavior;
import me.dylan.wands.spell.spellbuilders.Circle;
import me.dylan.wands.spell.spellbuilders.Circle.CirclePlacement;
import me.dylan.wands.spell.spells.AffinityType;
import me.dylan.wands.spell.util.SpellEffectUtil;
import me.dylan.wands.utils.Common;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;

public class ThunderStorm implements Castable {
    @Override
    public AffinityType[] getAffinityTypes() {
        return new AffinityType[]{AffinityType.WEATHER_MAGIC};
    }

    @Override
    public Behavior createBehaviour() {
        return Circle.newBuilder(CirclePlacement.RELATIVE)
                .setCircleRadius(10)
                .setSpellEffectRadius(10.0F)
                .setCircleHeight(7)
                .setMetersPerTick(2)
                .setCastSound(Sound.ENTITY_WITHER_AMBIENT)
                .setSpellRelativeEffects((loc, spellInfo) -> spellInfo.world()
                        .spawnParticle(Particle.CLOUD, loc, 10, 0.2, 0.2, 0.2, 0.1, null, true))
                .setEntityDamage(10)
                .setEntityEffects((entity, spellInfo) -> {
                    Location loc = entity.getLocation();
                    World world = loc.getWorld();
                    world.spawnParticle(Particle.CLOUD, loc, 40, 2, 2, 2, 0.2, null, true);
                    world.spawnParticle(Particle.SMOKE_NORMAL, loc, 20, 2, 2, 2, 0.2, null, true);
                    world.spawnParticle(Particle.SMOKE_LARGE, loc, 5, 2, 2, 2, 0.2, null, true);
                    world.spawnParticle(Particle.EXPLOSION_HUGE, loc, 0, 0.0, 0.0, 0.0, 0.0, null, true);
                    Common.runRepeatableTaskLater(() -> {
                        Location lightningLocation = SpellEffectUtil.randomizeLoc(loc, 3, 1, 3);
                        world.spigot().strikeLightningEffect(lightningLocation, true);
                        world.playSound(lightningLocation, Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 4, 1);
                        world.playSound(lightningLocation, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 5, 1);
                    }, 1, 2, 3);
                    entity.setFireTicks(60);
                })
                .build();
    }
}
package me.dylan.wands.spell.spells;

import me.dylan.wands.spell.SpellData;
import me.dylan.wands.spell.types.Behavior;
import me.dylan.wands.spell.types.Circle;
import me.dylan.wands.spell.types.Circle.CirclePlacement;
import me.dylan.wands.spell.util.SpellEffectUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;

public class ThunderStorm implements SpellData {
    private final Behavior behavior;

    public ThunderStorm() {
        this.behavior = Circle.newBuilder(CirclePlacement.RELATIVE)
                .setCircleRadius(10)
                .setSpellEffectRadius(10.0F)
                .setCircleHeight(7)
                .setMetersPerTick(2)
                .setCastSound(Sound.ENTITY_WITHER_AMBIENT)
                .setSpellRelativeEffects((loc, world) -> world.spawnParticle(Particle.CLOUD, loc, 10, 0.2, 0.2, 0.2, 0.1, null, true))
                .setEntityDamage(10)
                .setEntityEffects(entity -> {
                    Location loc = entity.getLocation();
                    World world = loc.getWorld();
                    world.spawnParticle(Particle.CLOUD, loc, 40, 2, 2, 2, 0.2, null, true);
                    world.spawnParticle(Particle.SMOKE_NORMAL, loc, 20, 2, 2, 2, 0.2, null, true);
                    world.spawnParticle(Particle.SMOKE_LARGE, loc, 5, 2, 2, 2, 0.2, null, true);
                    world.spawnParticle(Particle.EXPLOSION_HUGE, loc, 0, 0.0, 0.0, 0.0, 0.0, null, true);
                    SpellEffectUtil.runTaskLater(() -> {
                        Location lightningLocation = SpellEffectUtil.randomizeLoc(loc, 3, 1, 3);
                        world.playSound(lightningLocation, Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 4, 1);
                        lightningLocation.getWorld().strikeLightningEffect(lightningLocation);
                    }, 1, 2, 3);
                    entity.setFireTicks(60);
                })
                .build();
    }

    @Override
    public Behavior getBehavior() {
        return behavior;
    }
}
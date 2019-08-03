package me.dylan.wands.spell.spells;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.SpellEffectUtil;
import me.dylan.wands.spell.types.Circle;
import me.dylan.wands.spell.types.Circle.CirclePlacement;
import me.dylan.wands.spell.types.Base;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;

public enum ThunderStorm implements Castable {
    INSTANCE;
    private final Base baseType;

    ThunderStorm() {
        this.baseType = Circle.newBuilder(CirclePlacement.RELATIVE)
                .setCircleRadius(10)
                .setSpellEffectRadius(10.0F)
                .setCircleHeight(7)
                .setMetersPerTick(2)
                .setCastSound(Sound.ENTITY_WITHER_AMBIENT)
                .setSpellRelativeEffects((loc, world) -> world.spawnParticle(Particle.CLOUD, loc, 10, 0.2, 0.2, 0.2, 0.1, null, true))
                .setEntityDamage(10)
                .setEntityEffects(entity -> {
                    Location location = entity.getLocation();
                    World w = location.getWorld();
                    w.spawnParticle(Particle.CLOUD, location, 40, 2, 2, 2, 0.2, null, true);
                    w.spawnParticle(Particle.SMOKE_NORMAL, location, 20, 2, 2, 2, 0.2, null, true);
                    w.spawnParticle(Particle.SMOKE_LARGE, location, 5, 2, 2, 2, 0.2, null, true);
                    w.spawnParticle(Particle.EXPLOSION_HUGE, location, 0, 0.0, 0.0, 0.0, 0.0, null, true);
                    SpellEffectUtil.runTaskLater(() -> {
                        Location loc = SpellEffectUtil.randomizeLoc(location, 3, 1, 3);
                        w.playSound(loc, Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 4, 1);
                        loc.getWorld().strikeLightningEffect(loc);
                    }, 1, 2, 3);
                    entity.setFireTicks(60);
                })
                .build();
    }

    @Override
    public Base getBaseType() {
        return baseType;
    }
}
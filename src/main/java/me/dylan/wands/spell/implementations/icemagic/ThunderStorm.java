package me.dylan.wands.spell.implementations.icemagic;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.handler.Behaviour;
import me.dylan.wands.spell.handler.CircleSpell;
import me.dylan.wands.util.EffectUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;

public enum ThunderStorm implements Castable {
    INSTANCE;
    private final Behaviour behaviour;

    ThunderStorm() {
        this.behaviour = CircleSpell.newBuilder(CircleSpell.CircleType.RELATIVE)
                .setCircleRadius(10)
                .setEffectRadius(10)
                .setCircleHeight(7)
                .setTickSkip(2)
                .setCastEffects(loc -> loc.getWorld().playSound(loc, Sound.ENTITY_WITHER_AMBIENT, 4, 1))
                .setRelativeEffects(loc -> loc.getWorld().spawnParticle(Particle.CLOUD, loc, 10, 0.2, 0.2, 0.2, 0.1, null, true))
                .setEntityDamage(6)
                .setEntityEffects(entity -> {
                    Location location = entity.getLocation();
                    World w = location.getWorld();
                    w.spawnParticle(Particle.CLOUD, location, 40, 2, 2, 2, 0.2, null, true);
                    w.spawnParticle(Particle.SMOKE_NORMAL, location, 20, 2, 2, 2, 0.2, null, true);
                    w.spawnParticle(Particle.SMOKE_LARGE, location, 5, 2, 2, 2, 0.2, null, true);
                    w.spawnParticle(Particle.EXPLOSION_HUGE, location, 0, 0.0, 0.0, 0.0, 0.0, null, true);
                    EffectUtil.runTaskLater(() -> {
                        Location loc = EffectUtil.randomizeLoc(location, 3, 1, 3);
                        w.playSound(loc, Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 4, 1);
                        loc.getWorld().strikeLightningEffect(loc);
                    }, 1, 2, 3);
                    entity.setFireTicks(60);
                })
                .build();
    }

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }
}
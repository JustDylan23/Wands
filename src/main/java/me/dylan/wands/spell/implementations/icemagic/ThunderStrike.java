package me.dylan.wands.spell.implementations.icemagic;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.handler.Behaviour;
import me.dylan.wands.spell.handler.SparkSpell;
import me.dylan.wands.util.EffectUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;

public class ThunderStrike implements Castable {

    private static Behaviour behaviour;

    static {
        behaviour = SparkSpell.newBuilder()
                .setEffectDistance(30)
                .setEntityDamage(6)
                .setCastEffects(location -> location.getWorld().playSound(location, Sound.ENTITY_WITHER_SHOOT, 4, 1))
                .setEntityEffects(entity -> {
                    entity.setFireTicks(80);
                })
                .setRelativeEffects(location -> {
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
                })
                .setEffectRadius(5)
                .build();
    }

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }
}
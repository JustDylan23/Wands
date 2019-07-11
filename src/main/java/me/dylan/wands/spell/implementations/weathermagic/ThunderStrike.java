package me.dylan.wands.spell.implementations.weathermagic;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.SpellEffectUtil;
import me.dylan.wands.spell.handler.Behaviour;
import me.dylan.wands.spell.handler.SparkSpell;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;

public enum ThunderStrike implements Castable {
    INSTANCE;
    private final Behaviour behaviour;

    ThunderStrike() {
        this.behaviour = SparkSpell.newBuilder()
                .setEffectDistance(30)
                .setEntityDamage(6)
                .setImpactSpeed(0.8F)
                .setCastSound(Sound.ENTITY_WITHER_SHOOT)
                .setEntityEffects(entity -> entity.setFireTicks(80))
                .setSpellRelativeEffects(location -> {
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
                })
                .setSpellEffectRadius(4F)
                .build();
    }

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }
}
package me.dylan.wands.spell.castable.icemagic;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.handler.Behaviour;
import me.dylan.wands.spell.handler.ProjectileSpell;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Arrow;

public class LightningArrow implements Castable {

    private static Behaviour behaviour;

    static {
        behaviour = ProjectileSpell.newBuilder(Arrow.class, 2.2F)
                .setHitEffects(location -> {
                    World w = location.getWorld();
                    w.strikeLightningEffect(location);
                    w.playSound(location, Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 4, 1);
                    w.spawnParticle(Particle.CLOUD, location, 40, 0.2, 0.2, 0.2, 0.3, null, true);
                })
                .setEffectRadius(5)
                .setLifeTime(25)
                .setRelativeEffects(location -> {
                    World w = location.getWorld();
                    w.spawnParticle(Particle.CLOUD, location, 10, 0.2, 0.2, 0.2, 0.1, null, true);
                    w.spawnParticle(Particle.ENCHANTMENT_TABLE, location, 20, 0.5, 0.5, 0.5, 1, null, true);
                })
                .setEntityDamage(6)
                .setEntityEffects(entity -> {
                    entity.setFireTicks(80);
                })
                .setCastEffects(location -> location.getWorld().playSound(location, Sound.ENTITY_WITHER_SHOOT, 4, 1))
                .build();
    }

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }

    @Override
    public String getDisplayName() {
        return "Lightning Arrow";
    }
}
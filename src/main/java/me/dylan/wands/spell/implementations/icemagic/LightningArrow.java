package me.dylan.wands.spell.implementations.icemagic;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.handler.Behaviour;
import me.dylan.wands.spell.handler.ProjectileSpell;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Arrow;

public enum LightningArrow implements Castable {
    INSTANCE;
    private final Behaviour behaviour;

    LightningArrow() {
        this.behaviour = ProjectileSpell.newBuilder(Arrow.class, 2.2F)
                .setHitEffects(location -> {
                    World w = location.getWorld();
                    w.strikeLightningEffect(location);
                    w.playSound(location, Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 4, 1);
                    w.spawnParticle(Particle.CLOUD, location, 40, 0.2, 0.2, 0.2, 0.3, null, true);
                })
                .setSpellEffectRadius(3.5F)
                .setLifeTime(25)
                .setSpellRelativeEffects(location -> {
                    World w = location.getWorld();
                    w.spawnParticle(Particle.CLOUD, location, 10, 0.2, 0.2, 0.2, 0.1, null, true);
                    w.spawnParticle(Particle.ENCHANTMENT_TABLE, location, 20, 0.5, 0.5, 0.5, 1, null, true);
                })
                .setAffectedEntityDamage(6)
                .setAffectedEntityEffects(entity -> entity.setFireTicks(80))
                .setCastSound(Sound.ENTITY_WITHER_SHOOT)
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
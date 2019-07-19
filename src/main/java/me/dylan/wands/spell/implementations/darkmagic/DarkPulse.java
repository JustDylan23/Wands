package me.dylan.wands.spell.implementations.darkmagic;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.handler.Behaviour;
import me.dylan.wands.spell.handler.RaySpell;
import me.dylan.wands.spell.handler.RaySpell.Target;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public enum DarkPulse implements Castable {
    INSTANCE;
    private final Behaviour behaviour;

    private final PotionEffect blind = new PotionEffect(PotionEffectType.BLINDNESS, 60, 0, false);
    private final PotionEffect slow = new PotionEffect(PotionEffectType.SLOW, 40, 2, false);

    DarkPulse() {
        this.behaviour = RaySpell.newBuilder(Target.MULTI)
                .setRayWidth(1)
                .setSpellEffectRadius(3F)
                .setMetersPerTick(2)
                .setImpactSpeed(1)
                .setEntityEffects(entity -> {
                    entity.setFireTicks(40);
                    entity.addPotionEffect(blind);
                    entity.addPotionEffect(slow);
                })
                .setEntityDamage(6)
                .setCastSound(Sound.ENTITY_WITHER_SHOOT)
                .setEntityEffects(entity -> entity.addPotionEffect(blind, true))
                .setSpellRelativeEffects((loc, world) -> {
                    world.spawnParticle(Particle.SMOKE_LARGE, loc, 3, 0.4, 0.4, 0.4, 0.1, null, true);
                    world.spawnParticle(Particle.SMOKE_NORMAL, loc, 13, 0.8, 0.8, 0.8, 0.1, null, true);
                    world.spawnParticle(Particle.ENCHANTMENT_TABLE, loc, 10, 0.8, 0.8, 0.8, 0.1, null, true);
                })
                .setEffectDistance(30)
                .setHitEffects((loc, world) -> {
                    loc.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, loc, 0, 0.0, 0.0, 0.0, 0.0, null, true);
                    loc.createExplosion(0);
                    loc.getWorld().playSound(loc, Sound.ENTITY_WITHER_HURT, SoundCategory.MASTER, 4.0F, 1.0F);
                })
                .build();
    }

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }
}
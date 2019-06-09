package me.dylan.wands.spell.implementations.commonmagic;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.handler.Behaviour;
import me.dylan.wands.spell.handler.WaveSpell;
import me.dylan.wands.util.EffectUtil;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public enum PoisonWave implements Castable {
    INSTANCE;
    private final Behaviour behaviour;

    PoisonWave() {
        this.behaviour = WaveSpell.newBuilder()
                .setEffectRadius(2.2F)
                .setEntityDamage(1)
                .setCastEffects(location -> location.getWorld().playSound(location, Sound.ENTITY_EVOKER_CAST_SPELL, 3, 1))
                .setEntityEffects(entity -> entity.addPotionEffect(
                        new PotionEffect(PotionEffectType.POISON, 60, 4, false), true))
                .setRelativeEffects(loc -> {
                    EffectUtil.spawnColoredParticle(Particle.SPELL_MOB, loc, 18, 1.2, 1.2, 1.2, 75, 140, 50, false);
                    loc.getWorld().spawnParticle(Particle.SMOKE_NORMAL, loc, 5, 1, 1, 1, 0.05, null, true);
                }).setEffectDistance(20)
                .build();
    }

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }
}

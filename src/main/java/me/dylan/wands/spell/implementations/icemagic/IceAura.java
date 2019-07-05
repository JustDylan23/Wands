package me.dylan.wands.spell.implementations.icemagic;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.handler.AuraSpell;
import me.dylan.wands.spell.handler.AuraSpell.EffectRate;
import me.dylan.wands.spell.handler.Behaviour;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public enum IceAura implements Castable {
    INSTANCE;
    private final Behaviour behaviour;
    private final PotionEffect slow = new PotionEffect(PotionEffectType.SLOW, 80, 2, false);
    private final PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, 40, 0, false);

    private final PotionEffect weak = new PotionEffect(PotionEffectType.WEAKNESS, 60, 0, false);

    IceAura() {
        this.behaviour = AuraSpell.newBuilder(EffectRate.REPEATEDLY)
                .setSpellEffectRadius(4)
                .setEffectDuration(100)
                .setPlayerEffects(player -> player.addPotionEffect(speed, true))
                .setCastSound(Sound.ENTITY_PHANTOM_FLAP)
                .setSpellRelativeEffects(loc -> loc.getWorld().spawnParticle(Particle.CLOUD, loc, 4, 1, 1, 1, 0.1, null, true))
                .setAffectedEntityEffects(entity -> {
                    entity.addPotionEffect(slow, true);
                    entity.addPotionEffect(weak, true);
                })
                .build();
    }

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }
}
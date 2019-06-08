package me.dylan.wands.spell.implementations.icemagic;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.handler.Behaviour;
import me.dylan.wands.spell.handler.WaveSpell;
import org.bukkit.Particle;
import org.bukkit.Sound;

public class Freeze implements Castable {

    private static Behaviour behaviour;

    static {
        behaviour = WaveSpell.newBuilder()
                .stopAtEntity(true)
                .setEffectRadius(2.2F)
                .setEntityDamage(1)
                .setTickSkip(2)
                .setCastEffects(loc -> loc.getWorld().playSound(loc, Sound.ENTITY_LLAMA_SWAG, 4, 1))
                .setRelativeEffects(loc -> {
                    loc.getWorld().spawnParticle(Particle.CLOUD, loc, 8, 0.1, 0.1, 0.1, 0.02, null, true);
                    loc.getWorld().spawnParticle(Particle.ENCHANTMENT_TABLE, loc, 10, 0.3, 0.3, 0.3, 0.1, null, true);
                })
                .setEffectDistance(30)
                .build();
    }

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }
}
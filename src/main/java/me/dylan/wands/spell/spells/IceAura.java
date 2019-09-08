package me.dylan.wands.spell.spells;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.types.Aura;
import me.dylan.wands.spell.types.Aura.EffectFrequency;
import me.dylan.wands.spell.types.Behavior;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class IceAura implements Castable {
    @Override
    public Behavior createBehaviour() {
        PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, 40, 0, false);
        return Aura.newBuilder(EffectFrequency.CONSTANT)
                .setSpellEffectRadius(3.5F)
                .setEffectDuration(100)
                .setPlayerEffects(player -> player.addPotionEffect(speed, true))
                .setCastSound(Sound.ENTITY_PHANTOM_FLAP)
                .setPotionEffects(
                        new PotionEffect(PotionEffectType.SLOW, 80, 2, false),
                        new PotionEffect(PotionEffectType.WEAKNESS, 60, 0, false)
                )
                .setSpellRelativeEffects((loc, spellInfo) -> spellInfo.world()
                        .spawnParticle(Particle.CLOUD, loc, 4, 1, 1, 1, 0.1, null, true))
                .build();
    }
}
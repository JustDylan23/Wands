package me.dylan.wands.spell.spells;

import me.dylan.wands.spell.SpellData;
import me.dylan.wands.spell.types.Aura;
import me.dylan.wands.spell.types.Aura.EffectFrequency;
import me.dylan.wands.spell.types.Behavior;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class IceAura implements SpellData {
    private final Behavior behavior;
    private final PotionEffect slow = new PotionEffect(PotionEffectType.SLOW, 80, 2, false);
    private final PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, 40, 0, false);

    private final PotionEffect weak = new PotionEffect(PotionEffectType.WEAKNESS, 60, 0, false);

    public IceAura() {
        this.behavior = Aura.newBuilder(EffectFrequency.CONSTANT)
                .setSpellEffectRadius(3.5F)
                .setEffectDuration(100)
                .setPlayerEffects(player -> player.addPotionEffect(speed, true))
                .setCastSound(Sound.ENTITY_PHANTOM_FLAP)
                .setSpellRelativeEffects((loc, world) -> world.spawnParticle(Particle.CLOUD, loc, 4, 1, 1, 1, 0.1, null, true))
                .setEntityEffects(entity -> {
                    entity.addPotionEffect(slow, true);
                    entity.addPotionEffect(weak, true);
                })
                .build();
    }

    @Override
    public Behavior getBehavior() {
        return behavior;
    }
}
package me.dylan.wands.spell.implementations;

import me.dylan.wands.sound.CompoundSound;
import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.handler.AuraSpell;
import me.dylan.wands.spell.handler.AuraSpell.EffectFrequency;
import me.dylan.wands.spell.handler.Behaviour;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public enum DarkAura implements Castable {
    INSTANCE;
    private final Behaviour behaviour;
    private final PotionEffect wither = new PotionEffect(PotionEffectType.WITHER, 60, 1);
    private final PotionEffect weakness = new PotionEffect(PotionEffectType.WEAKNESS, 40, 2);
    private final PotionEffect slow = new PotionEffect(PotionEffectType.SLOW, 40, 0);

    DarkAura() {
        this.behaviour = AuraSpell.newBuilder(EffectFrequency.CONSTANT)
                .setEffectDuration(120)
                .setCastSound(CompoundSound.chain()
                        .add(Sound.ENTITY_EVOKER_PREPARE_SUMMON, 0.2F)
                        .add(Sound.ENTITY_BLAZE_BURN, 0.2F)
                )
                .setSpellEffectRadius(5F)
                .setEntityEffects(entity -> {
                    entity.addPotionEffect(wither, true);
                    entity.addPotionEffect(weakness, true);
                    entity.addPotionEffect(slow, true);
                })
                .setSpellRelativeEffects((loc, world) -> world.spawnParticle(Particle.SMOKE_LARGE, loc, 10, 4, -0.2, 4, 0, null, true))
                .build();
    }

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }
}

package me.dylan.wands.spell.spells;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.accessories.sound.CompoundSound;
import me.dylan.wands.spell.spellbuilders.Aura;
import me.dylan.wands.spell.spellbuilders.Aura.EffectFrequency;
import me.dylan.wands.spell.spellbuilders.Behavior;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DarkAura implements Castable {
    @Override
    public Behavior createBehaviour() {
        return Aura.newBuilder(EffectFrequency.CONSTANT)
                .setEffectDuration(120)
                .setCastSound(CompoundSound.chain()
                        .add(Sound.ENTITY_EVOKER_PREPARE_SUMMON, 0.2F)
                        .add(Sound.ENTITY_BLAZE_BURN, 0.2F)
                )
                .setSpellEffectRadius(5.0F)
                .setPotionEffects(
                        new PotionEffect(PotionEffectType.WITHER, 60, 1),
                        new PotionEffect(PotionEffectType.WEAKNESS, 40, 2),
                        new PotionEffect(PotionEffectType.SLOW, 40, 0)
                )
                .setSpellRelativeEffects((loc, spellInfo) -> spellInfo.world()
                        .spawnParticle(Particle.SMOKE_LARGE, loc, 10, 4, -0.2, 4, 0, null, true))
                .build();
    }
}

package me.dylan.wands.spell.spells;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.tools.sound.CompoundSound;
import me.dylan.wands.spell.types.Behavior;
import me.dylan.wands.spell.types.ShockWave;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CorruptedShockWave implements Castable {
    @Override
    public Behavior createBehaviour() {
        return ShockWave.newBuilder()
                .setCastSound(CompoundSound.chain().add(Sound.ENTITY_WOLF_GROWL, 0.5F).add(Sound.ENTITY_WOLF_WHINE, 0.5F))
                .setWaveRadius(8)
                .setEntityDamage(8)
                .setPotionEffects(
                        new PotionEffect(PotionEffectType.WITHER, 60, 1, false),
                        new PotionEffect(PotionEffectType.SLOW, 60, 2, false)
                )
                .setSpellRelativeEffects((loc, spellInfo) -> spellInfo.world().spawnParticle(Particle.SPELL_MOB, loc, 5, 0.2, 0.5, 0.2, 1, null, true))
                .setExpansionDelay(3)
                .build();
    }
}

package me.dylan.wands.spell.spells;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.tools.sound.CompoundSound;
import me.dylan.wands.spell.types.Behavior;
import me.dylan.wands.spell.types.Wave;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CorruptedWave implements Castable {
    @Override
    public Behavior createBehaviour() {
        return Wave.newBuilder()
                .setCastSound(CompoundSound.chain()
                        .addAll(Sound.ENTITY_WOLF_GROWL, 0.5F, 4)
                        .addAll(Sound.ENTITY_ENDER_DRAGON_FLAP, 1, 2, 2, 2, 2, 2, 2, 2, 2)
                )
                .setSpellEffectRadius(3.0F)
                .setEntityDamage(7)
                .setEffectDistance(20)
                .setPotionEffects(new PotionEffect(PotionEffectType.WITHER, 80, 1, false))
                .setSpellRelativeEffects((loc, spellInfo) -> spellInfo.world().spawnParticle(Particle.SPELL_MOB, loc, 20, 1, 1, 1, 1, null, true))
                .build();
    }
}
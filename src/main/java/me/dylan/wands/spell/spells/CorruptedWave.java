package me.dylan.wands.spell.spells;

import me.dylan.wands.spell.SpellData;
import me.dylan.wands.spell.tools.sound.CompoundSound;
import me.dylan.wands.spell.types.Behavior;
import me.dylan.wands.spell.types.Wave;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CorruptedWave implements SpellData {

    private final Behavior behavior;
    private final PotionEffect wither = new PotionEffect(PotionEffectType.WITHER, 80, 1, false);

    public CorruptedWave() {
        this.behavior = Wave.newBuilder()
                .setCastSound(CompoundSound.chain()
                        .addAll(Sound.ENTITY_WOLF_GROWL, 0.5F, 4)
                        .addAll(Sound.ENTITY_ENDER_DRAGON_FLAP, 1, 2, 2, 2, 2, 2, 2, 2, 2)
                )
                .setSpellEffectRadius(3.0F)
                .setEntityDamage(7)
                .setEffectDistance(20)
                .setEntityEffects(entity -> entity.addPotionEffect(wither, true))
                .setSpellRelativeEffects((loc, world) -> world.spawnParticle(Particle.SPELL_MOB, loc, 20, 1, 1, 1, 1, null, true))
                .build();
    }

    @Override
    public Behavior getBehavior() {
        return behavior;
    }
}
package me.dylan.wands.spell.spells;

import me.dylan.wands.sound.CompoundSound;
import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.types.Base;
import me.dylan.wands.spell.types.Wave;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public enum CorruptedWave implements Castable {
    INSTANCE;

    private final Base baseType;
    private final PotionEffect wither = new PotionEffect(PotionEffectType.WITHER, 80, 1, false);

    CorruptedWave() {
        this.baseType = Wave.newBuilder()
                .setCastSound(CompoundSound.chain()
                        .add(Sound.ENTITY_WOLF_GROWL, 0.5F, 4)
                        .add(Sound.ENTITY_ENDER_DRAGON_FLAP, 1, 2, 2, 2, 2, 2, 2, 2, 2)
                )
                .setSpellEffectRadius(3F)
                .setEntityDamage(7)
                .setEffectDistance(20)
                .setEntityEffects(entity -> entity.addPotionEffect(wither, true))
                .setSpellRelativeEffects((loc, world) -> world.spawnParticle(Particle.SPELL_MOB, loc, 20, 1, 1, 1, 1, null, true))
                .build();
    }

    @Override
    public Base getBaseType() {
        return baseType;
    }
}
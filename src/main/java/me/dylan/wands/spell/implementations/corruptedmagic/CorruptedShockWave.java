package me.dylan.wands.spell.implementations.corruptedmagic;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.handler.Behaviour;
import me.dylan.wands.spell.handler.ShockWaveSpell;
import me.dylan.wands.spell.spelleffect.sound.CompoundSound;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public enum CorruptedShockWave implements Castable {
    INSTANCE;

    private final Behaviour behaviour;
    private final PotionEffect wither = new PotionEffect(PotionEffectType.WITHER, 60, 1, false);
    private final PotionEffect slow = new PotionEffect(PotionEffectType.SLOW, 60, 2, false);

    CorruptedShockWave() {
        this.behaviour = ShockWaveSpell.newBuilder()
                .setCastSound(CompoundSound.chain().add(Sound.ENTITY_WOLF_GROWL, 0.5F).add(Sound.ENTITY_WOLF_WHINE, 0.5F))
                .setWaveRadius(10)
                .setEntityDamage(8)
                .setEntityEffects(entity -> {
                    entity.addPotionEffect(wither, true);
                    entity.addPotionEffect(slow, true);
                })
                .setSpellRelativeEffects((loc, world) -> world.spawnParticle(Particle.SPELL_MOB, loc, 5, 0.2, 0.5, 0.2, 1, null, true))
                .setExpansionDelay(3)
                .build();
    }

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }
}

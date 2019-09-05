package me.dylan.wands.spell.spells;

import me.dylan.wands.spell.SpellData;
import me.dylan.wands.spell.types.Behavior;
import me.dylan.wands.spell.types.Wave;
import me.dylan.wands.spell.util.SpellEffectUtil;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PoisonWave implements SpellData {
    private final Behavior behavior;
    private final PotionEffect poison = new PotionEffect(PotionEffectType.POISON, 120, 3, false);

    public PoisonWave() {
        this.behavior = Wave.newBuilder()
                .setSpellEffectRadius(2.0f)
                .setEntityDamage(6)
                .setEffectDistance(20)
                .setCastSound(Sound.ENTITY_EVOKER_CAST_SPELL)
                .setEntityEffects(entity -> entity.addPotionEffect(poison, true))
                .setSpellRelativeEffects((loc, world) -> {
                    SpellEffectUtil.spawnColoredParticle(Particle.SPELL_MOB, loc, 18, 1.2, 1.2, 1.2, 75, 140, 50, false);
                    world.spawnParticle(Particle.SMOKE_NORMAL, loc, 5, 1, 1, 1, 0.05, null, true);
                    world.spawnParticle(Particle.SMOKE_NORMAL, loc, 5, 1, 1, 1, 0.05, null, true);
                })
                .build();
    }

    @Override
    public Behavior getBehavior() {
        return behavior;
    }
}

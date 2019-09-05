package me.dylan.wands.spell.spells;

import me.dylan.wands.spell.SpellData;
import me.dylan.wands.spell.types.Aura;
import me.dylan.wands.spell.types.Aura.AuraParticleType;
import me.dylan.wands.spell.types.Aura.EffectFrequency;
import me.dylan.wands.spell.types.Behavior;
import org.bukkit.Particle;
import org.bukkit.Sound;

public class MephiAura implements SpellData {
    private final Behavior behavior;

    public MephiAura() {
        this.behavior = Aura.newBuilder(EffectFrequency.CONSTANT)
                .setSpellEffectRadius(3.5f)
                .setEffectDuration(160)
                .setAuraParticleType(AuraParticleType.CIRCLE)
                .setCastSound(Sound.ENTITY_ENDER_DRAGON_FLAP)
                .setSpellRelativeEffects((loc, world) -> {
                    world.spawnParticle(Particle.SMOKE_NORMAL, loc.add(0, 4, 0), 1, 0.3, 0.3, 0.3, 0.1, null, true);
                    world.spawnParticle(Particle.VILLAGER_HAPPY, loc, 1, 0.3, 0.3, 0.3, 0.1, null, true);
                })
                .setKnockBack(0.3f)
                .build();
    }

    @Override
    public Behavior getBehavior() {
        return behavior;
    }
}

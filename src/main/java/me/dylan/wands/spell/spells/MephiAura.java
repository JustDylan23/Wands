package me.dylan.wands.spell.spells;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.spellbuilders.Aura;
import me.dylan.wands.spell.spellbuilders.Aura.AuraParticleType;
import me.dylan.wands.spell.spellbuilders.Aura.EffectFrequency;
import me.dylan.wands.spell.spellbuilders.Behavior;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;

public class MephiAura implements Castable {
    @Override
    public Behavior createBehaviour() {
        return Aura.newBuilder(EffectFrequency.CONSTANT)
                .setSpellEffectRadius(3.5f)
                .setEffectDuration(160)
                .setAuraParticleType(AuraParticleType.EMIT_AS_CIRCLE)
                .setCastSound(Sound.ENTITY_ENDER_DRAGON_FLAP)
                .setSpellRelativeEffects((loc, spellInfo) -> {
                    World world = spellInfo.world();
                    world.spawnParticle(Particle.SMOKE_NORMAL, loc.add(0, 4, 0), 1, 0.3, 0.3, 0.3, 0.1, null, true);
                    world.spawnParticle(Particle.VILLAGER_HAPPY, loc, 1, 0.3, 0.3, 0.3, 0.1, null, true);
                })
                .setKnockBack(0.3f)
                .build();
    }
}

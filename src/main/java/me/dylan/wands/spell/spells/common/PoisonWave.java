package me.dylan.wands.spell.spells.common;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.spellbuilders.Behavior;
import me.dylan.wands.spell.spellbuilders.Wave;
import me.dylan.wands.spell.spells.AffinityType;
import me.dylan.wands.spell.util.SpellEffectUtil;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PoisonWave implements Castable {
    @Override
    public AffinityType[] getAffinityTypes() {
        return new AffinityType[]{AffinityType.WITCH_MAGIC, AffinityType.CORRUPTED_MAGIC, AffinityType.GRAVITY_MAGIC};
    }

    @Override
    public Behavior createBehaviour() {
        return Wave.newBuilder()
                .setSpellEffectRadius(2.0f)
                .setEntityDamage(6)
                .setEffectDistance(20)
                .setCastSound(Sound.ENTITY_EVOKER_CAST_SPELL)
                .setPotionEffects(new PotionEffect(PotionEffectType.POISON, 120, 3, false))
                .setSpellRelativeEffects((loc, spellInfo) -> {
                    SpellEffectUtil.spawnEntityEffect(loc, 18, 1.2, 1.2, 1.2, 75, 140, 50);
                    World world = spellInfo.world();
                    world.spawnParticle(Particle.SMOKE, loc, 5, 1, 1, 1, 0.05, null, true);
                    world.spawnParticle(Particle.SMOKE, loc, 5, 1, 1, 1, 0.05, null, true);
                })
                .build();
    }
}

package me.dylan.wands.spell.spells.blood;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.accessories.sound.CompoundSound;
import me.dylan.wands.spell.spellbuilders.Behavior;
import me.dylan.wands.spell.spellbuilders.Wave;
import me.dylan.wands.spell.spells.AffinityType;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BloodWave implements Castable {
    @Override
    public AffinityType[] getAffinityTypes() {
        return new AffinityType[]{AffinityType.BLOOD_MAGIC};
    }

    @Override
    public Behavior createBehaviour() {
        return Wave.newBuilder()
                .setEffectDistance(30)
                .setSpellEffectRadius(2.0F)
                .setEntityDamage(5)
                .setCastSound(CompoundSound.chain()
                        .add(Sound.ENTITY_FIREWORK_ROCKET_BLAST)
                        .addAll(Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, 1, 20, 5, 3)
                )
                .setPotionEffects(new PotionEffect(PotionEffectType.WITHER, 80, 4, false))
                .setSpellRelativeEffects((loc, spellInfo) -> {
                    World world = spellInfo.world();
                    world.spawnParticle(Particle.SMOKE, loc, 10, 0.2, 0.2, 0.2, 0.1, null, true);
                    world.spawnParticle(Particle.LARGE_SMOKE, loc, 10, 1, 1, 1, 0.1, null, true);
                    world.spawnParticle(Particle.BLOCK, loc, 12, 0.6, 0.6, 0.6, 0.15, BloodMagicConstants.BLOCK_CRACK_REDSTONE, true);
                })
                .build();
    }
}

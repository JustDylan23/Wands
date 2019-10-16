package me.dylan.wands.spell.spells;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.spellbuilders.Behavior;
import me.dylan.wands.spell.spellbuilders.BuildableBehaviour;
import me.dylan.wands.spell.spellbuilders.Ray;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BloodStun implements Castable {
    @Override
    public Behavior createBehaviour() {
        return Ray.newBuilder(BuildableBehaviour.Target.SINGLE)
                .setCastSound(Sound.ENTITY_FIREWORK_ROCKET_BLAST)
                .setEntityDamage(8)
                .setPotionEffects(new PotionEffect(PotionEffectType.SLOW, 180, 3, false))
                .setHitEffects((location, spellInfo) -> spellInfo.world().playSound(location, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, 4, 1))
                .setRayWidth(1)
                .setSpellRelativeEffects((loc, spellInfo) -> {
                    World world = spellInfo.world();
                    world.spawnParticle(Particle.SMOKE_NORMAL, loc, 2, 0.2, 0.2, 0.2, 0.04, null, true);
                    world.spawnParticle(Particle.DRIP_LAVA, loc, 2, 0.3, 0.3, 0.3, 0.04, null, true);
                    world.spawnParticle(Particle.BLOCK_CRACK, loc, 1, 0.6, 0.7, 0.6, 0.15, BloodSpark.BLOCK_CRACK_REDSTONE, true);
                }).setEffectDistance(30)
                .build();
    }
}

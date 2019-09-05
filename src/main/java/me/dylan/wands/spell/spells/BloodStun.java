package me.dylan.wands.spell.spells;

import me.dylan.wands.spell.SpellData;
import me.dylan.wands.spell.types.Behavior;
import me.dylan.wands.spell.types.Behavior.Target;
import me.dylan.wands.spell.types.Ray;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BloodStun implements SpellData {
    private final PotionEffect slow = new PotionEffect(PotionEffectType.SLOW, 180, 3, false);
    private final Behavior behavior;

    public BloodStun() {
        this.behavior = Ray.newBuilder(Target.SINGLE)
                .setCastSound(Sound.ENTITY_FIREWORK_ROCKET_BLAST)
                .setEntityDamage(8)
                .setEntityEffects(entity -> entity.addPotionEffect(slow, true))
                .setHitEffects((location, world) -> world.playSound(location, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, 4, 1))
                .setRayWidth(1)
                .setSpellRelativeEffects((loc, world) -> {
                    world.spawnParticle(Particle.SMOKE_NORMAL, loc, 2, 0.2, 0.2, 0.2, 0.04, null, true);
                    world.spawnParticle(Particle.DRIP_LAVA, loc, 2, 0.3, 0.3, 0.3, 0.04, null, true);
                    world.spawnParticle(Particle.BLOCK_CRACK, loc, 1, 0.6, 0.7, 0.6, 0.15, Material.REDSTONE_BLOCK.createBlockData(), true);
                }).setEffectDistance(30)
                .build();
    }

    @Override
    public Behavior getBehavior() {
        return behavior;
    }
}

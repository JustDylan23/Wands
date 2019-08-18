package me.dylan.wands.spell.spells;

import me.dylan.wands.spell.SpellData;
import me.dylan.wands.spell.SpellEffectUtil;
import me.dylan.wands.spell.types.Behaviour;
import me.dylan.wands.spell.types.Spark;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;

public class BloodSpark implements SpellData {
    private final Behaviour behaviour;

    public BloodSpark() {
        this.behaviour = Spark.newBuilder(Behaviour.Target.MULTI)
                .setSpellEffectRadius(2.8F)
                .setEntityDamage(12)
                .setSpellRelativeEffects((loc, world) -> {
                    world.spawnParticle(Particle.SMOKE_LARGE, loc, 20, 0.2, 0.2, 0.2, 0.1, null, true);
                    world.spawnParticle(Particle.BLOCK_CRACK, loc, 20, 0.6, 0.7, 0.6, 0.15, Material.REDSTONE_BLOCK.createBlockData(), true);
                    SpellEffectUtil.runTaskLater(() ->
                            world.playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, SoundCategory.MASTER, 4.0F, 1.0F), 10);
                })
                .setCastSound(Sound.ENTITY_FIREWORK_ROCKET_BLAST)

                .setEffectDistance(30)
                .build();
    }

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }
}

package me.dylan.wands.spell.spells;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.SpellEffectUtil;
import me.dylan.wands.spell.types.Behaviour;
import me.dylan.wands.spell.types.Spark;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.data.BlockData;

public enum CorruptedSpark implements Castable {
    INSTANCE;

    private final Behaviour behaviour;
    private final BlockData obsidian = Material.OBSIDIAN.createBlockData();


    CorruptedSpark() {
        this.behaviour = Spark.newBuilder(Behaviour.Target.MULTI)
                .setSpellEffectRadius(2.8F)
                .setEntityDamage(12)
                .setSpellRelativeEffects((loc, world) -> {
                    world.spawnParticle(Particle.SPELL_MOB, loc, 20, 0.6, 0.6, 0.6, 1, null, true);
                    world.spawnParticle(Particle.BLOCK_CRACK, loc, 10, 0.8, 0.3, 0.8, 0.4, obsidian, true);

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
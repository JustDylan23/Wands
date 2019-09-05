package me.dylan.wands.spell.spells;

import me.dylan.wands.spell.SpellData;
import me.dylan.wands.spell.types.Behavior;
import me.dylan.wands.spell.types.Spark;
import me.dylan.wands.spell.util.SpellEffectUtil;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.data.BlockData;

public class CorruptedSpark implements SpellData {

    private final Behavior behavior;
    private final BlockData obsidian = Material.OBSIDIAN.createBlockData();

    public CorruptedSpark() {
        this.behavior = Spark.newBuilder(Behavior.Target.MULTI)
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
    public Behavior getBehavior() {
        return behavior;
    }
}
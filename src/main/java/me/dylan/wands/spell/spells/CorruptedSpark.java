package me.dylan.wands.spell.spells;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.SpellEffectUtil;
import me.dylan.wands.spell.types.Spark;
import me.dylan.wands.spell.types.Base;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.data.BlockData;

public enum CorruptedSpark implements Castable {
    INSTANCE;

    private final Base baseType;
    private final BlockData obsidian = Material.OBSIDIAN.createBlockData();


    CorruptedSpark() {
        this.baseType = Spark.newBuilder(Base.Target.MULTI)
                .setSpellEffectRadius(2.5F)
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
    public Base getBaseType() {
        return baseType;
    }
}
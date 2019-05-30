package me.dylan.wands.spell.implementation;

import me.dylan.wands.spell.model.CastableSpell;
import me.dylan.wands.spell.spellhandler.SparkSpell;
import me.dylan.wands.spell.spellhandler.SpellBehaviour;
import me.dylan.wands.util.EffectUtil;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;

public class BloodSpark extends CastableSpell {
    @Override
    public SpellBehaviour getSpellBehaviour() {
        return SparkSpell.newBuilder()
                .setEffectRadius(2.2F)
                .setEntityDamage(10)
                .setVisualEffects(loc -> {
                    loc.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 20, 0.2, 0.2, 0.2, 0.1, null, true);
                    loc.getWorld().spawnParticle(Particle.BLOCK_CRACK, loc, 20, 0.6, 0.7, 0.6, 0.15, Material.REDSTONE_BLOCK.createBlockData(), true);

                    EffectUtil.runTaskLater(() ->
                            loc.getWorld().playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, SoundCategory.MASTER, 4.0F, 1.0F), 10);
                })
                .setCastEffects(loc -> loc.getWorld().playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_BLAST, SoundCategory.MASTER, 4.0F, 1.0F))
                .setEffectDistance(30)
                .build();
    }
}

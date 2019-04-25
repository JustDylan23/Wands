package me.dylan.wands.spells;

import me.dylan.wands.spellbehaviour.SparkSpell;
import me.dylan.wands.spellbehaviour.SpellBehaviour;
import me.dylan.wands.spellfoundation.CastableSpell;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;

public class BloodExplode extends CastableSpell {

    @Override
    public SpellBehaviour getSpellBehaviour() {
        return SparkSpell.newBuilder()
                .setEffectRadius(4.5F)
                .setEntityDamage(7)
                .setEntityEffects(entity -> entity.setFireTicks(40))
                .setVisualEffects(loc -> {
                    loc.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 20, 1, 1, 1, 0.1, null, true);
                    loc.getWorld().spawnParticle(Particle.BLOCK_CRACK, loc, 15, 1, 1, 1, 0.15, Material.REDSTONE_BLOCK.createBlockData(), true);
                    loc.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, loc, 0, 0.0, 0.0, 0.0, 0.0, null, true);
                    loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, SoundCategory.MASTER, 4.0F, 1.0F);
                })
                .setCastEffects(loc -> loc.getWorld().playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_BLAST, SoundCategory.MASTER, 4.0F, 1.0F))
                .setEffectDistance(30)
                .build();
    }
}

package me.dylan.wands.spells;

import me.dylan.wands.CastableSpell;
import me.dylan.wands.spellbehaviour.SparkSpell;
import org.bukkit.*;
import org.bukkit.entity.Player;

public class BloodSpark extends CastableSpell {

    private final SparkSpell spellBehaviour;

    @Override
    protected void cast(Player player) {
        spellBehaviour.executeFrom(player);
    }

    public BloodSpark() {
        super("BloodSpark");
        this.spellBehaviour = new SparkSpell.Builder()
                .setEffectDistance(30)
                .setEffectAreaRange(2.2F)
                .setEntityDamage(5)
                .setVisualEffects(loc -> {
                    loc.add(0, 1, 0);
                    loc.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 20, 0.2, 0.2, 0.2, 0.1, null, true);
                    loc.getWorld().spawnParticle(Particle.BLOCK_CRACK, loc, 12, 0.6, 0.7, 0.6, 0.15, Material.REDSTONE_BLOCK.createBlockData(), true);

                    Bukkit.getScheduler().runTaskLater(plugin, () ->
                            loc.getWorld().playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, SoundCategory.MASTER, 4.0F, 1.0F), 10L);
                })
                .setCastEffects(loc -> loc.getWorld().playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_BLAST, SoundCategory.MASTER, 4.0F, 1.0F))
                .build();
    }
}

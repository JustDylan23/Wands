package me.dylan.wands.spells;

import me.dylan.wands.Spell;
import me.dylan.wands.spellbehaviour.SparkSpell;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

public class Spark extends Spell {

    private final SparkSpell spellBehaviour;

    private Spark() {
        super("Spark");
        this.spellBehaviour = new SparkSpell.Builder()
                .setEffectDistance(30)
                .setEffectAreaRange(2.2F)
                .setEntityDamage(5)
                .setVisualEffects(loc -> {
                    loc.add(0, 1, 0);
                    loc.getWorld().spawnParticle(Particle.SPELL_WITCH, loc, 30, 0.6, 0.7, 0.6, 0.2, null, true);
                    loc.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 50, 0.2, 0.2, 0.2, 0.08, null, true);

                    loc.getWorld().playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_BLAST, SoundCategory.MASTER, 4.0F, 1.0F);
                    Bukkit.getScheduler().runTaskLater(plugin, () ->
                            loc.getWorld().playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, SoundCategory.MASTER, 4.0F, 1.0F), 10L);
                })
                .build();
    }

    public static Spark getInstance() {
        return Spark.InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder {
        private final static Spark INSTANCE = new Spark();
    }

    @Override
    protected void cast(Player player) {
        spellBehaviour.executeFrom(player);
    }
}

package me.dylan.wands.spell.spells;

import me.dylan.wands.Main;
import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.types.Behaviour;
import me.dylan.wands.spell.types.Spark;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;

public enum MagicSpark implements Castable {
    INSTANCE;
    private final Behaviour behaviour;

    MagicSpark() {
        this.behaviour = Spark.newBuilder(Behaviour.Target.MULTI)
                .setSpellEffectRadius(2.8F)
                .setEntityDamage(12)
                .setSpellRelativeEffects((loc, world) -> {
                    world.spawnParticle(Particle.SPELL_WITCH, loc, 30, 0.6, 0.7, 0.6, 0.2, null, true);
                    world.spawnParticle(Particle.SMOKE_LARGE, loc, 50, 0.2, 0.2, 0.2, 0.08, null, true);

                    Bukkit.getScheduler().runTaskLater(Main.getPlugin(), () ->
                            world.playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, SoundCategory.MASTER, 4.0F, 1.0F), 10L);
                })
                .setCastSound(Sound.ENTITY_FIREWORK_ROCKET_BLAST)
                .setEffectDistance(25)
                .build();
    }

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }

    @Override
    public String getDisplayName() {
        return "Spark";
    }
}

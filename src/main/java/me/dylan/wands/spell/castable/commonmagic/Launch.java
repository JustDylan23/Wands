package me.dylan.wands.spell.castable.commonmagic;

import me.dylan.wands.Main;
import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.handler.Behaviour;
import me.dylan.wands.spell.handler.SparkSpell;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.util.Vector;

public class Launch implements Castable {

    private static Behaviour behaviour;

    static {
        behaviour = SparkSpell.newBuilder()
                .setEffectRadius(2.2F)
                .setEntityDamage(3)
                .setEntityEffects(entity -> entity.setVelocity(new Vector(0, 1.2, 0)))
                .setRelativeEffects(loc -> {
                    loc.getWorld().spawnParticle(Particle.SPELL_WITCH, loc, 30, 0.6, 0.7, 0.6, 0.3, null, true);
                    loc.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 50, 0.2, 0.2, 0.2, 0.1, null, true);

                    loc.getWorld().playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_BLAST, SoundCategory.MASTER, 4.0F, 1.0F);
                    Bukkit.getScheduler().runTaskLater(Main.getPlugin(), () ->
                            loc.getWorld().playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, SoundCategory.MASTER, 4.0F, 1.0F), 10L);
                }).setEffectDistance(30)
                .build();
    }

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }
}

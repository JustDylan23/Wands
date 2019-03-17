package me.dylan.wands.spells;

import me.dylan.wands.SpellFoundation.CastableSpell;
import me.dylan.wands.SpellFoundation.SpellBehaviour;
import me.dylan.wands.SpellFoundation.SpellBehaviour.BaseProperties;
import me.dylan.wands.SpellFoundation.SpellBehaviour.SparkSpell.Builder;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;

public class Spark extends CastableSpell {

    public Spark() {
        super("Spark");
    }

    @Override
    public SpellBehaviour getSpellBehaviour() {
        BaseProperties baseProperties = SpellBehaviour.createEmptyBaseProperties()
                .setEffectAreaRange(2.2F)
                .setEntityDamage(10)
                .setVisualEffects(loc -> {
                    loc.add(0, 1, 0);
                    loc.getWorld().spawnParticle(Particle.SPELL_WITCH, loc, 30, 0.6, 0.7, 0.6, 0.2, null, true);
                    loc.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 50, 0.2, 0.2, 0.2, 0.08, null, true);

                    Bukkit.getScheduler().runTaskLater(plugin, () ->
                            loc.getWorld().playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, SoundCategory.MASTER, 4.0F, 1.0F), 10L);
                })
                .setCastEffects(loc -> loc.getWorld().playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_BLAST, SoundCategory.MASTER, 4.0F, 1.0F));

        return new Builder(baseProperties).setEffectDistance(25).build();
    }
}

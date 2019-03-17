package me.dylan.wands.spells;

import me.dylan.wands.SpellFoundation.CastableSpell;
import me.dylan.wands.SpellFoundation.SpellBehaviour;
import me.dylan.wands.SpellFoundation.SpellBehaviour.BaseProperties;
import me.dylan.wands.SpellFoundation.SpellBehaviour.SparkSpell.Builder;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Confuse extends CastableSpell {
    public Confuse() {
        super("Confuse");
    }

    @Override
    public SpellBehaviour getSpellBehaviour() {
        BaseProperties baseProperties = SpellBehaviour.createEmptyBaseProperties()
                .setEffectAreaRange(4F)
                .setEntityDamage(6)
                .setEntityEffects(entity -> ((LivingEntity) entity).addPotionEffect(
                        new PotionEffect(PotionEffectType.CONFUSION, 240, 4, false), true))
                .setVisualEffects(loc -> {
                    loc.add(0, 1, 0);
                    loc.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 30, 1, 1, 1, 0.08, null, true);
                    loc.getWorld().spawnParticle(Particle.SMOKE_NORMAL, loc, 30, 1, 1, 1, 0.08, null, true);
                    loc.getWorld().spawnParticle(Particle.SPELL_WITCH, loc, 30, 1, 1, 1, 0.15, null, true);
                    Bukkit.getScheduler().runTaskLater(plugin, () ->
                            loc.getWorld().playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, SoundCategory.MASTER, 4.0F, 1.0F), 10L);
                })
                .setCastEffects(loc -> loc.getWorld().playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_BLAST, SoundCategory.MASTER, 4.0F, 1.0F));

        return new Builder(baseProperties).setEffectDistance(30).build();
    }
}

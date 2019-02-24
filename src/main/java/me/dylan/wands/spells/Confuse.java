package me.dylan.wands.spells;

import me.dylan.wands.CastableSpell;
import me.dylan.wands.spellbehaviour.SparkSpell;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Confuse extends CastableSpell {

    private final SparkSpell spellBehaviour;

    @Override
    protected void cast(Player player) {
        spellBehaviour.executeFrom(player);
    }

    public Confuse() {
        super("Confuse");
        this.spellBehaviour = new SparkSpell.Builder()
                .setEffectDistance(30)
                .setEffectAreaRange(2.2F)
                .setEntityDamage(5)
                .setEntityEffects(entity -> ((LivingEntity) entity).addPotionEffect(
                        new PotionEffect(PotionEffectType.CONFUSION, 240, 4, false), true))
                .setVisualEffects(loc -> {
                    loc.add(0, 1, 0);
                    loc.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 17, 1, 1, 1, 0.08, null, true);
                    loc.getWorld().spawnParticle(Particle.SMOKE_NORMAL, loc, 30, 1, 1, 1, 0.08, null, true);
                    loc.getWorld().spawnParticle(Particle.SPELL_WITCH, loc, 15, 1, 1, 1, 0.15, null, true);
                    Bukkit.getScheduler().runTaskLater(plugin, () ->
                            loc.getWorld().playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, SoundCategory.MASTER, 4.0F, 1.0F), 10L);
                })
                .setCastEffects(loc -> loc.getWorld().playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_BLAST, SoundCategory.MASTER, 4.0F, 1.0F))
                .build();
    }
}
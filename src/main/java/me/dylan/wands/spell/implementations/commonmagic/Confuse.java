package me.dylan.wands.spell.implementations.commonmagic;

import me.dylan.wands.Main;
import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.handler.Behaviour;
import me.dylan.wands.spell.handler.SparkSpell;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public enum Confuse implements Castable {
    INSTANCE;
    private final Behaviour behaviour;

    Confuse() {
        this.behaviour = SparkSpell.newBuilder()
                .setEffectRadius(4F)
                .setEntityDamage(6)
                .setEntityEffects(entity -> entity.addPotionEffect(
                        new PotionEffect(PotionEffectType.CONFUSION, 240, 4, false), true))
                .setRelativeEffects(loc -> {
                    loc.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 30, 1, 1, 1, 0.08, null, true);
                    loc.getWorld().spawnParticle(Particle.SMOKE_NORMAL, loc, 30, 1, 1, 1, 0.08, null, true);
                    loc.getWorld().spawnParticle(Particle.SPELL_WITCH, loc, 30, 1, 1, 1, 0.15, null, true);
                    Bukkit.getScheduler().runTaskLater(Main.getPlugin(), () ->
                            loc.getWorld().playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, SoundCategory.MASTER, 4.0F, 1.0F), 10L);
                })
                .setCastEffects(loc -> loc.getWorld().playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_BLAST, SoundCategory.MASTER, 4.0F, 1.0F))
                .setEffectDistance(30)
                .build();
    }

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }
}

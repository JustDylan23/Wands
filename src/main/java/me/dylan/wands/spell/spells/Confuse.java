package me.dylan.wands.spell.spells;

import me.dylan.wands.Main;
import me.dylan.wands.spell.SpellData;
import me.dylan.wands.spell.types.Behavior;
import me.dylan.wands.spell.types.Spark;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Confuse implements SpellData {
    private final Behavior behavior;
    private final PotionEffect confusion = new PotionEffect(PotionEffectType.CONFUSION, 260, 4, false);

    public Confuse() {
        this.behavior = Spark.newBuilder(Behavior.Target.MULTI)
                .setSpellEffectRadius(3.0F)
                .setEntityDamage(8)
                .setEntityEffects(entity -> entity.addPotionEffect(confusion, true))
                .setSpellRelativeEffects((loc, world) -> {
                    world.spawnParticle(Particle.SMOKE_LARGE, loc, 50, 1.4, 1.2, 1.4, 0.08, null, true);
                    world.spawnParticle(Particle.SMOKE_NORMAL, loc, 50, 1.4, 1.2, 1.4, 0.08, null, true);
                    world.spawnParticle(Particle.SPELL_WITCH, loc, 50, 1.4, 1.2, 1.4, 0.15, null, true);
                    Bukkit.getScheduler().runTaskLater(Main.getPlugin(), () ->
                            world.playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, SoundCategory.MASTER, 4.0F, 1.0F), 10L);
                })
                .setCastSound(Sound.ENTITY_FIREWORK_ROCKET_BLAST)
                .setEffectDistance(30)
                .build();
    }

    @Override
    public Behavior getBehavior() {
        return behavior;
    }
}

package me.dylan.wands.spell.spells;

import me.dylan.wands.Main;
import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.types.Spark;
import me.dylan.wands.spell.types.Base;
import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public enum Confuse implements Castable {
    INSTANCE;
    private final Base baseType;
    private final PotionEffect confusion = new PotionEffect(PotionEffectType.CONFUSION, 240, 4, false);

    Confuse() {
        this.baseType = Spark.newBuilder(Base.Target.MULTI)
                .setSpellEffectRadius(3F)
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
    public Base getBaseType() {
        return baseType;
    }
}

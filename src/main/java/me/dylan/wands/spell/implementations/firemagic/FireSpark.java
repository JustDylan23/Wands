package me.dylan.wands.spell.implementations.firemagic;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.SpellEffectUtil;
import me.dylan.wands.spell.handler.Behaviour;
import me.dylan.wands.spell.handler.SparkSpell;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;

public enum FireSpark implements Castable {
    INSTANCE;
    private final Behaviour behaviour;

    FireSpark() {
        this.behaviour = SparkSpell.newBuilder()
                .setSpellEffectRadius(2.2F)
                .setAffectedEntityDamage(4)
                .setAffectedEntityEffects(entity -> entity.setFireTicks(100))
                .setSpellRelativeEffects(loc -> {
                    loc.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 10, 0.3, 0.3, 0.3, 0.1, null, true);
                    loc.getWorld().spawnParticle(Particle.SMOKE_NORMAL, loc, 10, 0.2, 0.2, 0.2, 0.1, null, true);
                    loc.getWorld().spawnParticle(Particle.FLAME, loc, 10, 0.8, 0.8, 0.8, 0.1, null, true);
                    loc.getWorld().spawnParticle(Particle.LAVA, loc, 10, 0.8, 0.8, 0.8, 0, null, true);
                    SpellEffectUtil.runTaskLater(() ->
                            loc.getWorld().playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, SoundCategory.MASTER, 4.0F, 1.0F), 10);
                })
                .setCastSound(Sound.ENTITY_FIREWORK_ROCKET_BLAST)

                .setEffectDistance(30)
                .build();
    }

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }
}
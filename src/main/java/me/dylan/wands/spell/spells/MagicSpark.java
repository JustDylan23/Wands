package me.dylan.wands.spell.spells;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.accessories.sound.RepeatableSound;
import me.dylan.wands.spell.accessories.sound.SoundEffect;
import me.dylan.wands.spell.spellbuilders.Behavior;
import me.dylan.wands.spell.spellbuilders.BuildableBehaviour;
import me.dylan.wands.spell.spellbuilders.Spark;
import org.bukkit.Particle;
import org.bukkit.Sound;

public class MagicSpark implements Castable {
    static final SoundEffect SPARK_SOUND = RepeatableSound.from(Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, 1, 10);

    @Override
    public Behavior createBehaviour() {
        return Spark.newBuilder(BuildableBehaviour.Target.MULTI)
                .setSpellEffectRadius(2.8F)
                .setEntityDamage(12)
                .setSpellRelativeEffects((loc, spellInfo) -> {
                    spellInfo.world().spawnParticle(Particle.SPELL_WITCH, loc, 30, 0.6, 0.7, 0.6, 0.2, null, true);
                    spellInfo.world().spawnParticle(Particle.SMOKE_LARGE, loc, 50, 0.2, 0.2, 0.2, 0.08, null, true);
                    SPARK_SOUND.play(loc);
                })
                .setCastSound(Sound.ENTITY_FIREWORK_ROCKET_BLAST)
                .setEffectDistance(25)
                .build();
    }

    @Override
    public String getDisplayName() {
        return "Spark";
    }
}

package me.dylan.wands.spell.spells.blood;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.accessories.KnockBack;
import me.dylan.wands.spell.spellbuilders.Behavior;
import me.dylan.wands.spell.spellbuilders.Spark;
import me.dylan.wands.spell.spellbuilders.Spark.Target;
import org.bukkit.Sound;

public class BloodExplode implements Castable {
    @Override
    public Behavior createBehaviour() {
        return Spark.newBuilder(Target.MULTI)
                .setEntityDamage(10)
                .setKnockBack(KnockBack.EXPLOSION)
                .setSpellEffectRadius(3.0F)
                .setEntityEffects((entity, spellInfo) -> entity.setFireTicks(80))
                .setSpellRelativeEffects(BloodMagicConstants.BLOOD_EXPLOSION_EFFECTS)
                .setCastSound(Sound.ENTITY_FIREWORK_ROCKET_BLAST)
                .setEffectDistance(30)
                .build();
    }
}

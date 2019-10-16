package me.dylan.wands.spell.spells;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.accessories.KnockBack;
import me.dylan.wands.spell.spellbuilders.Behavior;
import me.dylan.wands.spell.spellbuilders.Circle;
import me.dylan.wands.spell.spellbuilders.Circle.CirclePlacement;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DarkCircle implements Castable {
    @Override
    public Behavior createBehaviour() {
        return Circle.newBuilder(CirclePlacement.RELATIVE)
                .setCircleRadius(4)
                .setEntityDamage(8)
                .setSpellEffectRadius(4.0F)
                .setEntityEffects((entity, spellInfo) -> spellInfo.world().createExplosion(entity.getLocation(), 0.0f))
                .setPotionEffects(new PotionEffect(PotionEffectType.BLINDNESS, 40, 0, false))
                .setMetersPerTick(2)
                .setCastSound(Sound.ENTITY_ENDER_DRAGON_FLAP)
                .setSpellRelativeEffects((loc, spellInfo) -> {
                    World world = spellInfo.world();
                    world.spawnParticle(Particle.SMOKE_LARGE, loc, 10, 0.3, 0.3, 0.3, 0.1, null, true);
                    world.spawnParticle(Particle.ENCHANTMENT_TABLE, loc, 8, 0.3, 0.3, 0.3, 0.1, null, true);
                })
                .setKnockBack(KnockBack.SIDEWAYS)
                .setCircleHeight(1)
                .build();
    }
}
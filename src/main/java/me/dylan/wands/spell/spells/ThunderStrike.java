package me.dylan.wands.spell.spells;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.accessories.KnockBack;
import me.dylan.wands.spell.spellbuilders.Behavior;
import me.dylan.wands.spell.spellbuilders.BuildableBehaviour.Target;
import me.dylan.wands.spell.spellbuilders.Ray;
import me.dylan.wands.spell.util.SpellEffectUtil;
import me.dylan.wands.utils.Common;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;

public class ThunderStrike implements Castable {
    @Override
    public Behavior createBehaviour() {
        return Ray.newBuilder(Target.MULTI)
                .setEffectDistance(40)
                .setEntityDamage(8)
                .setKnockBack(KnockBack.EXPLOSION)
                .setCastSound(Sound.ENTITY_WITHER_SHOOT)
                .setEntityEffects((entity, spellInfo) -> entity.setFireTicks(80))
                .setMetersPerTick(2)
                .setSpellRelativeEffects((loc, spellInfo) -> {
                    World world = spellInfo.world();
                    world.spawnParticle(Particle.CLOUD, loc, 5, 0.2, 0.2, 0.2, 0.05, null, true);
                    world.spawnParticle(Particle.ENCHANTMENT_TABLE, loc, 15, 0.5, 0.5, 0.5, 1, null, true);
                })
                .setHitEffects((loc, spellInfo) -> {
                    World world = spellInfo.world();
                    world.spawnParticle(Particle.CLOUD, loc, 40, 2, 2, 2, 0.2, null, true);
                    world.spawnParticle(Particle.SMOKE_NORMAL, loc, 20, 2, 2, 2, 0.2, null, true);
                    world.spawnParticle(Particle.SMOKE_LARGE, loc, 5, 2, 2, 2, 0.2, null, true);
                    world.spawnParticle(Particle.EXPLOSION_HUGE, loc, 0, 0.0, 0.0, 0.0, 0.0, null, true);
                    Common.runRepeatableTaskLater(() -> {
                        Location loc2 = SpellEffectUtil.randomizeLoc(loc, 3, 1, 3);
                        world.playSound(loc2, Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 4, 1);
                        world.strikeLightningEffect(loc2);
                    }, 1, 2, 3);
                })
                .setSpellEffectRadius(4.0F)
                .build();
    }
}
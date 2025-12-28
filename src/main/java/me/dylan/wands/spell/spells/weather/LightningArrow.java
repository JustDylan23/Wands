package me.dylan.wands.spell.spells.weather;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.spellbuilders.Behavior;
import me.dylan.wands.spell.spellbuilders.MagicProjectile;
import me.dylan.wands.spell.spells.AffinityType;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Arrow;

public class LightningArrow implements Castable {
    @Override
    public AffinityType[] getAffinityTypes() {
        return new AffinityType[]{AffinityType.WEATHER_MAGIC};
    }

    @Override
    public Behavior createBehaviour() {
        return MagicProjectile.newBuilder(Arrow.class, 2.2F)
                .setHitEffects((loc, spellInfo) -> {
                    World world = spellInfo.world();
                    world.spigot().strikeLightningEffect(loc, true);
                    world.playSound(loc, Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 4, 1);
                    world.playSound(loc, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 5, 1);
                    world.spawnParticle(Particle.CLOUD, loc, 40, 0.2, 0.2, 0.2, 0.3, null, true);
                })
                .setSpellEffectRadius(3.5F)
                .setLifeTime(25)
                .setSpellRelativeEffects((loc, spellInfo) -> {
                    World world = spellInfo.world();
                    world.spawnParticle(Particle.CLOUD, loc, 10, 0.2, 0.2, 0.2, 0.1, null, true);
                    world.spawnParticle(Particle.ENCHANT, loc, 20, 0.5, 0.5, 0.5, 1, null, true);
                })
                .setEntityDamage(8)
                .setEntityEffects((entity, spellInfo) -> entity.setFireTicks(80))
                .setCastSound(Sound.ENTITY_WITHER_SHOOT)
                .build();
    }
}
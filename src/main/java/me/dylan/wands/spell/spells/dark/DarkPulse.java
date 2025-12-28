package me.dylan.wands.spell.spells.dark;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.accessories.KnockBack;
import me.dylan.wands.spell.spellbuilders.Behavior;
import me.dylan.wands.spell.spellbuilders.BuildableBehaviour.Target;
import me.dylan.wands.spell.spellbuilders.Ray;
import me.dylan.wands.spell.spells.AffinityType;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DarkPulse implements Castable {
    @Override
    public AffinityType[] getAffinityTypes() {
        return new AffinityType[]{AffinityType.DARK_MAGIC};
    }

    @Override
    public Behavior createBehaviour() {
        return Ray.newBuilder(Target.MULTI)
                .setRayWidth(1)
                .setSpellEffectRadius(3.0F)
                .setMetersPerTick(2)
                .setKnockBack(KnockBack.EXPLOSION)
                .setPotionEffects(
                        new PotionEffect(PotionEffectType.BLINDNESS, 40, 0, false),
                        new PotionEffect(PotionEffectType.SLOWNESS, 40, 2, false),
                        new PotionEffect(PotionEffectType.WITHER, 40, 2, false)
                )
                .setEntityDamage(8)
                .setCastSound(Sound.ENTITY_WITHER_SHOOT)
                .setSpellRelativeEffects((loc, spellInfo) -> {
                    World world = spellInfo.world();
                    world.spawnParticle(Particle.LARGE_SMOKE, loc, 3, 0.4, 0.4, 0.4, 0.1, null, true);
                    world.spawnParticle(Particle.SMOKE, loc, 13, 0.8, 0.8, 0.8, 0.1, null, true);
                    world.spawnParticle(Particle.ENCHANT, loc, 10, 0.8, 0.8, 0.8, 0.1, null, true);
                })
                .setEffectDistance(30)
                .setHitEffects((loc, spellInfo) -> {
                    World world = spellInfo.world();
                    world.spawnParticle(Particle.EXPLOSION_EMITTER, loc, 0, 0.0, 0.0, 0.0, 0.0, null, true);
                    world.createExplosion(loc, 0.0f);
                    world.playSound(loc, Sound.ENTITY_WITHER_HURT, SoundCategory.MASTER, 4.0F, 1.0F);
                })
                .build();
    }
}
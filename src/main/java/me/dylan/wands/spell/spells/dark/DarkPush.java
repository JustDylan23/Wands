package me.dylan.wands.spell.spells.dark;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.spellbuilders.Behavior;
import me.dylan.wands.spell.spellbuilders.BuildableBehaviour.KnockBackDirection;
import me.dylan.wands.spell.spellbuilders.BuildableBehaviour.Target;
import me.dylan.wands.spell.spellbuilders.Phase;
import me.dylan.wands.spell.spells.AffinityType;
import me.dylan.wands.spell.util.SpellEffectUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class DarkPush implements Castable {
    @Override
    public AffinityType[] getAffinityTypes() {
        return new AffinityType[]{AffinityType.DARK_MAGIC};
    }

    @Override
    public Behavior createBehaviour() {
        return Phase.newBuilder(Target.SINGLE)
                .setEntityDamage(6)
                .setCastSound(Sound.ENTITY_WITHER_SHOOT)
                .setKnockBack(1.2F, 0.6F)
                .setEffectDistance(30)
                .setSpellEffectRadius(2.5F)
                .knockBackFrom(KnockBackDirection.PLAYER)
                .setSpellRelativeEffects((loc, spellInfo) -> {
                    World world = spellInfo.world();
                    world.spawnParticle(Particle.LARGE_SMOKE, loc, 20, 0.4, 0.4, 0.4, 0.1, null, true);
                    world.spawnParticle(Particle.SMOKE, loc, 20, 0.4, 0.4, 0.4, 0.1, null, true);
                    world.spawnParticle(Particle.ENCHANT, loc, 40, 0.8, 0.8, 0.8, 0.3, null, true);
                })
                .setPotionEffects(new PotionEffect(PotionEffectType.BLINDNESS, 40, 0, false))
                .setStagePassCondition(Entity::isOnGround)
                .setEffectsDuringPhase(entity -> entity.getWorld().spawnParticle(Particle.LARGE_SMOKE, entity.getLocation(), 5, 0.3, 0.3, 0.3, 0.05, null, true))
                .setEffectsAfterPhase((entity, player) -> {
                    Location loc = entity.getLocation();
                    World world = loc.getWorld();
                    loc.getWorld().spawnParticle(Particle.LARGE_SMOKE, loc, 15, 2, 0.2, 2, 0.05, null, true);
                    world.createExplosion(loc, 0.0f);
                    for (LivingEntity loopEntity : SpellEffectUtil.getNearbyLivingEntities(player, loc, 3)) {
                        loopEntity.damage(3);
                        if (!loopEntity.equals(entity)) {
                            loopEntity.setVelocity(loopEntity.getLocation().subtract(loc).toVector().normalize().multiply(0.5F));
                            world.createExplosion(loc, 0.0f);
                        }
                    }
                })
                .build();
    }
}
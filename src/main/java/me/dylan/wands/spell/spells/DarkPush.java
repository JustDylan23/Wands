package me.dylan.wands.spell.spells;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.SpellEffectUtil;
import me.dylan.wands.spell.types.Behaviour;
import me.dylan.wands.spell.types.Behaviour.KnockBackFrom;
import me.dylan.wands.spell.types.Behaviour.Target;
import me.dylan.wands.spell.types.Phase;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public enum DarkPush implements Castable {
    INSTANCE;
    private final Behaviour behaviour;

    private final PotionEffect blind = new PotionEffect(PotionEffectType.BLINDNESS, 40, 0, false);

    DarkPush() {
        this.behaviour = Phase.newBuilder(Target.SINGLE)
                .setEntityDamage(6)
                .setCastSound(Sound.ENTITY_WITHER_SHOOT)
                .setKnockBack(1.2F, 0.6F)
                .setEffectDistance(30)
                .setSpellEffectRadius(2.5F)
                .knockBackFrom(KnockBackFrom.PLAYER)
                .setSpellRelativeEffects((loc, world) -> {
                    world.spawnParticle(Particle.SMOKE_LARGE, loc, 20, 0.4, 0.4, 0.4, 0.1, null, true);
                    world.spawnParticle(Particle.SMOKE_NORMAL, loc, 20, 0.4, 0.4, 0.4, 0.1, null, true);
                    world.spawnParticle(Particle.ENCHANTMENT_TABLE, loc, 40, 0.8, 0.8, 0.8, 0.3, null, true);
                })
                .setEntityEffects(entity -> entity.addPotionEffect(blind, true))
                .setStagePassCondition(Entity::isOnGround)
                .setEffectsDuringPhase(entity -> entity.getWorld().spawnParticle(Particle.SMOKE_LARGE, entity.getLocation(), 5, 0.3, 0.3, 0.3, 0.05, null, true))
                .setEffectsAfterPhase((entity, player) -> {
                    Location loc = entity.getLocation();
                    loc.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 15, 2, 0.2, 2, 0.05, null, true);
                    loc.createExplosion(0.0f);
                    for (LivingEntity loopEntity : SpellEffectUtil.getNearbyLivingEntities(player, loc, 3)) {
                        loopEntity.damage(3);
                        if (!loopEntity.equals(entity)) {
                            loopEntity.setVelocity(loopEntity.getLocation().subtract(loc).toVector().normalize().multiply(0.5F));
                            loc.createExplosion(0.0f);
                        }
                    }
                })
                .build();
    }

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }
}
package me.dylan.wands.spell.spells;

import me.dylan.wands.Main;
import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.types.Behaviour;
import me.dylan.wands.spell.types.Behaviour.Target;
import me.dylan.wands.spell.types.Ray;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public enum MephiGrabWave implements Castable {
    INSTANCE;
    private final Behaviour behaviour;
    private final PotionEffect slow = new PotionEffect(PotionEffectType.SLOW, 60, 2, false);
    private final PotionEffect blind = new PotionEffect(PotionEffectType.BLINDNESS, 60, 2, false);


    MephiGrabWave() {
        this.behaviour = Ray.newBuilder(Target.MULTI)
                .setCastSound(Sound.ENTITY_EVOKER_CAST_SPELL)
                .setRayWidth(2)
                .setEntityDamage(5)
                .setSpellEffectRadius(2)
                .setSpellRelativeEffects((location, world) -> {
                    world.spawnParticle(Particle.VILLAGER_HAPPY, location, 4, 0.4, 0.4, 0.4, 0.1, null, true);
                    world.spawnParticle(Particle.SMOKE_LARGE, location, 4, 0.3, 0.3, 0.3, 0.1, null, true);
                    world.spawnParticle(Particle.SMOKE_LARGE, location, 4, 1, 1, 1, 0.05, null, true);
                })
                .setHitEffects((location, world) -> {
                    world.spawnParticle(Particle.VILLAGER_HAPPY, location, 6, 3, 3, 3, 0.4, null, true);
                    world.spawnParticle(Particle.SMOKE_LARGE, location, 10, 0.3, 0.3, 0.3, 0.4, null, true);
                    world.playSound(location, Sound.ENTITY_ILLUSIONER_MIRROR_MOVE, SoundCategory.MASTER, 4, 1);
                })
                .setEffectDistance(30)
                .setEntityEffects(entity -> {
                    entity.addPotionEffect(slow, true);
                    entity.addPotionEffect(blind, true);
                })
                .extendedSetEntityEffects((livingEntity, SpellInfo) -> new BukkitRunnable() {
                    int i = 0;

                    @Override
                    public void run() {
                        Location entityLoc = livingEntity.getLocation();
                        double distance = entityLoc.distance(SpellInfo.origination);
                        if (++i == 30 || distance <= 6) {
                            cancel();
                        } else {
                            livingEntity.setVelocity(SpellInfo.origination.clone().subtract(entityLoc).toVector().normalize());
                        }
                    }
                }.runTaskTimer(Main.getPlugin(), 0, 1))
                .build();
    }

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }
}

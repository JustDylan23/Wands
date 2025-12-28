package me.dylan.wands.spell.spells.weather;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.accessories.SpellInfo;
import me.dylan.wands.spell.spellbuilders.Behavior;
import me.dylan.wands.spell.spellbuilders.BuildableBehaviour.Target;
import me.dylan.wands.spell.spellbuilders.Ray;
import me.dylan.wands.spell.spells.AffinityType;
import me.dylan.wands.utils.Common;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Freeze implements Castable {
    @Override
    public AffinityType[] getAffinityTypes() {
        return new AffinityType[]{AffinityType.WEATHER_MAGIC};
    }

    @Override
    public Behavior createBehaviour() {
        return Ray.newBuilder(Target.SINGLE)
                .setRayWidth(1)
                .setEntityDamage(6)
                .setMetersPerTick(2)
                .setCastSound(Sound.ENTITY_LLAMA_SWAG)
                .setSpellRelativeEffects((loc, spellInfo) -> {
                    World world = spellInfo.world();
                    world.spawnParticle(Particle.POOF, loc, 8, 0.1, 0.1, 0.1, 0.02, null, true);
                    world.spawnParticle(Particle.ENCHANT, loc, 10, 0.5, 0.5, 0.5, 1, null, true);
                })
                .setPotionEffects(new PotionEffect(PotionEffectType.SLOWNESS, 120, 4, false))
                .setEffectDistance(25)
                .setEntityEffects(this::freeze)
                .build();
    }

    private void freeze(LivingEntity entity, SpellInfo ignored) {
        Location loc = entity.getLocation();
        World world = loc.getWorld();
        world.playSound(loc, Sound.ENTITY_EVOKER_FANGS_ATTACK, 4, 2);
        if (entity.hasPotionEffect(PotionEffectType.SLOWNESS)) return;
        BukkitRunnable bukkitRunnable = new BukkitRunnable() {
            int count = 0;

            @Override
            public void run() {
                if (++count > 60 || !entity.isValid()) {
                    cancel();
                } else {
                    Location loc = entity.getLocation().add(0, 3.5, 0);
                    world.spawnParticle(Particle.ITEM_SNOWBALL, loc, 6, 0.5, 0.5, 0.5, 0.01, null, true);
                    world.spawnParticle(Particle.CLOUD, loc.add(0, 1, 0), 6, 0.5, 0.3, 0.5, 0, null, true);
                }
            }
        };
        Common.runTaskTimer(bukkitRunnable, 2, 2);
    }
}
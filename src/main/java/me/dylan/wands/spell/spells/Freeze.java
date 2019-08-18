package me.dylan.wands.spell.spells;

import me.dylan.wands.Main;
import me.dylan.wands.spell.SpellData;
import me.dylan.wands.spell.types.Behaviour;
import me.dylan.wands.spell.types.Behaviour.Target;
import me.dylan.wands.spell.types.Ray;
import me.dylan.wands.util.Common;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public class Freeze implements SpellData {
    private final String metaKey = "FREEZE_SPELL";
    private final Behaviour behaviour;
    private final PotionEffect slow = new PotionEffect(PotionEffectType.SLOW, 120, 4, false);


    public Freeze() {
        this.behaviour = Ray.newBuilder(Target.SINGLE)
                .setRayWidth(1)
                .setEntityDamage(6)
                .setMetersPerTick(2)
                .setCastSound(Sound.ENTITY_LLAMA_SWAG)
                .setSpellRelativeEffects((loc, world) -> {
                    world.spawnParticle(Particle.EXPLOSION_NORMAL, loc, 8, 0.1, 0.1, 0.1, 0.02, null, true);
                    world.spawnParticle(Particle.ENCHANTMENT_TABLE, loc, 10, 0.5, 0.5, 0.5, 1, null, true);
                })
                .setEffectDistance(25)
                .setEntityEffects(this::freeze)
                .build();
    }

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }

    private void freeze(LivingEntity entity) {
        Location loc = entity.getLocation();
        World world = loc.getWorld();
        world.playSound(loc, Sound.ENTITY_EVOKER_FANGS_ATTACK, 4, 2);
        if (entity.hasMetadata(metaKey)) return;
        entity.setMetadata(metaKey, Common.METADATA_VALUE_TRUE);
        entity.addPotionEffect(slow, true);
        new BukkitRunnable() {
            int count = 0;

            @Override
            public void run() {
                if (++count > 60 || !entity.isValid()) {
                    cancel();
                    entity.removeMetadata(metaKey, Main.getPlugin());
                } else {
                    Location loc = entity.getLocation().add(0, 3.5, 0);
                    world.spawnParticle(Particle.SNOW_SHOVEL, loc, 6, 0.5, 0.5, 0.5, 0.01, null, true);
                    world.spawnParticle(Particle.CLOUD, loc.add(0, 1, 0), 6, 0.5, 0.3, 0.5, 0, null, true);
                }
            }
        }.runTaskTimer(Main.getPlugin(), 2, 2);
    }
}
package me.dylan.wands.spell.spells;

import me.dylan.wands.Main;
import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.SpellEffectUtil;
import me.dylan.wands.spell.types.Spark;
import me.dylan.wands.spell.types.Base;
import me.dylan.wands.util.Common;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Wolf;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.Set;

public enum CorruptedWolves implements Castable {
    INSTANCE;

    private final Base baseType;
    private final Main plugin = Main.getPlugin();
    private final Set<Wolf> wolves = new HashSet<>();
    private final PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, 150, 1, true);

    CorruptedWolves() {
        plugin.addDisableLogic(() -> wolves.forEach(Entity::remove));

        this.baseType = Spark.newBuilder(Base.Target.SINGLE)
                .setSpellEffectRadius(2.0F)
                .setCastSound(Sound.ENTITY_EVOKER_PREPARE_SUMMON)
                .setEffectDistance(30)
                .setEntityEffects(this::accept)
                .build();
    }

    @Override
    public Base getBaseType() {
        return baseType;
    }

    private void accept(LivingEntity target) {
        Location loc = target.getLocation();
        World world = loc.getWorld();
        for (int i = 0; i < 4; i++) {
            Wolf wolf = (Wolf) world.spawnEntity(SpellEffectUtil.getFirstPassableBlockAbove(SpellEffectUtil.randomizeLoc(loc, 2, 0, 2)), EntityType.WOLF);
            wolf.setMetadata(SpellEffectUtil.UNTARGETABLE, Common.METADATA_VALUE_TRUE);
            Location location = wolf.getLocation();
            world.playSound(location, Sound.BLOCK_CHORUS_FLOWER_GROW, SoundCategory.MASTER, 4, 1);
            world.spawnParticle(Particle.SMOKE_LARGE, location, 2, 0.1, 0.1, 0.05, 0.1, null, true);
            wolf.addPotionEffect(speed, true);
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (wolf.isValid()) {
                        world.spawnParticle(Particle.SMOKE_NORMAL, wolf.getLocation(), 1, 0.1, 0.1, 0.1, 0.05, null, true);
                    } else {
                        cancel();
                        wolves.remove(wolf);
                        world.spawnParticle(Particle.SMOKE_LARGE, wolf.getLocation(), 3, 0, 0, 0, 0.1, null, true);
                    }
                }
            }.runTaskTimer(plugin, 1, 1);
            Bukkit.getScheduler().runTaskLater(plugin, () -> wolf.damage(0, target), 10);
            Bukkit.getScheduler().runTaskLater(plugin, wolf::remove, 150);
        }
    }
}

package me.dylan.wands.spell.implementations;

import me.dylan.wands.Main;
import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.SpellEffectUtil;
import me.dylan.wands.spell.handler.Behaviour;
import me.dylan.wands.spell.handler.SparkSpell;
import me.dylan.wands.spell.handler.SparkSpell.Target;
import me.dylan.wands.util.Common;
import org.bukkit.*;
import org.bukkit.entity.Damageable;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

public enum CorruptedWolves implements Castable {
    INSTANCE;

    private final Behaviour behaviour;
    private final Main plugin = Main.getPlugin();

    CorruptedWolves() {
        this.behaviour = SparkSpell.newBuilder(Target.SINGLE)
                .requireLivingTarget(true)
                .setCastSound(Sound.ENTITY_EVOKER_PREPARE_SUMMON)
                .setEffectDistance(30)
                .setEntityEffects(entity -> {
                    Location loc = entity.getLocation();
                    World world = loc.getWorld();
                    Damageable[] wolves = new Damageable[4];
                    for (int i = 0; i < wolves.length; i++) {
                        Damageable wolf = (Damageable) world.spawnEntity(SpellEffectUtil.getFirstPassableBlockAbove(SpellEffectUtil.randomizeLoc(loc, 2, 0, 2)), EntityType.WOLF);
                        wolf.setMetadata(SpellEffectUtil.UNTARGETABLE, Common.METADATA_VALUE_TRUE);
                        Location location = wolf.getLocation();
                        world.playSound(location, Sound.BLOCK_CHORUS_FLOWER_GROW, SoundCategory.MASTER, 4, 1);
                        world.spawnParticle(Particle.SMOKE_LARGE, location, 2, 0.1, 0.1, 0.05, 0.1, null, true);
                        wolf.setVelocity(wolf.getVelocity().setY(0.4));
                        wolves[i] = wolf;
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (wolf.isValid()) {
                                    world.spawnParticle(Particle.SMOKE_NORMAL, wolf.getLocation(), 1, 0.1, 0.1, 0.1, 0.05, null, true);
                                } else {
                                    cancel();
                                }
                            }
                        }.runTaskTimer(plugin, 0, 1);
                    }
                    Runnable runnable = () -> {
                        for (Damageable wolf : wolves) {
                            wolf.remove();
                        }
                    };
                    plugin.addDisableLogic(runnable);
                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                        for (Damageable wolf : wolves) wolf.damage(0, entity);
                    }, 15);
                    Bukkit.getScheduler().runTaskLater(plugin, () -> Arrays.stream(wolves).forEach(wolf -> {
                        if (wolf.isValid()) {
                            wolf.remove();
                            plugin.removeDisableLogic(runnable);
                            world.spawnParticle(Particle.SMOKE_LARGE, wolf.getLocation(), 3, 0, 0, 0, 0.1, null, true);
                        }
                    }), 100);
                })
                .build();
    }

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }
}

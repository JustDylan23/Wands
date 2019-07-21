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
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

public enum CorruptedWolfs implements Castable {
    INSTANCE;

    private final Behaviour behaviour;

    CorruptedWolfs() {
        this.behaviour = SparkSpell.newBuilder(Target.SINGLE)
                .requireLivingTarget(true)
                .setCastSound(Sound.ENTITY_EVOKER_PREPARE_SUMMON)
                .setEffectDistance(30)
                .setEntityEffects(entity -> {
                    Location loc = entity.getLocation();
                    World world = loc.getWorld();
                    Damageable[] wolfs = new Damageable[4];
                    new BukkitRunnable() {
                        int count = 0;
                        @Override
                        public void run() {
                            if (count >= 4) {
                                cancel();
                                Bukkit.getScheduler().runTaskLater(Main.getPlugin(),() -> Arrays.stream(wolfs).forEach(entity -> {
                                    if (entity.isValid()) {
                                        entity.remove();
                                        world.spawnParticle(Particle.SMOKE_LARGE, entity.getLocation(), 3, 0, 0, 0, 0.1, null, true);
                                    }
                                }) ,100);
                                for (Entity wolf : wolfs) {
                                    ((Damageable) wolf).damage(0, entity);
                                }
                                return;
                            } else {
                                Damageable wolf = (Damageable) world.spawnEntity(SpellEffectUtil.getFirstPassableBlockAbove(SpellEffectUtil.randomizeLoc(loc, 2, 0, 2)), EntityType.WOLF);
                                wolf.setMetadata(SpellEffectUtil.UNTARGETABLE, Common.METADATA_VALUE_TRUE);
                                Location location = wolf.getLocation();
                                world.playSound(location, Sound.BLOCK_CHORUS_FLOWER_GROW, SoundCategory.MASTER, 4, 1);
                                world.spawnParticle(Particle.SMOKE_LARGE, location, 2, 0.1, 0.1, 0.05, 0.1, null, true);
                                wolf.setVelocity(wolf.getVelocity().setY(0.4));
                                wolfs[count] = wolf;
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        if (wolf.isValid()) {
                                            world.spawnParticle(Particle.SMOKE_NORMAL, wolf.getLocation(), 1, 0.1, 0.1, 0.1, 0.05, null, true);
                                        } else {
                                            cancel();
                                        }
                                    }
                                }.runTaskTimer(Main.getPlugin(), 0, 1);
                            }
                            count++;
                        }
                    }.runTaskTimer(Main.getPlugin(), 0, 4);
                })
                .build();
    }

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }
}

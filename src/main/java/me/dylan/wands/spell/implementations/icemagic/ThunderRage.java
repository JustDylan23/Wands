package me.dylan.wands.spell.implementations.icemagic;

import me.dylan.wands.Main;
import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.handler.Behaviour;
import me.dylan.wands.spell.handler.BurstAuraSpell;
import me.dylan.wands.spell.handler.SparkSpell;
import me.dylan.wands.util.EffectUtil;
import org.bukkit.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

public enum ThunderRage implements Castable {
    INSTANCE;
    private final Behaviour behaviour;
    private final PotionEffect wither = new PotionEffect(PotionEffectType.WITHER, 80, 1, false);
    private final PotionEffect slow = new PotionEffect(PotionEffectType.SLOW, 80, 3, false);

    ThunderRage() {
        this.behaviour = BurstAuraSpell.newBuilder()
                .setCastSound(Sound.ITEM_TOTEM_USE)
                .setAffectedEntityDamage(4)
                .setSpellEffectRadius(8)
                .setPlayerEffects(player -> {
                    new BukkitRunnable() {
                        World world = player.getWorld();
                        int count = 0;

                        @Override
                        public void run() {
                            if (++count >= 10) cancel();
                            Location loc = player.getLocation();
                            world.spawnParticle(Particle.CLOUD, loc, 6, 0.2, 0.2, 0.2, 0.1, null, true);
                            world.spawnParticle(Particle.SMOKE_LARGE, loc, 2, 0.2, 0.2, 0.2, 0.1, null, true);
                            world.spawnParticle(Particle.SMOKE_NORMAL, loc, 2, 0.2, 0.2, 0.2, 0.1, null, true);
                            world.spawnParticle(Particle.FLAME, loc, 2, 0.2, 0.2, 0.2, 0.1, null, true);
                        }
                    }.runTaskTimer(Main.getPlugin(), 0, 2);
                })
                .setAffectedEntityEffects(entity -> {
                    entity.addPotionEffect(wither, true);
                    entity.addPotionEffect(slow, true);
                    entity.setFireTicks(60);
                    World world = entity.getWorld();
                    Location loc = entity.getLocation();
                    world.spawnParticle(Particle.FLAME, loc, 10, 0.2, 0.2, 0.2, 0.1, null, true);
                    world.spawnParticle(Particle.CLOUD, loc, 10, 0.2, 0.2, 0.2, 0.1, null, true);
                    world.strikeLightningEffect(loc);
                    EffectUtil.runTaskLater(() -> {
                        world.strikeLightningEffect(loc);
                    }, 3, 3, 3);
                })
                .build();
    }

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }
}
package me.dylan.wands.spell.spells;

import me.dylan.wands.spell.SpellData;
import me.dylan.wands.spell.tools.KnockBack;
import me.dylan.wands.spell.types.Behavior;
import me.dylan.wands.spell.util.SpellEffectUtil;
import org.bukkit.*;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class SpiritFury extends Behavior implements SpellData {
    private static final DustOptions RED = new DustOptions(Color.RED, 1);
    private static final DustOptions BLACK = new DustOptions(Color.BLACK, 1);
    private static final KnockBack knockBack = KnockBack.from(0.4f, 0.2f);

    @Override
    public Behavior getBehavior() {
        return this;
    }

    @Override
    public boolean cast(@NotNull Player player, @NotNull String weaponName) {
        Location location = player.getEyeLocation();
        World world = location.getWorld();
        world.playSound(location, Sound.ENTITY_BLAZE_SHOOT, 4, 2);
        Location[] points = SpellEffectUtil.getCircleFromPlayerView(location, 0.5, 18, 0);
        Vector incrementFull = player.getLocation().getDirection().normalize();
        Vector increment = incrementFull.clone().multiply(0.2);

        new BukkitRunnable() {
            int count;

            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    if (++count > 120) {
                        cancel(world, location);
                        return;
                    } else {
                        if (!location.getBlock().isPassable()) {
                            cancel(world, location);
                            return;
                        }
                        if (count % 5 == 0) {
                            for (Location point : points) {
                                world.spawnParticle(Particle.REDSTONE, point.add(incrementFull), 1, 0, 0, 0, 0, BLACK, false);
                            }
                        }
                        location.add(increment);
                        world.spawnParticle(Particle.REDSTONE, location, 1, 0, 0, 0, 0, RED, false);

                        for (LivingEntity livingEntity : SpellEffectUtil.getNearbyLivingEntities(player, location, 0.7)) {
                            SpellEffectUtil.damageEffect(player, livingEntity, 5, weaponName);
                            knockBack.apply(livingEntity, location);
                            cancel(world, location);
                            break;
                        }
                    }
                }
            }

            void cancel(World world, Location location) {
                cancel();
                world.playSound(location, Sound.ENTITY_ENDER_DRAGON_HURT, 4, 0);
            }
        }.runTaskTimer(plugin, 0, 1);
        return true;
    }
}

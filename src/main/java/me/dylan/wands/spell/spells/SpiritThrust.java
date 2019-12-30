package me.dylan.wands.spell.spells;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.accessories.KnockBack;
import me.dylan.wands.spell.spellbuilders.Behavior;
import me.dylan.wands.spell.util.SpellEffectUtil;
import me.dylan.wands.utils.Common;
import org.bukkit.*;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class SpiritThrust extends Behavior implements Castable {
    private static final DustOptions BLACK = new DustOptions(Color.BLACK, 1);
    private static final DustOptions RED = new DustOptions(Color.RED, 1);
    private static final KnockBack knockBack = KnockBack.from(0.7f, 0.2f);

    @Override
    public Behavior createBehaviour() {
        return this;
    }

    @Override
    public boolean cast(@NotNull Player player, @NotNull String weapon) {
        Location location = player.getEyeLocation();
        Location origin = location.clone();
        World world = location.getWorld();
        world.playSound(location, Sound.ENTITY_BLAZE_SHOOT, 4, 2);
        Location[] points = SpellEffectUtil.getCircleFromPlayerView(location, 0.5, 18, 0);
        Vector increment = player.getLocation().getDirection().normalize().multiply(0.2);
        Vector incrementDistance = new Vector(0, 0, 0);
        BukkitRunnable bukkitRunnable = new BukkitRunnable() {
            int count = 0;
            int length = 0;

            @Override
            public void run() {
                for (int i = 0; i < 3; i++) {
                    if (length >= 5) {
                        cancel();
                        return;
                    } else {
                        if (count >= 6) {
                            count = 0;
                            length++;
                        }
                        for (int j = 0; j < 3; j++) {
                            world.spawnParticle(Particle.REDSTONE, points[6 * j + count].clone().add(incrementDistance), 1, 0, 0, 0, 0, BLACK, false);
                        }

                        world.spawnParticle(Particle.REDSTONE, location, 1, 0.1, 0.1, 0.1, 0, RED, false);

                        for (LivingEntity livingEntity : SpellEffectUtil.getNearbyLivingEntities(player, location.add(increment), 0.7)) {
                            SpellEffectUtil.damageEffect(player, livingEntity, 6, weapon);
                            knockBack.apply(livingEntity, origin);
                            cancel();
                            break;
                        }

                        incrementDistance.add(increment);
                        count++;
                    }
                }
            }
        };
        Common.runTaskTimer(bukkitRunnable, 0, 1);
        return true;
    }
}

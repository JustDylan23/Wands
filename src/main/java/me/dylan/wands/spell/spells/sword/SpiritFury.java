package me.dylan.wands.spell.spells.sword;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.accessories.KnockBack;
import me.dylan.wands.spell.spellbuilders.Behavior;
import me.dylan.wands.spell.spells.AffinityType;
import me.dylan.wands.spell.util.SpellEffectUtil;
import me.dylan.wands.utils.Common;
import org.bukkit.*;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class SpiritFury extends Behavior implements Castable {
    private static final DustOptions RED = new DustOptions(Color.RED, 1);
    private static final DustOptions BLACK = new DustOptions(Color.BLACK, 1);
    private static final KnockBack knockBack = KnockBack.from(0.4f, 0.2f);

    @Override
    public CastType getCastType() {
        return CastType.SWORD_SKILL;
    }

    @Override
    public AffinityType[] getAffinityTypes() {
        return new AffinityType[]{AffinityType.SWORD_ARTS};
    }

    @Override
    public Behavior createBehaviour() {
        return this;
    }

    @Override
    public boolean cast(@NotNull Player player, @NotNull String weapon) {
        BukkitRunnable bukkitRunnable1 = new BukkitRunnable() {
            int count = 0;

            @Override
            public void run() {
                count++;
                if (count >= 5) {
                    cancel();
                }
                Location location = SpellEffectUtil.randomizeLoc(player.getEyeLocation(), 1.5, 1.5, 1.5);
                Location knockBackLoc = location.clone();
                World world = location.getWorld();
                world.playSound(location, Sound.ENTITY_BLAZE_SHOOT, 4, 2);
                Location[] points = SpellEffectUtil.getCircleFromPlayerView(location, 0.5, 18, 0);
                Vector incrementFull = player.getLocation().getDirection().normalize();
                Vector increment = incrementFull.clone().multiply(0.2);

                BukkitRunnable bukkitRunnable = new BukkitRunnable() {
                    int count = 0;

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
                                        world.spawnParticle(Particle.DUST, point.add(incrementFull), 1, 0, 0, 0, 0, BLACK, false);
                                    }
                                }
                                location.add(increment);
                                world.spawnParticle(Particle.DUST, location, 1, 0, 0, 0, 0, RED, false);

                                for (LivingEntity livingEntity : SpellEffectUtil.getNearbyLivingEntities(player, location, 0.7)) {
                                    SpellEffectUtil.damageEffect(player, livingEntity, 6, weapon);
                                    knockBack.apply(livingEntity, knockBackLoc);
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
                };
                Common.runTaskTimer(bukkitRunnable, 0, 1);
            }
        };
        Common.runTaskTimer(bukkitRunnable1, 0, 2);
        return true;
    }
}

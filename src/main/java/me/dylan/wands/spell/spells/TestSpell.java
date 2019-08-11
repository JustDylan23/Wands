package me.dylan.wands.spell.spells;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.SpellEffectUtil;
import me.dylan.wands.spell.types.Behaviour;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class TestSpell extends Behaviour implements Castable {
    public static final TestSpell INSTANCE = new TestSpell();

    @SuppressWarnings("deprecation")
    private TestSpell() {
    }

    @Override
    public boolean cast(@NotNull Player player, @NotNull String weaponName) {
        Location loc = player.getEyeLocation();
        Location[] circlePoints = SpellEffectUtil.getCircleFromPlayerView(loc, 0.8, 20, 1);
        World world = loc.getWorld();
        Vector direction = loc.getDirection().normalize();
        loc.add(direction);
        direction.multiply(2);
        double offSet = Math.toRadians(10);
        new BukkitRunnable() {
            int i = 0;
            @Override
            public void run() {
                if (i >= 20) {
                    cancel();
                } else {
                    Location circlePoint = circlePoints[i];
                    world.spawnParticle(Particle.FLAME, circlePoint, 1, 0, 0, 0, 0, null, true);
                    int i2 = i + 1;
                    if (i2 % 2 == 0) {
                        Bukkit.getScheduler().runTaskLater(plugin, () -> {
                            //implement
                            circlePoint.subtract(loc).multiply(0.5).add(loc);
                            Arrow smallFireball = (Arrow) world.spawnEntity(loc, EntityType.ARROW);
                            smallFireball.setVelocity(newRandomVector(direction, offSet));
                            smallFireball.setFireTicks(100);
                            smallFireball.setShooter(player);
                            Bukkit.getScheduler().runTaskLater(plugin, smallFireball::remove, 20);
                        }, 10 - i2 / 2);
                    }
                }
                i++;
            }
        }.runTaskTimer(plugin, 0, 1);
        return false;
    }

    private Vector newRandomVector(Vector vector, double radians) {
        Vector vec = vector.clone();
        rotateX(vec, SpellEffectUtil.randomize(radians));
        rotateY(vec, SpellEffectUtil.randomize(radians));
        return vec;
    }

    private void rotateX(Vector point, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double y = point.getY();
        double z = point.getZ();
        point.setY(y * cos - z * sin);
        point.setZ(y * sin + z * cos);
    }

    private void rotateY(Vector point, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double z = point.getZ();
        double x = point.getX();
        point.setZ(z * cos - x * sin);
        point.setX(z * sin + x * cos);
    }

    @Override
    public Behaviour getBehaviour() {
        return this;
    }
}

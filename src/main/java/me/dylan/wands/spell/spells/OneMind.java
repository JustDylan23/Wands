package me.dylan.wands.spell.spells;

import me.dylan.wands.knockback.KnockBack;
import me.dylan.wands.spell.SpellData;
import me.dylan.wands.spell.SpellEffectUtil;
import me.dylan.wands.spell.types.Behaviour;
import me.dylan.wands.util.Common;
import org.bukkit.*;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class OneMind extends Behaviour implements SpellData {
    private KnockBack knockBack = KnockBack.from(0.3f);

    @Override
    public Behaviour getBehaviour() {
        return this;
    }

    @Override
    public boolean cast(@NotNull Player player, @NotNull String weaponName) {
        new BukkitRunnable() {
            int count = 0;

            @Override
            public void run() {
                if (++count > 3) {
                    cancel();
                } else {
                    int degrees = 0;
                    switch (count) {
                        case 1:
                            degrees = 180;
                            break;
                        case 2:
                            degrees = 75;
                            break;
                        case 3:
                            degrees = 285;
                    }
                    Location location = player.getLocation();
                    draw(player, weaponName, degrees, 3, entity -> {
                        entity.damage(3);
                        knockBack.apply(entity, location);
                    }, 0, false);
                }
            }
        }.runTaskTimer(plugin, 0, 10);
        return true;
    }

    static void draw(Player player, String weaponName, double degrees, double radius, Consumer<LivingEntity> entityEffects, int rotation, boolean fullCircle) {
        double deg = degrees - 90;
        Location location = player.getEyeLocation();
        World world = location.getWorld();

        DustOptions red = new DustOptions(Color.fromRGB(255, 0, 0), 1);
        DustOptions black = new DustOptions(Color.fromRGB(0, 0, 0), 1);

        int points = (int) (12 * radius);
        int times = points / 8 * (fullCircle ? 2 : 1);
        int delayTimes = 8 + (fullCircle ? 8 : 0);

        double xzRotation = Math.toRadians(90 + location.getYaw());
        double rotXZSin = Math.sin(xzRotation);
        double rotXZCos = Math.cos(xzRotation);

        double yRotation = Math.toRadians(270 + location.getPitch());
        double rotYSin = Math.sin(yRotation);
        double rotYCos = Math.cos(yRotation);

        Vector direction = location.getDirection();

        double angleIncrement = (2 * Math.PI) / points / 2;
        new BukkitRunnable() {
            double angle = Math.toRadians(90 + rotation);
            int count = 0;
            boolean first = false;
            String uuid = UUID.randomUUID().toString();

            @Override
            public void run() {
                if (++count > delayTimes) {
                    cancel();
                } else {
                    for (int i = 0; i < times; ++i) {
                        double sin = Math.sin(angle);
                        double cos = Math.cos(angle);

                        double x = radius * sin;
                        double y = radius * cos;

                        double yRotatedY = y * rotYCos;
                        double yRotatedZ = y * rotYSin;

                        double xyzRotatedX = yRotatedZ * rotXZCos - x * rotXZSin;
                        double xyzRotatedZ = yRotatedZ * rotXZSin + x * rotXZCos;

                        Vector vector = rotate(new Vector(xyzRotatedX, yRotatedY, xyzRotatedZ), direction, deg);

                        Location dustLoc = location.clone().add(vector);

                        Location dustSpread = dustLoc.clone().subtract(location).multiply(0.1);

                        for (int j = 0; j < 10; j++) {
                            if (j == 3) {
                                if (!first) {
                                    first = true;
                                    world.playSound(dustLoc, Sound.ENTITY_WITHER_SHOOT, 2, 2);
                                }
                                world.spawnParticle(Particle.REDSTONE, dustLoc.add(dustSpread), 2, 0, 0, 0, 0, red, true);
                            } else {
                                world.spawnParticle(Particle.REDSTONE, dustLoc.add(dustSpread), 1, 0.05, 0.05, 0.05, 0, black, true);
                                if (j == 5 && count % 4 == 0) {
                                    List<LivingEntity> effected = SpellEffectUtil.getNearbyLivingEntities(player, dustLoc, entity -> !entity.hasMetadata(uuid), 1.5);
                                    for (LivingEntity entity : effected) {
                                        entityEffects.accept(entity);
                                        entity.setMetadata(uuid, Common.METADATA_VALUE_TRUE);
                                    }
                                    Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                        effected.forEach(entity -> entity.removeMetadata(uuid, plugin));
                                    }, 10);
                                }
                            }
                        }
                        angle += angleIncrement;
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    private static Vector rotate(Vector vector, Vector axis, double degrees) {
        degrees *= Math.PI / 180d;
        double u = axis.getX();
        double v = axis.getY();
        double w = axis.getZ();
        double cos = Math.cos(degrees);
        double sin = Math.sin(degrees);
        double x = vector.getX();
        double y = vector.getY();
        double z = vector.getZ();
        double v1 = (u * x + v * y + w * z) * (1 - cos);
        double xPrime = u * v1 + x * cos + (-w * y + v * z) * sin;
        double yPrime = v * v1 + y * cos + (w * x - u * z) * sin;
        double zPrime = w * v1 + z * cos + (-v * x + u * y) * sin;
        vector.setX(xPrime);
        vector.setY(yPrime);
        vector.setZ(zPrime);
        return vector;
    }
}

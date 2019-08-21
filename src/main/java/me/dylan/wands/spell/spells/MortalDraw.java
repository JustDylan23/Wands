package me.dylan.wands.spell.spells;

import me.dylan.wands.Main;
import me.dylan.wands.spell.SpellEffectUtil;
import me.dylan.wands.util.Common;
import org.bukkit.*;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class MortalDraw {
    private static final Main plugin = Main.getPlugin();
    private static final DustOptions RED = new DustOptions(Color.fromRGB(255, 0, 0), 1);
    private static final DustOptions BLACK = new DustOptions(Color.fromRGB(0, 0, 0), 1);

    private MortalDraw() {
        throw new UnsupportedOperationException();
    }

    static void draw(Player player, double degrees, double radius, Consumer<LivingEntity> entityEffects, int rotation, boolean fullCircle) {
        double deg = degrees - 90;
        Location location = player.getEyeLocation();
        World world = location.getWorld();

        double xzRotation = Math.toRadians(90 + location.getYaw());
        double rotXZSin = Math.sin(xzRotation);
        double rotXZCos = Math.cos(xzRotation);

        double yRotation = Math.toRadians(270 + location.getPitch());
        double rotYSin = Math.sin(yRotation);
        double rotYCos = Math.cos(yRotation);

        Vector direction = location.getDirection();

        new BukkitRunnable() {
            double angle = Math.toRadians(90 + rotation);
            int pointsToDisplay = (int) Math.floor(12 * radius) * (fullCircle ? 2 : 1);
            double angleIncrement = (2 * Math.PI) / pointsToDisplay / (fullCircle ? 1 : 2);
            boolean first = false;
            String uuid = UUID.randomUUID().toString();

            @Override
            public void run() {
                for (int i = 0; i < 8; ++i) {
                    pointsToDisplay--;
                    if (pointsToDisplay < 0) {
                        cancel();
                        return;
                    }
                    double sin = Math.sin(angle);
                    double cos = Math.cos(angle);

                    double x = radius * sin;
                    double y = radius * cos;

                    double yRotatedY = y * rotYCos;
                    double yRotatedZ = y * rotYSin;

                    double xyzRotatedX = yRotatedZ * rotXZCos - x * rotXZSin;
                    double xyzRotatedZ = yRotatedZ * rotXZSin + x * rotXZCos;

                    Vector vector = new Vector(xyzRotatedX, yRotatedY, xyzRotatedZ)
                            .rotateAroundAxis(direction, Math.toRadians(deg));

                    Location dustLoc = location.clone().add(vector);
                    Location dustSpread = dustLoc.clone().subtract(location).multiply(0.1);

                    for (int j = 0; j < 10; j++) {
                        if (j == 3) {
                            if (!first) {
                                first = true;
                                world.playSound(dustLoc, Sound.ENTITY_WITHER_SHOOT, 2, 2);
                            }
                            world.spawnParticle(Particle.REDSTONE, dustLoc.add(dustSpread), 2, 0, 0, 0, 0, RED, true);
                        } else {
                            world.spawnParticle(Particle.REDSTONE, dustLoc.add(dustSpread), 1, 0.05, 0.05, 0.05, 0, BLACK, true);
                            if (j == 5) {
                                List<LivingEntity> effected = SpellEffectUtil.getNearbyLivingEntities(player, dustLoc, entity -> !entity.hasMetadata(uuid), 1.5);
                                for (LivingEntity entity : effected) {
                                    entityEffects.accept(entity);
                                    entity.setMetadata(uuid, Common.METADATA_VALUE_TRUE);
                                }
                                if (!effected.isEmpty()) {
                                    Bukkit.getScheduler().runTaskLater(plugin, () ->
                                            effected.forEach(entity -> entity.removeMetadata(uuid, plugin)), 10);
                                }
                            }
                        }
                    }
                    angle += angleIncrement;
                }
            }
        }.runTaskTimer(plugin, 0, 1);
    }
}

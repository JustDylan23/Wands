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
        Location location = player.getEyeLocation();
        World world = location.getWorld();

        double rotX = Math.toRadians(degrees);
        double rotY = Math.toRadians(-location.getPitch());
        double rotZ = Math.toRadians(270 - location.getYaw());

        world.playSound(location.clone().add(newVector(rotation, radius, rotX, rotY, rotZ)),
                Sound.ENTITY_WITHER_SHOOT, 2, 2);

        new BukkitRunnable() {
            double angle = Math.toRadians(rotation);
            int pointsToDisplay = (int) Math.floor(20 * radius) * (fullCircle ? 2 : 1);
            double angleIncrement = (2 * Math.PI) / pointsToDisplay / (fullCircle ? 1 : 2);
            String uuid = UUID.randomUUID().toString();

            @Override
            public void run() {
                for (int i = 0; i < 10; ++i) {
                    if (pointsToDisplay-- < 0) {
                        cancel();
                        return;
                    }

                    Vector vector = newVector(angle, radius, rotX, rotY, rotZ);

                    Location dustLoc = location.clone().add(vector);
                    vector.normalize().multiply(0.2);

                    for (int j = 0; j < 6; j++) {
                        dustLoc.add(vector);
                        if (j == 2) {
                            world.spawnParticle(Particle.REDSTONE, dustLoc, 2, 0, 0, 0, 0, RED, true);
                        }
                        world.spawnParticle(Particle.REDSTONE, dustLoc.add(vector), 1, 0.1, 0.1, 0.1, 0, BLACK, true);
                        if (j == 2) {
                            List<LivingEntity> effected = SpellEffectUtil.getNearbyLivingEntities(player, dustLoc, entity -> !entity.hasMetadata(uuid), 0.6);
                            if (!effected.isEmpty()) {
                                for (LivingEntity entity : effected) {
                                    entityEffects.accept(entity);
                                    entity.setMetadata(uuid, Common.METADATA_VALUE_TRUE);
                                }
                                Bukkit.getScheduler().runTaskLater(plugin, () ->
                                        effected.forEach(entity -> entity.removeMetadata(uuid, plugin)), 10);
                            }
                        }
                    }
                    angle += angleIncrement;
                }
            }
        }.runTaskTimer(plugin, 0, 1);
    }

    private static Vector newVector(double angle, double radius, double rotX, double rotY, double rotZ) {
        double x = radius * Math.sin(angle);
        double y = radius * Math.cos(angle);

        return new Vector(x, y, 0)
                .rotateAroundX(rotX)
                .rotateAroundZ(rotY)
                .rotateAroundY(rotZ);
    }
}

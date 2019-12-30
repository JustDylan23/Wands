package me.dylan.wands.spell.spells;

import me.dylan.wands.spell.accessories.KnockBack;
import me.dylan.wands.spell.util.SpellEffectUtil;
import me.dylan.wands.utils.Common;
import me.dylan.wands.utils.ItemUtil;
import org.bukkit.*;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

final class MortalDraw {
    private static final KnockBack knockBack = KnockBack.from(0.3f, 0.2f);
    private static final DustOptions RED = new DustOptions(Color.RED, 1);
    private static final DustOptions BLACK = new DustOptions(Color.BLACK, 1);

    private MortalDraw() {
        throw new UnsupportedOperationException("");
    }

    static void draw(@NotNull Player player, double degrees, double radius, int damage, int rotation, boolean fullCircle) {
        String weaponName = ItemUtil.getName(player.getInventory().getItemInMainHand());
        Location location = player.getEyeLocation();
        World world = location.getWorld();

        double rotX = Math.toRadians(degrees);
        double rotY = Math.toRadians(-location.getPitch());
        double rotZ = Math.toRadians(270 - location.getYaw());

        world.playSound(location.clone().add(newVector(rotation, radius, rotX, rotY, rotZ)),
                Sound.ENTITY_WITHER_SHOOT, 2, 2);

        BukkitRunnable bukkitRunnable = new BukkitRunnable() {
            final String uuid = UUID.randomUUID().toString();
            final List<LivingEntity> affectedEntities = new ArrayList<>();
            double angle = Math.toRadians(rotation);
            int pointsToDisplay = (int) Math.floor(20 * radius) * (fullCircle ? 2 : 1);
            final double angleIncrement = (2 * Math.PI) / pointsToDisplay / (fullCircle ? 1 : 2);

            @Override
            public void run() {
                for (int i = 0; i < 8; ++i) {
                    if (pointsToDisplay < 0) {
                        cancel();
                        affectedEntities.forEach(entity -> Common.removeMetaData(entity, uuid));
                        return;
                    }
                    pointsToDisplay--;
                    Vector vector = newVector(angle, radius, rotX, rotY, rotZ);
                    angle += angleIncrement;
                    Location dustLoc = location.clone().add(vector);

                    vector.normalize().multiply(0.2);
                    dustLoc.add(vector);
                    world.spawnParticle(
                            Particle.REDSTONE,
                            dustLoc.clone().add(vector.clone().multiply(4)),
                            1, 0, 0, 0, 0,
                            RED,
                            false
                    );
                    for (int j = 0; j < 10; j++) {
                        dustLoc.add(vector);
                        if (j == 3) {
                            world.spawnParticle(Particle.REDSTONE, dustLoc, 1, 0, 0, 0, 0, RED, false);
                        } else if (i % 2 == 0) {
                            world.spawnParticle(Particle.REDSTONE, dustLoc, 1, 0.1, 0.1, 0.1, 0, BLACK, false);
                            if (j == 4 && damage != 0) {
                                SpellEffectUtil.getNearbyLivingEntities(player, dustLoc, entity -> !entity.hasMetadata(uuid), 1).forEach(
                                        entity -> {
                                            SpellEffectUtil.damageEffect(player, entity, damage, weaponName);
                                            knockBack.apply(entity, location);
                                            entity.setMetadata(uuid, Common.getMetadataValueTrue());
                                            affectedEntities.add(entity);
                                        }
                                );
                            }
                        }
                    }
                }
            }
        };
        Common.runTaskTimer(bukkitRunnable, 0, 1);
    }

    private static @NotNull Vector newVector(double angle, double radius, double rotX, double rotY, double rotZ) {
        double x = radius * Math.sin(angle);
        double y = radius * Math.cos(angle);

        return new Vector(x, y, 0)
                .rotateAroundX(rotX)
                .rotateAroundZ(rotY)
                .rotateAroundY(rotZ);
    }
}

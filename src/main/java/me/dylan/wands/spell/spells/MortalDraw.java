package me.dylan.wands.spell.spells;

import me.dylan.wands.knockback.KnockBack;
import me.dylan.wands.spell.SpellData;
import me.dylan.wands.spell.SpellEffectUtil;
import me.dylan.wands.spell.types.Behaviour;
import org.bukkit.*;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

public class MortalDraw extends Behaviour implements SpellData, Listener {
    private final KnockBack knockBack = KnockBack.from(0.3f);

    @Override
    public Behaviour getBehaviour() {
        return this;
    }

    @Override
    public boolean cast(@NotNull Player player, @NotNull String weaponName) {
        double radius = 3;
        Location location = player.getEyeLocation();
        World world = location.getWorld();

        for (LivingEntity entity : SpellEffectUtil.getNearbyLivingEntities(player, location, 4)) {
            SpellEffectUtil.damageEffect(player, entity, 5, weaponName);
            entity.playEffect(EntityEffect.HURT);
            knockBack.apply(entity, location);
        }

        DustOptions red = new DustOptions(Color.fromRGB(255, 0, 0), 1);
        DustOptions black = new DustOptions(Color.fromRGB(0, 0, 0), 1);

        world.playSound(location, Sound.ENTITY_WITHER_SHOOT, 3, 2);

        int points = 40;

        double oldX = location.getX();
        double oldY = location.getY();
        double oldZ = location.getZ();

        double xzRotation = Math.toRadians(location.getYaw());
        double rotXZSin = Math.sin(xzRotation);
        double rotXZCos = Math.cos(xzRotation);

        double yRotation = Math.toRadians(ThreadLocalRandom.current().nextFloat() * 360.0f);
        double rotYSin = Math.sin(yRotation);
        double rotYCos = Math.cos(yRotation);

        double angleIncrement = (2 * Math.PI) / points / 2;
        new BukkitRunnable() {
            double angle = 0;
            int count = 0;
            @Override
            public void run() {
                if (++count > 8) {
                    cancel();
                } else {
                    for (int i = 0; i < 5; ++i) {
                        double sin = Math.sin(angle);
                        double cos = Math.cos(angle);

                        double x = radius * sin;
                        double y = radius * cos;

                        double yRotatedY = y * rotYCos + oldY;
                        double yRotatedZ = y * rotYSin;

                        double xyzRotatedX = yRotatedZ * rotXZCos - x * rotXZSin + oldX;
                        double xyzRotatedZ = yRotatedZ * rotXZSin + x * rotXZCos + oldZ;

                        Location dustLoc = new Location(world, xyzRotatedX, yRotatedY, xyzRotatedZ);

                        Location dustSpread = dustLoc.clone().subtract(location).multiply(0.1);

                        for (int j = 0; j < 10; j++) {
                            if (j == 3) {
                                world.spawnParticle(Particle.REDSTONE, dustLoc.add(dustSpread), 2, 0, 0, 0, 0, red, true);
                            } else {
                                world.spawnParticle(Particle.REDSTONE, dustLoc.add(dustSpread), 1, 0.05, 0.05, 0.05, 0, black, true);
                            }
                        }
                        angle += angleIncrement;
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 1);

        return false;
    }

    private void rotateX(Vector point, double cos, double sin) {
        double y = point.getY();
        double z = point.getZ();
        point.setY(y * cos - z * sin);
        point.setZ(y * sin + z * cos);
    }

    private void rotateY(Vector point, double cos, double sin) {
        double z = point.getZ();
        double x = point.getX();
        point.setZ(z * cos - x * sin);
        point.setX(z * sin + x * cos);
    }

    private void rotateZ(Vector point, double cos, double sin) {
        double x = point.getX();
        double y = point.getY();
        point.setX(x * cos - y * sin);
        point.setY(x * sin + y * cos);
    }
}

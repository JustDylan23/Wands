package me.dylan.wands.spell.util;

import me.dylan.wands.events.MagicDamageEvent;
import me.dylan.wands.utils.Common;
import me.dylan.wands.utils.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.*;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public final class SpellEffectUtil {
    private SpellEffectUtil() {
    }

    public static @NotNull Location getSpellLocation(Player player, int effectDistance) {
        if (effectDistance == 0) return player.getLocation();
        Entity entity = PlayerUtil.getTargetEntity(player, effectDistance, e -> true);
        if (entity instanceof LivingEntity && !(entity instanceof ArmorStand)) {
            return entity.getLocation();
        }
        return PlayerUtil.getTargetLocation(player, effectDistance);
    }

    public static @NotNull Location[] getHorizontalCircleFrom(@NotNull Location location, float radius, float angleOffset, float pointsMultiplier) {
        int points = (int) Math.ceil(radius * 2 * Math.PI * pointsMultiplier);
        double increment = (2 * Math.PI) / points;
        Location[] locations = new Location[points];
        double angle = Math.toRadians(angleOffset + 90);
        World world = location.getWorld();
        double originX = location.getX();
        double originY = location.getY();
        double originZ = location.getZ();
        for (int i = 0; i < points; i++) {
            double newX = originX + (radius * Math.cos(angle));
            double newZ = originZ + (radius * Math.sin(angle));
            locations[i] = new Location(world, newX, originY, newZ);
            angle += increment;
        }
        return locations;
    }

    public static @NotNull Location[] getCircleFromPlayerView(@NotNull Location location, double radius, int points, double distance) {
        World world = location.getWorld();
        Location[] locations = new Location[points];

        double oldX = location.getX();
        double oldY = location.getY();
        double oldZ = location.getZ();

        double xzRotation = Math.toRadians(270.0f + location.getYaw());
        double rotXZSin = Math.sin(xzRotation);
        double rotXZCos = Math.cos(xzRotation);

        double yRotation = Math.toRadians(180.0f - location.getPitch());
        double rotYSin = Math.sin(yRotation);
        double rotYCos = Math.cos(yRotation);

        double angleIncrement = (2 * Math.PI) / points;
        double angle = 0;
        for (int i = 0; i < points; ++i) {
            double sin = Math.sin(angle);
            double cos = Math.cos(angle);

            double x = radius * sin;
            double y = radius * cos;

            double yRotatedY = y * rotYCos - distance * rotYSin + oldY;
            double yRotatedZ = y * rotYSin + distance * rotYCos;

            double xyzRotatedX = yRotatedZ * rotXZCos - x * rotXZSin + oldX;
            double xyzRotatedZ = yRotatedZ * rotXZSin + x * rotXZCos + oldZ;
            locations[i] = new Location(world, xyzRotatedX, yRotatedY, xyzRotatedZ);
            angle += angleIncrement;
        }
        return locations;
    }

    public static boolean checkFriendlyFireOption(@NotNull Entity attacker, Entity victim) {
        Scoreboard scoreboard = Objects.requireNonNull(Bukkit.getScoreboardManager()).getMainScoreboard();
        Team team = scoreboard.getEntryTeam(attacker.getName());
        String entry = (victim instanceof Player)
                ? victim.getName()
                : victim.getUniqueId().toString();
        return team == null || team.allowFriendlyFire() || !team.equals(scoreboard.getEntryTeam(entry));
    }

    public static List<LivingEntity> getNearbyLivingEntities(Player player, Location loc, double radius) {
        return getNearbyLivingEntities(player, loc, Common.emptyPredicate(), radius, radius, radius);
    }

    public static List<LivingEntity> getNearbyLivingEntities(Player player, @NotNull Location loc, Predicate<LivingEntity> predicate, double radius) {
        return getNearbyLivingEntities(player, loc, predicate, radius, radius, radius);
    }

    public static List<LivingEntity> getNearbyLivingEntities(Player player, @NotNull Location loc, Predicate<LivingEntity> predicate, double rx, double ry, double rz) {
        return loc.getWorld()
                .getNearbyEntities(loc, rx, ry, rz).stream()
                .filter(Entity::isValid)
                .filter(LivingEntity.class::isInstance)
                .filter(entity -> !(entity instanceof ArmorStand))
                .filter(entity -> !entity.equals(player))
                .filter(entity -> checkFriendlyFireOption(player, entity))
                .map(LivingEntity.class::cast)
                .filter(predicate)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static void spawnColoredSpellMob(Location location, int count, double offsetX, double offsetY, double offsetZ, int red, int green, int blue, boolean rainbow) {
        if (rainbow) {
            location.getWorld().spawnParticle(Particle.SPELL_MOB, location, (count > 0) ? count : 1, offsetX, offsetY, offsetZ, 1, null, true);
        } else {
            float redR = Math.max(Float.MIN_NORMAL, red / 255.0F);
            float greenG = Math.max(0, green / 255.0F);
            float blueB = Math.max(0, blue / 255.0F);
            for (int i = 0; count > i; i++) {
                location.getWorld().spawnParticle(Particle.SPELL_MOB, randomizeLoc(location, offsetX, offsetY, offsetZ), 0, redR, greenG, blueB, 1, null, true);
            }
        }
    }

    public static @NotNull Location randomizeLoc(@NotNull Location location, double rx, double ry, double rz) {
        return location.clone().add(randomize(rx), randomize(ry), randomize(rz));
    }

    public static double randomize(double d) {
        if (d == 0) return 0;
        return ThreadLocalRandom.current().nextDouble() * d * 2.0 - d;
    }

    public static void damageEffect(Player attacker, Damageable victim, int amount, @NotNull String weaponDisplayName) {
        if (amount != 0) {
            if (victim instanceof Player) {
                Player player = (Player) victim;
                AttributeInstance atr = player.getAttribute(Attribute.GENERIC_ARMOR);
                double armorDamageReduction = 1;
                if (atr != null) {
                    armorDamageReduction = 1 - (1.75 * atr.getValue()) / 100.0D;
                }
                double finalDamage = Math.round((amount * armorDamageReduction) * 10.0D) / 10.0D;
                player.setLastDamageCause(new MagicDamageEvent(player, attacker, finalDamage, weaponDisplayName));
                victim.damage(finalDamage);
            } else victim.damage(amount);
        }
    }

    public static @NotNull Location getFirstPassableBlockAbove(@NotNull Location location) {
        if (location.getBlock().isPassable()) return location;
        Location loc = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());
        for (int i = (int) loc.getY(); i < 256; i++) {
            loc.add(0, 1, 0);
            if (loc.getBlock().isPassable()) return loc;
        }
        return loc;
    }

    public static @NotNull Location getFirstGroundBlockUnder(@NotNull Location location) {
        Location loc = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());
        for (int i = (int) loc.getY(); i > 0; i--) {
            loc.add(0, -1, 0);
            if (!loc.getBlock().isPassable()) return loc.add(0, 1, 0);
        }
        return loc;
    }
}

package me.dylan.wands.spell;

import com.destroystokyo.paper.block.TargetBlockInfo;
import me.dylan.wands.Main;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.entity.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import org.bukkit.scoreboard.Team.Option;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class SpellEffectUtil {
    public static final String UNTARGETABLE = UUID.randomUUID().toString();
    private static final Main plugin = Main.getPlugin();

    @Contract(value = " -> fail", pure = true)
    private SpellEffectUtil() {
        throw new UnsupportedOperationException();
    }

    @Nullable
    public static Location getSpellLocation(int effectDistance, Player player, boolean requireLivingTarget) {
        if (effectDistance == 0) return player.getLocation();
        Entity entity = player.getTargetEntity(effectDistance);
        if (entity instanceof LivingEntity && !(entity instanceof ArmorStand) && !entity.hasMetadata(UNTARGETABLE)) {
            return entity.getLocation().add(0, 0.5, 0).toCenterLocation();
        } else if (requireLivingTarget) {
            player.sendActionBar("§cselect a valid target");
            return null;
        }
        TargetBlockInfo info = player.getTargetBlockInfo(effectDistance);
        if (info == null) {
            return null;
        }
        if (info.getBlock().getType() == Material.AIR) {
            return info.getBlock().getLocation().toCenterLocation();
        }
        return info.getRelativeBlock().getLocation().toCenterLocation();
    }

    public static Location[] getHorizontalCircleFrom(@NotNull Location location, float radius) {
        int points = (int) Math.ceil(radius * 2 * Math.PI);
        double increment = (2 * Math.PI) / points;
        Location[] locations = new Location[points];
        double angle = 0;
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

    @SuppressWarnings("SameParameterValue")
    public static Location[] getCircleFromPlayerView(@NotNull Location location, double radius, int points, double distance) {
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

    private static boolean canDamage(Player p1, Player p2) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = scoreboard.getEntryTeam(p1.getName());
        return team == null || team.allowFriendlyFire() || !team.equals(scoreboard.getEntryTeam(p2.getName()));
    }

    public static List<LivingEntity> getNearbyLivingEntities(Player player, Location loc, double radius) {
        return getNearbyLivingEntities(player, loc, b -> true, radius);
    }

    public static List<LivingEntity> getNearbyLivingEntities(Player player, @NotNull Location loc, Predicate<LivingEntity> predicate, double radius) {
        return loc.getWorld()
                .getNearbyEntities(loc, radius, radius, radius).stream()
                .filter(entity ->
                        entity instanceof LivingEntity
                                && !(entity instanceof ArmorStand)
                                && (Main.getPlugin().getConfigurableData().isSelfHarmAllowed() || !entity.equals(player))
                                && (!(entity instanceof Player) || canDamage((Player) entity, player))
                )
                .map(LivingEntity.class::cast)
                .filter(predicate)
                .collect(Collectors.toCollection(ArrayList::new));
    }


    public static void runTaskLater(Runnable runnable, @NotNull int... delays) {
        int delay = 0;
        for (int i : delays) {
            delay += i;
            Bukkit.getScheduler().runTaskLater(plugin, runnable, delay);
        }
    }

    public static void spawnColoredParticle(@NotNull Particle particle, Location location, int count, double offsetX, double offsetY, double offsetZ, int red, int green, int blue, boolean rainbow) {
        switch (particle) {
            case REDSTONE:
            case SPELL_MOB:
            case SPELL_MOB_AMBIENT:
                if (rainbow) {
                    location.getWorld().spawnParticle(particle, location, (count > 0) ? count : 1, offsetX, offsetY, offsetZ, 1, null, true);
                } else {
                    float redR = Math.max(Float.MIN_NORMAL, red / 255F);
                    float greenG = Math.max(0, green / 255F);
                    float blueB = Math.max(0, blue / 255F);
                    for (int i = 0; count > i; i++) {
                        location.getWorld().spawnParticle(particle, randomizeLoc(location, offsetX, offsetY, offsetZ), 0, redR, greenG, blueB, 1, null, true);
                    }
                }
        }
    }

    @NotNull
    public static Location randomizeLoc(@NotNull Location location, double x, double y, double z) {
        return location.clone().add(randomize(x), randomize(y), randomize(z));
    }

    public static double randomize(double d) {
        return ThreadLocalRandom.current().nextDouble() * d * 2.0 - d;
    }

    public static void damageEffect(Player attacker, Damageable victim, int amount, String weaponDisplayName) {
        if (amount != 0) {
            if (victim instanceof Player) {
                victim.setMetadata("deathMessage", new FixedMetadataValue(plugin,
                        ((Player) victim).getDisplayName()
                                + " was slain by "
                                + attacker.getDisplayName()
                                + " using §7[§r"
                                + weaponDisplayName
                                + "§7]"
                ));
                victim.setLastDamageCause(new EntityDamageByEntityEvent(attacker, victim, DamageCause.CUSTOM, amount));
            }
            double armorDamageReduction = 1;
            if (victim instanceof Player) {
                Player player = (Player) victim;
                AttributeInstance atr = player.getAttribute(Attribute.GENERIC_ARMOR);
                if (atr != null) {
                    armorDamageReduction = 1 - (1.75 * atr.getValue()) / 100D;
                }
            }
            victim.damage(Math.round((amount * armorDamageReduction) * 10) / 10);
        }
    }

    public static Location getFirstPassableBlockAbove(@NotNull Location location) {
        if (location.getBlock().isPassable()) return location;
        Location loc = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());
        for (int i = (int) loc.getY(); i < 256; i++) {
            loc.add(0, 1, 0);
            if (loc.getBlock().isPassable()) return loc;
        }
        return loc;
    }

    public static Location getFirstGroundBlockUnder(@NotNull Location location) {
        Location loc = new Location(location.getWorld(), location.getX(), location.getY(), location.getZ());
        for (int i = (int) loc.getY(); i > 0; i--) {
            loc.add(0, -1, 0);
            if (!loc.getBlock().isPassable()) return loc.add(0, 1, 0);
        }
        return loc;
    }
}

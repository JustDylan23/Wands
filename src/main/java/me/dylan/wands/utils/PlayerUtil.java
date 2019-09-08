package me.dylan.wands.utils;

import me.dylan.wands.spell.TargetBlockInfo;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Predicate;

public class PlayerUtil {
    private PlayerUtil() {
        throw new UnsupportedOperationException("Instantiating util class");
    }

    public static void sendActionBar(@NotNull Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
    }

    @Nullable
    public static Entity getTargetEntity(@NotNull Player player, int distance, Predicate<Entity> predicate) {
        Location location = player.getLocation();
        Vector facing = location.getDirection();

        Location farthest = location.clone().add(facing.clone().multiply(distance));
        BoundingBox area = new BoundingBox(location.getX(), location.getY(), location.getZ(), farthest.getX(), farthest.getY(), farthest.getZ());

        Collection<Entity> entities = location.getWorld().getNearbyEntities(area, e -> e != player && predicate.test(e));

        Vector start = location.toVector();

        double minDistance = Double.MAX_VALUE;
        Entity bestMatch = null;
        for (Entity entity : entities) {
            BoundingBox boundingBox = entity.getBoundingBox();
            RayTraceResult rayTrace = boundingBox.rayTrace(start, facing, distance);

            if (rayTrace == null)
                continue;
            double distanceSq = rayTrace.getHitPosition().distanceSquared(start);
            if (distanceSq < minDistance) {
                minDistance = distanceSq;
                bestMatch = entity;
            }
        }
        return bestMatch;
    }

    @Nullable
    public static TargetBlockInfo getTargetBlockInfo(@NotNull Player player, int maxDistance) {
        RayTraceResult result = player.rayTraceBlocks(maxDistance);
        if (result == null || result.getHitBlock() == null || result.getHitBlockFace() == null)
            return null;
        return new TargetBlockInfo(result.getHitBlock(), result.getHitBlockFace());
    }
}
package me.dylan.wands.utils;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.BoundingBox;
import org.bukkit.util.RayTraceResult;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Predicate;

public final class PlayerUtil {
    private PlayerUtil() {
    }

    public static void sendActionBar(@NotNull Player player, String message) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
    }

    public static @Nullable LivingEntity getTargetEntity(@NotNull Player player, int distance) {
        Location location = player.getEyeLocation();
        World world = location.getWorld();
        if (world != null) {
            RayTraceResult rayTraceResult = world.rayTraceEntities(location, location.getDirection(), distance, e -> !e.equals(player) && e instanceof LivingEntity);
            if (rayTraceResult != null) {
                return (LivingEntity) rayTraceResult.getHitEntity();
            }
        }
        return null;
    }

    @NotNull
    public static Location getTargetLocation(@NotNull Player player, int maxDistance) {
        RayTraceResult result = player.rayTraceBlocks(maxDistance);
        if (result == null) {
            Location playerLocation = player.getEyeLocation();
            return playerLocation.add(playerLocation.getDirection().multiply(maxDistance));
        }
        return result.getHitPosition().toLocation(player.getWorld());
    }
}
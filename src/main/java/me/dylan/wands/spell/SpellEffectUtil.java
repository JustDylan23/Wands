package me.dylan.wands.spell;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

public class SpellEffectUtil {
    private SpellEffectUtil() {
        throw new UnsupportedOperationException();
    }

    public static Location getSpellLocation(int effectDistance, Player player) {
        if (effectDistance == 0) return player.getLocation();
        Entity entity = player.getTargetEntity(effectDistance);
        if (entity != null) {
            return entity.getLocation().add(0, 0.5, 0);
        }
        Block block = player.getTargetBlock(effectDistance);
        if (block != null) {
            return block.getLocation().toCenterLocation().subtract(player.getLocation().getDirection().normalize());
        }
        return player.getLocation();
    }

    public static Location[] getCircleFrom(Location location, float radius) {
        int density = (int) StrictMath.ceil(radius * 2 * Math.PI);
        double increment = (2 * Math.PI) / density;
        Location[] locations = new Location[density];
        double angle = 0;
        World world = location.getWorld();
        double originX = location.getX();
        double originY = location.getY();
        double originZ = location.getZ();
        for (int i = 0; i < density; i++) {
            angle += increment;
            double newX = originX + (radius * Math.cos(angle));
            double newZ = originZ + (radius * Math.sin(angle));
            locations[i] = new Location(world, newX, originY, newZ);
        }
        return locations;
    }
}

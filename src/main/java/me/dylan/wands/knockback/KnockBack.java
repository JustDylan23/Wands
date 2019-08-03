package me.dylan.wands.knockback;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class KnockBack {
    public static final KnockBack NONE = new KnockBack(0, 0) {
        @Override
        public void apply(@NotNull LivingEntity livingEntity, @NotNull Location from) {
        }
    };
    private final float xz;
    private final float y;

    private KnockBack(float xz, float y) {
        this.xz = xz;
        this.y = y;
    }

    public static KnockBack from(float xz, float y) {
        if (xz == 0 && y == 0) {
            return NONE;
        } else {
            return new KnockBack(xz, y);
        }
    }

    public float getXz() {
        return xz;
    }

    public float getY() {
        return y;
    }

    public void apply(@NotNull LivingEntity livingEntity, @NotNull Location from) {
        Vector direction = livingEntity.getLocation().subtract(from).toVector();
        direction.setY(0).normalize().multiply(xz).setY(y);
        if (Double.isFinite(direction.getX()) && Double.isFinite(direction.getY()) && Double.isFinite(direction.getZ())) {
            livingEntity.setVelocity(direction);
        }
    }
}

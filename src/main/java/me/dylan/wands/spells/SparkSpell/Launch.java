package me.dylan.wands.spells.SparkSpell;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class Launch extends BasicSparkSpell {

    public Launch() {
        super("Launch", 30);
    }

    public void cast(Player player) {
        trigger(player, loc -> {
            loc.add(0, 1, 0);
            getNearbyDamageables(loc, 3.2).forEach(entity -> {
                if (!entity.equals(player)) {
                    player.damage(3, entity);
                    entity.setVelocity(new Vector(0, 1, 0));
                }
            });
        });
    }
}

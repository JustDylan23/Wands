package me.dylan.wands.spells.SparkSpell;

import org.bukkit.entity.Player;

public class Launch extends BasicSparkSpell {

    public Launch() {
        super("Launch", 30);
    }

    public void cast(Player player) {
        cast(player, location -> {

        });
    }
}

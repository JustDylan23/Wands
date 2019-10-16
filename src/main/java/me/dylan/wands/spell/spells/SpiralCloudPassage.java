package me.dylan.wands.spell.spells;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.spellbuilders.Behavior;
import me.dylan.wands.utils.Common;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

public class SpiralCloudPassage extends Behavior implements Castable {

    @Override
    public Behavior createBehaviour() {
        return this;
    }

    @Override
    public boolean cast(@NotNull Player player, @NotNull String weaponName) {
        BukkitRunnable bukkitRunnable = new BukkitRunnable() {
            int count;

            @Override
            public void run() {
                ++count;
                if (count > 8) {
                    cancel();
                } else {
                    MortalDraw.draw(player,
                            ThreadLocalRandom.current().nextInt(0, 360),
                            ThreadLocalRandom.current().nextInt(1, 4), 5,
                            ThreadLocalRandom.current().nextInt(0, 360), true);
                }
            }
        };
        Common.runTaskTimer(bukkitRunnable, 0, 3);
        return true;
    }
}

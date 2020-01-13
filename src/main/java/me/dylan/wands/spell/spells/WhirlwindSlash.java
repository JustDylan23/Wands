package me.dylan.wands.spell.spells;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.spellbuilders.Behavior;
import me.dylan.wands.utils.Common;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

public class WhirlwindSlash implements Castable {
    @Override
    public Behavior createBehaviour() {
        return new Behavior() {
            @Override
            public boolean cast(@NotNull Player player, @NotNull String weapon) {
                BukkitRunnable bukkitRunnable = new BukkitRunnable() {
                    int count = 0;

                    @Override
                    public void run() {
                        ++count;
                        if (count > 5) {
                            cancel();
                        } else {
                            MortalDraw.draw(player, ThreadLocalRandom.current().nextInt(0, 360), 1.8, 4, 0, false);
                        }
                    }
                };
                Common.runTaskTimer(bukkitRunnable, 0, 3);
                return true;
            }
        };
    }
}

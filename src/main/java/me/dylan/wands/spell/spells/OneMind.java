package me.dylan.wands.spell.spells;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.spellbuilders.Behavior;
import me.dylan.wands.utils.Common;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class OneMind extends Behavior implements Castable {

    @Override
    public Behavior createBehaviour() {
        return this;
    }

    @Override
    public boolean cast(@NotNull Player player, @NotNull String weapon) {
        BukkitRunnable bukkitRunnable = new BukkitRunnable() {
            int count = 0;

            @Override
            public void run() {
                ++count;
                if (count > 3) {
                    cancel();
                } else {
                    int degrees = 0;
                    switch (count) {
                        case 1:
                            degrees = 180;
                            break;
                        case 2:
                            degrees = 75;
                            break;
                        case 3:
                            degrees = 285;
                    }
                    MortalDraw.draw(player, degrees, 2, 4, 0, false);
                }
            }
        };
        Common.runTaskTimer(bukkitRunnable, 0, 10);
        return true;
    }
}

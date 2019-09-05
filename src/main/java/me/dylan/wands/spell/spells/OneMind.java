package me.dylan.wands.spell.spells;

import me.dylan.wands.spell.SpellData;
import me.dylan.wands.spell.types.Behavior;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class OneMind extends Behavior implements SpellData {

    @Override
    public Behavior getBehavior() {
        return this;
    }

    @Override
    public boolean cast(@NotNull Player player, @NotNull String weaponName) {
        new BukkitRunnable() {
            int count;

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
        }.runTaskTimer(plugin, 0, 10);
        return true;
    }
}

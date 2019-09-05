package me.dylan.wands.spell.spells;

import me.dylan.wands.spell.SpellData;
import me.dylan.wands.spell.types.Behavior;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class DualDraw extends Behavior implements SpellData {
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
                if (count > 2) {
                    cancel();
                } else {
                    if (count == 1) {
                        MortalDraw.draw(player, 255, 2.6, 7, 0, false);
                    } else {
                        MortalDraw.draw(player, 285, 2.2, 7, -30, false);
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 7);
        return true;
    }
}

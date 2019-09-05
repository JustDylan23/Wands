package me.dylan.wands.spell.spells;

import me.dylan.wands.spell.SpellData;
import me.dylan.wands.spell.types.Behavior;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

public class FloatingPassage extends Behavior implements SpellData {

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
                if (count > 10) {
                    cancel();
                } else {
                    MortalDraw.draw(player, ThreadLocalRandom.current().nextInt(0, 360), 2, 5, 90, false);
                }
            }
        }.runTaskTimer(plugin, 0, 3);
        return true;
    }
}

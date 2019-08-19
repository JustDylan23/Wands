package me.dylan.wands.spell.spells;

import me.dylan.wands.spell.SpellData;
import me.dylan.wands.spell.types.Behaviour;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

public class CrossBlades extends Behaviour implements SpellData {
    @Override
    public Behaviour getBehaviour() {
        return this;
    }

    @Override
    public boolean cast(@NotNull Player player, @NotNull String weaponName) {
        new BukkitRunnable() {
            int count = 0;
            double offSet = ThreadLocalRandom.current().nextDouble() * 20;

            @Override
            public void run() {
                if (++count > 2) {
                    cancel();
                } else {
                    if (count == 1) {
                        MortalDraw.draw(player, weaponName, 250 + offSet, 3.3);
                    } else {
                        MortalDraw.draw(player, weaponName, 90 + offSet, 3.3);
                    }
                }
            }
        }.runTaskTimer(plugin, 0, 4);
        return true;
    }
}

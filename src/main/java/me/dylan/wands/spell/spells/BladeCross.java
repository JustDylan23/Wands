package me.dylan.wands.spell.spells;

import me.dylan.wands.spell.SpellData;
import me.dylan.wands.spell.types.Behaviour;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

public class BladeCross extends Behaviour implements SpellData {
    @Override
    public Behaviour getBehaviour() {
        return this;
    }

    @Override
    public boolean cast(@NotNull Player player, @NotNull String weaponName) {
        new BukkitRunnable() {
            int count = 0;
            int offSet = ThreadLocalRandom.current().nextInt(0, 20);

            @Override
            public void run() {
                if (++count > 2) {
                    cancel();
                } else {
                    double degrees;
                    if (count == 1) {
                        degrees = 250 + offSet;
                    } else {
                        degrees = 90 + offSet;
                    }
                    OneMind.draw(player, weaponName, degrees, 3.3, entity -> {
                        entity.damage(7);
                    }, 0, false);

                }
            }
        }.runTaskTimer(plugin, 0, 4);
        return true;
    }
}

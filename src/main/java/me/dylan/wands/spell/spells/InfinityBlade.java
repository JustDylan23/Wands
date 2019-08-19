package me.dylan.wands.spell.spells;

import me.dylan.wands.spell.SpellData;
import me.dylan.wands.spell.types.Behaviour;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

public class InfinityBlade extends Behaviour implements SpellData {
    @Override
    public Behaviour getBehaviour() {
        return this;
    }

    @Override
    public boolean cast(@NotNull Player player, @NotNull String weaponName) {
        new BukkitRunnable() {
            int count = 0;
            @Override
            public void run() {
                if (++count > 8) {
                    cancel();
                } else {
                    MortalDraw.draw(player, weaponName, ThreadLocalRandom.current().nextDouble() * 360, 5 - ThreadLocalRandom.current().nextDouble() * 3);
                }
            }
        }.runTaskTimer(plugin, 0, 3);
        return true;
    }
}

package me.dylan.wands.spell.spells;

import me.dylan.wands.knockback.KnockBack;
import me.dylan.wands.spell.SpellData;
import me.dylan.wands.spell.types.Behaviour;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

public class SpiralCloudPassage extends Behaviour implements SpellData {
    private KnockBack knockBack = KnockBack.from(0.2f);
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
                    Location location = player.getLocation();
                    OneMind.draw(player, weaponName, ThreadLocalRandom.current().nextInt(0, 360), ThreadLocalRandom.current().nextInt(1, 4), entity -> {
                        entity.damage(4);
                        knockBack.apply(entity, location);
                    }, ThreadLocalRandom.current().nextInt(0, 360), ThreadLocalRandom.current().nextBoolean());
                }
            }
        }.runTaskTimer(plugin, 0, 3);
        return true;
    }
}

package me.dylan.wands.spell.spells;

import me.dylan.wands.knockback.KnockBack;
import me.dylan.wands.spell.SpellData;
import me.dylan.wands.spell.SpellEffectUtil;
import me.dylan.wands.spell.types.Behaviour;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.ThreadLocalRandom;

public class FloatingPassage extends Behaviour implements SpellData {
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
                if (++count > 10) {
                    cancel();
                } else {
                    Location location = player.getLocation();
                    MortalDraw.draw(player, ThreadLocalRandom.current().nextInt(0, 360), 2, entity -> {
                        SpellEffectUtil.damageEffect(player, entity, 5, weaponName);
                        knockBack.apply(entity, location);
                    }, 90, false);
                }
            }
        }.runTaskTimer(plugin, 0, 3);
        return true;
    }
}

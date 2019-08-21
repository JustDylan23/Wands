package me.dylan.wands.spell.spells;

import me.dylan.wands.knockback.KnockBack;
import me.dylan.wands.spell.SpellData;
import me.dylan.wands.spell.SpellEffectUtil;
import me.dylan.wands.spell.types.Behaviour;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class OneMind extends Behaviour implements SpellData {
    private KnockBack knockBack = KnockBack.from(0.3f);

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
                if (++count > 3) {
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
                    Location location = player.getLocation();
                    MortalDraw.draw(player, degrees, 3, entity -> {
                        SpellEffectUtil.damageEffect(player, entity, 3, weaponName);
                        knockBack.apply(entity, location);
                    }, 0, false);
                }
            }
        }.runTaskTimer(plugin, 0, 10);
        return true;
    }
}

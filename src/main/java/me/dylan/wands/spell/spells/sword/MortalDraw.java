package me.dylan.wands.spell.spells.sword;

import me.dylan.wands.ListenerRegistry;
import me.dylan.wands.WandsPlugin;
import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.spellbuilders.Behavior;
import me.dylan.wands.spell.spells.AffinityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class MortalDraw extends Behavior implements Castable, Listener {
    Map<Player, BukkitTask> secondAnimation = new HashMap<>();

    public MortalDraw() {
        ListenerRegistry.addListener(this);
    }

    @EventHandler
    private void leave(PlayerQuitEvent event) {
        secondAnimation.remove(event.getPlayer());
    }

    @Override
    public CastType getCastType() {
        return CastType.SWORD_SKILL;
    }

    @Override
    public AffinityType[] getAffinityTypes() {
        return new AffinityType[]{AffinityType.SWORD_ARTS};
    }

    @Override
    public Behavior createBehaviour() {
        return this;
    }

    @Override
    public boolean cast(@NotNull Player player, @NotNull String weapon) {
        BukkitTask bukkitTask = secondAnimation.get(player);
        if (bukkitTask != null) {
            CutEffect.draw(player, 320, 2, 5, 0, false);
            bukkitTask.cancel();
            secondAnimation.remove(player);
        } else {
            CutEffect.draw(player, 255, 2, 5, 0, false);
            bukkitTask = new BukkitRunnable() {
                @Override
                public void run() {
                    secondAnimation.remove(player);
                }
            }.runTaskLater(WandsPlugin.getPlugin(WandsPlugin.class), 40L);
            secondAnimation.put(player, bukkitTask);
        }
        return true;
    }
}
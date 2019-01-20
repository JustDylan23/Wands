package me.dylan.wands.spells.WaveSpell;

import me.dylan.wands.Spell;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.function.Consumer;

public abstract class BasicWaveSpell extends Spell {

    private final int range;

    BasicWaveSpell(String spellName, int range) {
        super(spellName);
        this.range = range;
    }

    final void trigger(Player player, Consumer<Location> consumer) {
        Location pLoc = player.getEyeLocation();
        Vector direction = player.getLocation().getDirection().normalize();

        ArrayList<Location> locations = new ArrayList<>();
        for (int i = 1; i <= range; i++) {
            Location location = direction.clone().multiply(i).toLocation(player.getWorld()).add(pLoc);
            Bukkit.getScheduler().runTaskLater(plugin, () -> consumer.accept(location), i);
        }
    }
}

package me.dylan.wands.spell.tools;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

// todo document
public class SpellInfo {
    private final Player caster;
    private final Location origination;
    private final World world;
    private final Location spellLocation;

    public SpellInfo(Player caster, @NotNull Location origination, Location spellLocation) {
        this.caster = caster;
        this.origination = origination;
        this.world = origination.getWorld();
        this.spellLocation = spellLocation;
    }

    public Player caster() {
        return caster;
    }

    public Location origin() {
        return origination;
    }

    public World world() {
        return world;
    }

    public Location spellLocation() {
        return spellLocation;
    }
}

package me.dylan.wands.spell.accessories;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

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

    /**
     * @return Caster of the spell.
     */
    public Player caster() {
        return caster;
    }

    /**
     * @return Location from where the spell was fired
     */
    public Location origin() {
        return origination;
    }

    /**
     * @return World of the spell's origination.
     */
    public World world() {
        return world;
    }

    /**
     * @return Location of the spells' projectile or of the location the spell concludes.
     */
    public Location spellLocation() {
        return spellLocation;
    }
}

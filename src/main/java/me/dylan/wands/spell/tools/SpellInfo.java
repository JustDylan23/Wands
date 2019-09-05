package me.dylan.wands.spell.tools;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

// todo document
public class SpellInfo {
    public final Player caster;
    public final Location origination;
    public final World world;
    public final Location spellLocation;

    public SpellInfo(@NotNull Player caster, @NotNull Location origination, @NotNull Location spellLocation) {
        this.caster = caster;
        this.origination = origination;
        this.world = origination.getWorld();
        this.spellLocation = spellLocation;
    }
}

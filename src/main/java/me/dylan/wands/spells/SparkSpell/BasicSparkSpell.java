package me.dylan.wands.spells.SparkSpell;

import me.dylan.wands.Spell;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.function.Consumer;

public abstract class BasicSparkSpell extends Spell {

    private final int range;

    public BasicSparkSpell(String spellName, int range) {
        super(spellName);
        this.range = range;
    }

    protected final void cast(Player player, Consumer<Location> consumer) {
        consumer.accept(getSpellLocation(player, range));
    }
}
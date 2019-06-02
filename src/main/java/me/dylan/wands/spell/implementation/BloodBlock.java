package me.dylan.wands.spell.implementation;

import me.dylan.wands.spell.Spell;
import me.dylan.wands.spell.handler.Behaviour;
import me.dylan.wands.spell.handler.MovingBlock;
import org.bukkit.Material;

public class BloodBlock extends Spell {

    @Override
    public Behaviour getBehaviour() {
        return MovingBlock.newBuilder(Material.REDSTONE_BLOCK).build();
    }
}

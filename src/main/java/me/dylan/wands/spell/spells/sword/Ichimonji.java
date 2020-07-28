package me.dylan.wands.spell.spells.sword;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.spellbuilders.Behavior;
import me.dylan.wands.spell.spells.AffinityType;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

public class Ichimonji extends Behavior implements Castable {
    private final Vector offset = new Vector(0, -0.5, 0);
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
        CutEffect.draw(player, 0, 1, 10, -80, false, offset);
        Vector vector = player.getLocation().getDirection().normalize().multiply(0.5).setY(player.getVelocity().getY());
        player.setVelocity(vector);
        return true;
    }
}

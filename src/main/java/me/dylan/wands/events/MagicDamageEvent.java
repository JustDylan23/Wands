package me.dylan.wands.events;

import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

public final class MagicDamageEvent extends EntityDamageEvent {
    private static final HandlerList handlers = new HandlerList();
    private final Player attacker;
    private final String weaponDisplayName;

    public MagicDamageEvent(Player victim, Player attacker, double amount, String weaponName) {
        super(victim, DamageCause.CUSTOM, amount);
        this.attacker = attacker;
        this.weaponDisplayName = weaponName;
    }

    @Override
    @NotNull
    public Player getEntity() {
        return (Player) entity;
    }

    public Player getAttacker() {
        return attacker;
    }

    public String getWeaponDisplayName() {
        return weaponDisplayName;
    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}

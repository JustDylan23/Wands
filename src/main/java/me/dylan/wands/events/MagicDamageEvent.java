package me.dylan.wands.events;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.entity.EntityDamageEvent;
import org.jetbrains.annotations.NotNull;

public final class MagicDamageEvent extends EntityDamageEvent {
    private static final HandlerList handlers = new HandlerList();
    private final Player attacker;
    private final String weaponDisplayName;

    public MagicDamageEvent(Entity victim, Player attacker, double amount, String weaponName) {
        super(victim, DamageCause.CUSTOM, amount);
        this.attacker = attacker;
        this.weaponDisplayName = weaponName;
    }

    @Override
    public @NotNull Player getEntity() {
        return (Player) entity;
    }

    public Player getAttacker() {
        return attacker;
    }

    public String getWeaponDisplayName() {
        return weaponDisplayName;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }
}

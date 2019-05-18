package me.dylan.wands.spellbehaviour;

import me.dylan.wands.plugindata.ListenerRegister;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;

public class LeapSpell extends SpellBehaviour implements Listener {

    private final float forwardsSpeed;
    private final float upwardsSpeed;

    private LeapSpell(Builder.BuilderWrapper builderWrapper, float forwardsSpeed, float upwardsSpeed) {
        super(builderWrapper);
        ListenerRegister.addListener(this);
        this.forwardsSpeed = forwardsSpeed;
        this.upwardsSpeed = upwardsSpeed;
    }

    @Override
    void cast(Player player) {
        push(player, forwardsSpeed, upwardsSpeed);
    }

    private void push(Player player, float forward, float upward) {
        Vector forwards = player.getLocation().getDirection().setY(0).normalize().multiply(forward);
        Vector upwards = new Vector(0, 1, 0).multiply(upward);
        player.setVelocity(upwards.add(forwards));
    }

    @EventHandler
    private void onDamage(EntityDamageEvent event) {
    }
}
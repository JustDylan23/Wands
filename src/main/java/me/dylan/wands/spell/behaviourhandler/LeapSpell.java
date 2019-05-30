package me.dylan.wands.spell.behaviourhandler;

import me.dylan.wands.pluginmeta.ListenerRegistry;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;

//TODO Finish base template for spell that allow the caster to leap

public class LeapSpell extends BaseBehaviour implements Listener {

    private final float forwardsSpeed;
    private final float upwardsSpeed;

    private LeapSpell(AbstractBuilder.BaseMeta baseMeta, float forwardsSpeed, float upwardsSpeed) {
        super(baseMeta);
        ListenerRegistry.addListener(this);
        this.forwardsSpeed = forwardsSpeed;
        this.upwardsSpeed = upwardsSpeed;
    }

    @Override
    public void cast(Player player) {
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

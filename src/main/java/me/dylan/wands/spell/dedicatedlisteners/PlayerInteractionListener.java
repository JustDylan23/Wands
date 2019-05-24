package me.dylan.wands.spell.dedicatedlisteners;

import me.dylan.wands.util.WandUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public final class PlayerInteractionListener implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (action == Action.PHYSICAL) return;

        Player player = event.getPlayer();
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (event.getHand() == EquipmentSlot.HAND && WandUtil.isWand(itemStack)) {
            event.setCancelled(true);
            if (action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK) {
                WandUtil.castSpell(player, itemStack);
            } else if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
                WandUtil.nextSpell(player, itemStack);
            }

        }
    }

    @EventHandler
    private void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        player.setLastDamageCause(new EntityDamageEvent(player, EntityDamageEvent.DamageCause.MAGIC, 0));
    }
}


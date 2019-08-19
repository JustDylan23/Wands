package me.dylan.wands.spell;

import me.dylan.wands.Main;
import me.dylan.wands.MouseClickListeners.ClickEvent;
import me.dylan.wands.MouseClickListeners.LeftClickListener;
import me.dylan.wands.MouseClickListeners.RightClickListener;
import me.dylan.wands.events.MagicDamageEvent;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class PlayerListener implements Listener, LeftClickListener, RightClickListener {
    private final Map<Player, MagicDamageEvent> playerMagicDamageEventMap = new HashMap<>();

    public PlayerListener() {
        Main.getPlugin().getMouseClickListeners().addBoth(this);
    }

    @Override
    public void onLeftClick(@NotNull ClickEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (SpellManagementUtil.isWand(itemStack) && SpellManagementUtil.canUse(player)) {
            SpellManagementUtil.castSpell(player, itemStack);
        }
    }

    @Override
    public void onRightClick(@NotNull ClickEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (SpellManagementUtil.isWand(itemStack) && SpellManagementUtil.canUse(player)) {
            event.cancel();
            SpellManagementUtil.nextSpell(player, itemStack);
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        MagicDamageEvent magicDamageEvent = playerMagicDamageEventMap.remove(victim);
        if (magicDamageEvent != null) {
            event.setDeathMessage(null);
            String str = magicDamageEvent.getWeaponDisplayName();
            ComponentBuilder componentBuilder = new ComponentBuilder(
                    victim.getDisplayName()
                            + "§r was slain by "
                            + magicDamageEvent.getAttacker().getDisplayName()
                            + "§r using §7[§r")
                    .append(str)
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText(str)))
                    .append("§7]");
            BaseComponent[] baseComponents = componentBuilder.create();
            Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage(baseComponents));
            event.setDeathMessage(null);
            Player attacker = magicDamageEvent.getAttacker();
            if (!attacker.equals(victim)) {
                attacker.incrementStatistic(Statistic.PLAYER_KILLS);
                Set<Objective> objectives = Bukkit.getScoreboardManager().getMainScoreboard().getObjectivesByCriteria("playerKillCount");
                for (Objective objective : objectives) {
                    Score score = objective.getScore(attacker.getName());
                    score.setScore(score.getScore() + 1);
                }
            }
        }
    }

    @EventHandler
    private void onChangeSlot(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        Bukkit.getScheduler().runTaskLater(Main.getPlugin(), () -> {
            ItemStack itemStack = player.getInventory().getItemInMainHand();
            if (SpellManagementUtil.isWand(itemStack)) {
                SpellManagementUtil.showSelectedSpell(player, itemStack);
            }
        }, 1L);
    }

    @EventHandler
    private void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity attacker = event.getDamager();
        Entity victim = event.getEntity();
        if (victim instanceof LivingEntity) {
            LivingEntity v = (LivingEntity) victim;
            if (v.getMaximumNoDamageTicks() != 0) {
                v.setMaximumNoDamageTicks(0);
            }
        }
        if (attacker instanceof Player
                && !victim.hasMetadata(SpellEffectUtil.CAN_DAMAGE_WITH_WANDS)
                && SpellManagementUtil.isWand(((Player) attacker).getInventory().getItemInMainHand())
        ) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void onEntityDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            Player victim = (Player) event.getEntity();
            EntityDamageEvent dmgEvent = victim.getLastDamageCause();
            if ((dmgEvent instanceof MagicDamageEvent)) {
                playerMagicDamageEventMap.put(victim, (MagicDamageEvent) dmgEvent);
            } else {
                playerMagicDamageEventMap.remove(victim);
            }
        }
    }

    @EventHandler
    private void onPlayerQuit(PlayerQuitEvent event) {
        playerMagicDamageEventMap.remove(event.getPlayer());
    }

    @EventHandler
    private void onBlockBreak(BlockBreakEvent event) {
        if (SpellManagementUtil.isWand(event.getPlayer().getInventory().getItemInMainHand())) {
            event.setCancelled(true);
        }
    }
}

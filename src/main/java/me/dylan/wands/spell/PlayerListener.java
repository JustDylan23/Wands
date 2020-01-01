package me.dylan.wands.spell;

import me.dylan.wands.MouseClickListeners.ClickEvent;
import me.dylan.wands.MouseClickListeners.LeftClickListener;
import me.dylan.wands.MouseClickListeners.RightClickListener;
import me.dylan.wands.events.MagicDamageEvent;
import me.dylan.wands.spell.accessories.ItemTag;
import me.dylan.wands.spell.util.SpellInteractionUtil;
import me.dylan.wands.utils.ItemUtil;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Statistic;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
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
import java.util.Objects;
import java.util.Set;

public class PlayerListener implements Listener, LeftClickListener, RightClickListener {
    private final Map<Player, MagicDamageEvent> playerMagicDamageEventMap = new HashMap<>();

    @Override
    public void onLeftClick(@NotNull ClickEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (ItemTag.IS_WAND.isTagged(itemStack) && SpellInteractionUtil.canUseMagic(player)) {
            SpellType spell = SpellInteractionUtil.getSelectedSpell(itemStack);
            if (spell != null) {
                SpellInteractionUtil.castSpell(player, ItemUtil.getName(itemStack), spell);
            }
        }
    }

    @Override
    public void onRightClick(@NotNull ClickEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = player.getInventory().getItemInMainHand();
        if (ItemTag.IS_WAND.isTagged(itemStack) && SpellInteractionUtil.canUseMagic(player)) {
            SpellInteractionUtil.nextSpell(player, itemStack);
            if (player.getGameMode() != GameMode.ADVENTURE) {
                event.cancel();
            }
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
            Bukkit.getOnlinePlayers().forEach(player -> player.spigot().sendMessage(baseComponents));
            event.setDeathMessage(null);
            Player attacker = magicDamageEvent.getAttacker();
            if (!attacker.equals(victim)) {
                attacker.incrementStatistic(Statistic.PLAYER_KILLS);
                Set<Objective> objectives = Objects.requireNonNull(Objects.requireNonNull(Bukkit.getScoreboardManager())).getMainScoreboard().getObjectivesByCriteria("playerKillCount");
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
        ItemStack itemStack = player.getInventory().getItem(event.getNewSlot());
        if (itemStack != null && ItemTag.IS_WAND.isTagged(itemStack)) {
            SpellInteractionUtil.showSelectedSpell(player, itemStack);
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
        if (ItemTag.IS_WAND.isTagged(event.getPlayer().getInventory().getItemInMainHand())) {
            event.setCancelled(true);
        }
    }
}

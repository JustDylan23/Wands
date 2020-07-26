package me.dylan.wands.spell;

import me.dylan.wands.MouseClickListeners.ClickEvent;
import me.dylan.wands.MouseClickListeners.LeftClickListener;
import me.dylan.wands.MouseClickListeners.RightClickListener;
import me.dylan.wands.WandsPlugin;
import me.dylan.wands.events.MagicDamageEvent;
import me.dylan.wands.spell.accessories.ItemTag;
import me.dylan.wands.spell.spells.AffinityType;
import me.dylan.wands.spell.util.SpellInteractionUtil;
import me.dylan.wands.utils.ItemUtil;
import me.dylan.wands.utils.PlayerUtil;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.jetbrains.annotations.NotNull;

import java.util.*;

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

    @EventHandler
    private void onInventoryClickEvent(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        GameMode gameMode = player.getGameMode();
        if (gameMode == GameMode.SURVIVAL || gameMode == GameMode.ADVENTURE) {
            ItemStack clickedItem = event.getCurrentItem();
            ItemStack cursorItem = event.getCursor();
            if (clickedItem != null
                    && cursorItem != null
                    && clickedItem.getType() != Material.AIR
                    && cursorItem.getType() != Material.AIR
                    && ItemTag.IS_WAND.isTagged(clickedItem)) {
                ItemUtil.getPersistentData(cursorItem, "spell", PersistentDataType.INTEGER).ifPresent(spellId -> {
                    Optional<Integer> wandAffinityType = ItemUtil.getPersistentData(clickedItem, AffinityType.PERSISTENT_DATA_KEY_WAND, PersistentDataType.INTEGER);
                    Optional<int[]> spellAffinityTypes = ItemUtil.getPersistentData(cursorItem, AffinityType.PERSISTENT_DATA_KEY_SCROLL, PersistentDataType.INTEGER_ARRAY);
                    if (wandAffinityType.isPresent() && spellAffinityTypes.isPresent()) {
                        int wandAffinityId = wandAffinityType.get();
                        int[] scrollAffinityIdArray = spellAffinityTypes.get();
                        boolean result = Arrays.stream(scrollAffinityIdArray).anyMatch(value -> value == wandAffinityId);
                        if (!result) {
                            player.sendMessage(WandsPlugin.PREFIX + "§cIncompatible!");
                            event.setCancelled(true);
                            return;
                        }
                    } else return;
                    SpellType spellType = SpellType.getSpellById(spellId);
                    if (spellType != null) {
                        Set<@NotNull SpellType> compound = SpellCompound.getCompound(clickedItem);
                        event.setCancelled(true);
                        if (compound.add(spellType)) {
                            player.setItemOnCursor(null);
                            SpellCompound.apply(compound, clickedItem);
                            player.playSound(player.getLocation(), Sound.BLOCK_RESPAWN_ANCHOR_SET_SPAWN, 1, 1);
                        } else {
                            player.sendMessage(WandsPlugin.PREFIX + "§cAlready bound!");
                        }
                    }
                });
            }
        }
    }
}

package me.dylan.wands.customitems;

import me.dylan.wands.Main;
import me.dylan.wands.MouseClickListeners.ClickEvent;
import me.dylan.wands.MouseClickListeners.RightClickListener;
import me.dylan.wands.miscellaneous.utils.ItemUtil;
import me.dylan.wands.spell.CooldownManager;
import me.dylan.wands.spell.SpellType;
import me.dylan.wands.spell.spells.MortalDraw;
import me.dylan.wands.spell.util.SpellEffectUtil;
import me.dylan.wands.spell.util.SpellInteractionUtil;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataType;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MortalBlade implements Listener, RightClickListener {
    public static final String ID_TAG = "MortalBlade";

    private final Main plugin = Main.getPlugin();
    private final CooldownManager cooldownManager = plugin.getCooldownManager();

    public MortalBlade() {
        plugin.getMouseClickListeners().addRightClickListener(this);
    }

    private boolean hasBlade(Player player) {
        PlayerInventory inventory = player.getInventory();
        return SpellInteractionUtil.canUse(player)
                && ItemUtil.hasPersistentData(inventory.getItemInMainHand(), ID_TAG, PersistentDataType.BYTE);
    }

    @Override
    public void onRightClick(ClickEvent event) {
        Player player = event.getPlayer();
        if (hasBlade(player) && cooldownManager.canCast(player)) {
            cooldownManager.updateLastUsed(player);
            String weaponName = player.getInventory().getItemInMainHand().getItemMeta().getDisplayName();
            List<LivingEntity> entities = SpellEffectUtil.getNearbyLivingEntities(player, player.getLocation(), 6);
            switch (entities.size()) {
                case 0:
                    MortalDraw.draw(player, ThreadLocalRandom.current().nextInt(0, 360), 2, 3, 0, false);
                    player.sendActionBar("§4§l1st form: §0§l§oMortal Draw");
                    return;
                case 1:
                    SpellType.DUAL_DRAW.spellData.getBehavior().cast(player, weaponName);
                    player.sendActionBar("§4§l2nd form: §0§l§oDual Draw");
                    return;
                case 2:
                    SpellType.ONE_MIND.spellData.getBehavior().cast(player, weaponName);
                    player.sendActionBar("§4§l3nd form: §0§l§oOne Mind");
                    return;
                case 3:
                    SpellType.WHIRLWIND_SLASH.spellData.getBehavior().cast(player, weaponName);
                    player.sendActionBar("§4§l4th form: §0§l§oWhirlwind Slash");
                    return;
                case 4:
                    SpellType.FLOATING_PASSAGE.spellData.getBehavior().cast(player, weaponName);
                    player.sendActionBar("§4§l5th form: §0§l§oFloating Passage");
                    return;
                default:
                    SpellType.SPIRAL_CLOUD_PASSAGE.spellData.getBehavior().cast(player, weaponName);
                    player.sendActionBar("§4§lFinal form: §0§l§oSpiral Cloud Passage");
            }
        }
    }
}

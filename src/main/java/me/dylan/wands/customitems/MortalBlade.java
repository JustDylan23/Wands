package me.dylan.wands.customitems;

import me.dylan.wands.Main;
import me.dylan.wands.MouseClickListeners.ClickEvent;
import me.dylan.wands.MouseClickListeners.RightClickListener;
import me.dylan.wands.knockback.KnockBack;
import me.dylan.wands.spell.CooldownManager;
import me.dylan.wands.spell.SpellEffectUtil;
import me.dylan.wands.spell.SpellManagementUtil;
import me.dylan.wands.spell.SpellType;
import me.dylan.wands.spell.spells.MortalDraw;
import me.dylan.wands.util.ItemUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class MortalBlade implements Listener, RightClickListener {
    public static final String ID_TAG = "MortalBlade";

    private final Main plugin = Main.getPlugin();
    private final CooldownManager cooldownManager = plugin.getCooldownManager();
    private KnockBack knockBack = KnockBack.from(0.3f);
    private PotionEffect wither = new PotionEffect(PotionEffectType.WITHER, 40, 1, false, true, false);

    public MortalBlade() {
        plugin.getMouseClickListeners().addRightClickListener(this);
    }

    private boolean hasBlade(Player player) {
        PlayerInventory inventory = player.getInventory();
        return SpellManagementUtil.canUse(player)
                && ItemUtil.hasPersistentData(inventory.getItemInMainHand(), ID_TAG, PersistentDataType.BYTE);
    }

    @EventHandler
    private void onAttack(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            Player player = (Player) event.getDamager();
            if (event.getEntity() instanceof LivingEntity) {
                LivingEntity victim = (LivingEntity) event.getEntity();
                if (hasBlade(player)) {
                    victim.addPotionEffect(wither, true);
                    event.setDamage(8);
                    Location loc = victim.getLocation().add(0, 1.5, 0);
                    World world = loc.getWorld();
                    world.spawnParticle(Particle.REDSTONE, loc, 5, 0.5, 0.5, 0.5, 0, MortalDraw.BLACK, true);
                    world.spawnParticle(Particle.REDSTONE, loc, 2, 0.5, 0.5, 0.5, 0, MortalDraw.RED, true);
                }
            }
        }
    }

    @Override
    public void onRightClick(ClickEvent event) {
        Player player = event.getPlayer();
        Location location = player.getLocation();
        if (hasBlade(player) && cooldownManager.canCast(player)) {
            cooldownManager.updateLastUsed(player);
            String weaponName = player.getInventory().getItemInMainHand().getItemMeta().getDisplayName();
            List<LivingEntity> entities = SpellEffectUtil.getNearbyLivingEntities(player, player.getLocation(), 6);
            switch (entities.size()) {
                case 0:
                    MortalDraw.draw(player, ThreadLocalRandom.current().nextInt(0, 360), 2, entity -> {
                        SpellEffectUtil.damageEffect(player, entity, 3, weaponName);
                        knockBack.apply(entity, location);
                    }, 0, false);
                    return;
                case 1:
                    SpellType.BLADE_CROSS.spellData.getBehaviour().cast(player, weaponName);
                    return;
                case 2:
                    SpellType.ONE_MIND.spellData.getBehaviour().cast(player, weaponName);
                    return;
                case 3:
                    SpellType.WHIRLWIND_SLASH.spellData.getBehaviour().cast(player, weaponName);
                    return;
                case 4:
                    SpellType.FLOATING_PASSAGE.spellData.getBehaviour().cast(player, weaponName);
                    return;
                default:
                    SpellType.SPIRAL_CLOUD_PASSAGE.spellData.getBehaviour().cast(player, weaponName);
            }
        }
    }
}

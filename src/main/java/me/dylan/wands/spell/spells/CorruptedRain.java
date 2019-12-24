package me.dylan.wands.spell.spells;

import me.dylan.wands.ListenerRegistry;
import me.dylan.wands.WandsPlugin;
import me.dylan.wands.config.ConfigHandler;
import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.accessories.SpellInfo;
import me.dylan.wands.spell.accessories.sound.CompoundSound;
import me.dylan.wands.spell.spellbuilders.Behavior;
import me.dylan.wands.spell.spellbuilders.BuildableBehaviour;
import me.dylan.wands.spell.spellbuilders.Spark;
import me.dylan.wands.spell.util.SpellEffectUtil;
import me.dylan.wands.utils.Common;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.AbstractArrow.PickupStatus;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CorruptedRain implements Castable, Listener {
    private final PotionEffect wither = new PotionEffect(PotionEffectType.WITHER, 40, 2, true);
    private final PotionEffect blind = new PotionEffect(PotionEffectType.BLINDNESS, 120, 0, false);
    private final BlockData obsidian = Material.OBSIDIAN.createBlockData();
    private final String tagCorruptedRain = UUID.randomUUID().toString();
    private final ConfigHandler config;

    public CorruptedRain() {
        ListenerRegistry.addListener(this);
        WandsPlugin plugin = JavaPlugin.getPlugin(WandsPlugin.class);
        config = plugin.getConfigHandler();
    }

    @Override
    public Behavior createBehaviour() {
        return Spark.newBuilder(BuildableBehaviour.Target.MULTI)
                .setCastSound(CompoundSound.chain()
                        .add(Sound.ENTITY_ARROW_SHOOT)
                        .add(Sound.BLOCK_STONE_PLACE)
                        .addAll(Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1, 1)
                )
                .setEffectDistance(30)
                .setSpellRelativeEffects(this::spawnArrows)
                .build();
    }

    private void spawnArrows(Location location, SpellInfo spellInfo) {
        location.add(0, 5, 0);
        World world = location.getWorld();
        Location particleLoc = location.clone().add(0, 2, 0);
        world.spawnParticle(Particle.SPELL_MOB, particleLoc, 50, 1, 1, 1, 1, null, true);
        world.spawnParticle(Particle.BLOCK_CRACK, particleLoc, 50, 0.8, 0.3, 0.8, 0, obsidian, true);
        world.spawnParticle(Particle.SMOKE_NORMAL, particleLoc, 70, 1, 1, 1, 0, null, true);
        List<Arrow> arrows = new ArrayList<>();
        BukkitRunnable bukkitRunnable = new BukkitRunnable() {
            int count;

            @Override
            public void run() {
                if (++count >= 14) {
                    cancel();
                    Common.runTaskLater(() -> arrows.forEach(Entity::remove), 40L);
                } else {
                    Location arrowLoc = SpellEffectUtil.randomizeLoc(location, 1.5, 0, 1.5);
                    Arrow arrow = (Arrow) world.spawnEntity(arrowLoc, EntityType.ARROW);
                    arrow.setDamage(6);
                    arrow.setShooter(spellInfo.caster());
                    arrow.setPickupStatus(PickupStatus.DISALLOWED);
                    arrow.addCustomEffect(blind, true);
                    arrow.addCustomEffect(wither, true);
                    arrow.setVelocity(new Vector(SpellEffectUtil.randomize(0.4), -1, SpellEffectUtil.randomize(0.4)));
                    arrow.setMetadata(tagCorruptedRain, Common.getMetadataValueTrue());
                    arrows.add(arrow);
                }
            }
        };
        Common.runTaskTimer(bukkitRunnable, 0, 1);
    }

    @EventHandler
    private void onEntityDamageEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Projectile) {
            Projectile projectile = (Projectile) event.getDamager();
            if (projectile.hasMetadata(tagCorruptedRain)) {
                Player source = (Player) projectile.getShooter();
                if (event.getEntity().equals(source)) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
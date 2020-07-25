package me.dylan.wands.spell.spells.sky;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.spellbuilders.Behavior;
import me.dylan.wands.spell.util.SpellEffectUtil;
import me.dylan.wands.utils.Common;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class ThunderRage implements Castable {
    private final PotionEffect wither = new PotionEffect(PotionEffectType.WITHER, 80, 1, false);
    private final PotionEffect slow = new PotionEffect(PotionEffectType.SLOW, 80, 3, false);

    @Override
    public Behavior createBehaviour() {
        return new Behavior() {
            @Override
            public boolean cast(@NotNull Player player, @NotNull String weapon) {
                Location loc = player.getLocation();
                World world = loc.getWorld();
                world.playSound(loc, Sound.ITEM_TOTEM_USE, 4.0F, 1.0F);
                BukkitRunnable bukkitRunnable = new BukkitRunnable() {
                    int count = 0;

                    @Override
                    public void run() {
                        count++;
                        if (count >= 25) {
                            cancel();
                        }
                        Location currentLoc = player.getLocation();
                        world.spawnParticle(Particle.EXPLOSION_NORMAL, currentLoc, 1, 0.4, 0.3, 0.4, 0.1, null, true);
                        world.spawnParticle(Particle.SMOKE_LARGE, currentLoc, 1, 0.4, 0.3, 0.4, 0.1, null, true);
                        world.spawnParticle(Particle.SMOKE_NORMAL, currentLoc, 1, 0.4, 0.3, 0.4, 0.1, null, true);
                        world.spawnParticle(Particle.FLAME, currentLoc, 1, 0.4, 0.2, 0.4, 0.1, null, true);
                        strikeRandomLightning(player, weapon);
                    }
                };
                Common.runTaskTimer(bukkitRunnable, 0, 2);
                return true;
            }
        };
    }

    private void strikeRandomLightning(Player player, String weapon) {
        Location loc = SpellEffectUtil.getFirstPassableBlockAbove(
                SpellEffectUtil.getFirstGroundBlockUnder(
                        SpellEffectUtil.randomizeLoc(player.getLocation(), 12, 0, 12)
                )
        );
        World world = loc.getWorld();
        world.spigot().strikeLightningEffect(loc, true);
        world.playSound(loc, Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 4, 1);
        world.playSound(loc, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 5, 1);
        world.spawnParticle(Particle.FLAME, loc, 10, 0.2, 0.2, 0.2, 0.1, null, true);
        world.spawnParticle(Particle.CLOUD, loc, 10, 0.2, 0.2, 0.2, 0.1, null, true);
        effectNearbyEntities(loc, player, weapon);
    }

    private void effectNearbyEntities(Location location, Player player, String weapon) {
        SpellEffectUtil.getNearbyLivingEntities(player, location, 3.5D)
                .forEach(livingEntity -> {
                    livingEntity.addPotionEffect(wither);
                    livingEntity.addPotionEffect(slow);
                    livingEntity.setFireTicks(60);
                    SpellEffectUtil.damageEffect(player, livingEntity, 5, weapon);
                });
    }
}
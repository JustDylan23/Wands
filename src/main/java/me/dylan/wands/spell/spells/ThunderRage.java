package me.dylan.wands.spell.spells;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.spellbuilders.Aura;
import me.dylan.wands.spell.spellbuilders.Aura.EffectFrequency;
import me.dylan.wands.spell.spellbuilders.Behavior;
import me.dylan.wands.utils.Common;
import org.bukkit.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ThunderRage implements Castable {
    @Override
    public Behavior createBehaviour() {
        PotionEffect strength = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 300, 1, false);
        PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, 60, 1, false);
        return Aura.newBuilder(EffectFrequency.ONCE)
                .setCastSound(Sound.ITEM_TOTEM_USE)
                .setEntityDamage(7)
                .setSpellEffectRadius(8.0F)
                .setEffectDuration(40)
                .setSpellRelativeEffects((loc, spellInfo) -> {
                    World world = spellInfo.world();
                    world.spawnParticle(Particle.EXPLOSION_NORMAL, loc, 1, 0.4, 0.3, 0.4, 0.1, null, true);
                    world.spawnParticle(Particle.SMOKE_LARGE, loc, 1, 0.4, 0.3, 0.4, 0.1, null, true);
                    world.spawnParticle(Particle.SMOKE_NORMAL, loc, 1, 0.4, 0.3, 0.4, 0.1, null, true);
                    world.spawnParticle(Particle.FLAME, loc, 1, 0.4, 0.2, 0.4, 0.1, null, true);
                })
                .setPotionEffects(
                        new PotionEffect(PotionEffectType.WITHER, 80, 1, false),
                        new PotionEffect(PotionEffectType.SLOW, 80, 3, false)
                )
                .setEntityEffects((entity, spellInfo) -> {
                    entity.setFireTicks(60);
                    World world = entity.getWorld();
                    Location loc = entity.getLocation();
                    world.spawnParticle(Particle.FLAME, loc, 10, 0.2, 0.2, 0.2, 0.1, null, true);
                    world.spawnParticle(Particle.CLOUD, loc, 10, 0.2, 0.2, 0.2, 0.1, null, true);
                    world.spigot().strikeLightningEffect(loc, true);
                    world.playSound(loc, Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 4, 1);
                    world.playSound(loc, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 5, 1);
                    Common.runRepeatableTaskLater(() -> {
                        world.spigot().strikeLightningEffect(loc, true);
                        world.playSound(loc, Sound.ENTITY_LIGHTNING_BOLT_IMPACT, 4, 1);
                        world.playSound(loc, Sound.ENTITY_LIGHTNING_BOLT_THUNDER, 5, 1);
                    }, 3, 3, 3);
                })
                .setReverseAuraEffects(player -> {
                    player.addPotionEffect(strength, true);
                    player.addPotionEffect(speed, true);
                    World world = player.getWorld();
                    Location location = player.getLocation();
                    world.spawnParticle(Particle.LAVA, location, 10, 1, 1, 1, 0, null, true);
                    world.playSound(location, Sound.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.MASTER, 4, 1);
                })
                .build();
    }
}
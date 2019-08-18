package me.dylan.wands.spell.spells;

import me.dylan.wands.spell.SpellData;
import me.dylan.wands.spell.SpellEffectUtil;
import me.dylan.wands.spell.types.Aura;
import me.dylan.wands.spell.types.Aura.EffectFrequency;
import me.dylan.wands.spell.types.Behaviour;
import org.bukkit.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class ThunderRage implements SpellData {
    private final Behaviour behaviour;
    private final PotionEffect wither = new PotionEffect(PotionEffectType.WITHER, 80, 1, false);
    private final PotionEffect slow = new PotionEffect(PotionEffectType.SLOW, 80, 3, false);
    private final PotionEffect strength = new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 300, 1, false);
    private final PotionEffect speed = new PotionEffect(PotionEffectType.SPEED, 60, 1, false);

    public ThunderRage() {
        this.behaviour = Aura.newBuilder(EffectFrequency.ONCE)
                .setCastSound(Sound.ITEM_TOTEM_USE)
                .setEntityDamage(7)
                .setSpellEffectRadius(8.0F)
                .setEffectDuration(40)
                .setSpellRelativeEffects((loc, world) -> {
                    world.spawnParticle(Particle.EXPLOSION_NORMAL, loc, 1, 0.4, 0.3, 0.4, 0.1, null, true);
                    world.spawnParticle(Particle.SMOKE_LARGE, loc, 1, 0.4, 0.3, 0.4, 0.1, null, true);
                    world.spawnParticle(Particle.SMOKE_NORMAL, loc, 1, 0.4, 0.3, 0.4, 0.1, null, true);
                    world.spawnParticle(Particle.FLAME, loc, 1, 0.4, 0.2, 0.4, 0.1, null, true);
                })
                .setEntityEffects(entity -> {
                    entity.addPotionEffect(wither, true);
                    entity.addPotionEffect(slow, true);
                    entity.setFireTicks(60);
                    World world = entity.getWorld();
                    Location loc = entity.getLocation();
                    world.spawnParticle(Particle.FLAME, loc, 10, 0.2, 0.2, 0.2, 0.1, null, true);
                    world.spawnParticle(Particle.CLOUD, loc, 10, 0.2, 0.2, 0.2, 0.1, null, true);
                    world.strikeLightningEffect(loc);
                    SpellEffectUtil.runTaskLater(() -> world.strikeLightningEffect(loc), 3, 3, 3);
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

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }
}
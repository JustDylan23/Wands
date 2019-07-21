package me.dylan.wands.spell.implementations;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.handler.Behaviour;
import me.dylan.wands.spell.handler.RaySpell;
import me.dylan.wands.spell.handler.RaySpell.Target;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public enum BloodStun implements Castable {
    INSTANCE;
    private final PotionEffect slow = new PotionEffect(PotionEffectType.SLOW, 180, 3, false);
    private final Behaviour behaviour;

    BloodStun() {
        this.behaviour = RaySpell.newBuilder(Target.SINGLE)
                .setCastSound(Sound.ENTITY_FIREWORK_ROCKET_BLAST)
                .setEntityDamage(3)
                .setEntityEffects(entity -> {
                    entity.addPotionEffect(slow, true);
                    Location location = entity.getLocation();
                    location.getWorld().playSound(location, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, 4, 1);
                })
                .setRayWidth(1)
                .setSpellRelativeEffects((loc, world) -> {
                    world.spawnParticle(Particle.SMOKE_NORMAL, loc, 2, 0.2, 0.2, 0.2, 0.04, null, true);
                    world.spawnParticle(Particle.DRIP_LAVA, loc, 2, 0.3, 0.3, 0.3, 0.04, null, true);
                    world.spawnParticle(Particle.BLOCK_CRACK, loc, 1, 0.6, 0.7, 0.6, 0.15, Material.REDSTONE_BLOCK.createBlockData(), true);
                }).setEffectDistance(30)
                .build();
    }

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }
}

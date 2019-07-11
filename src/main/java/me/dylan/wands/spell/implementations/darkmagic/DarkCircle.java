package me.dylan.wands.spell.implementations.darkmagic;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.handler.Behaviour;
import me.dylan.wands.spell.handler.CircleSpell;
import me.dylan.wands.spell.handler.CircleSpell.CirclePlacement;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public enum DarkCircle implements Castable {
    INSTANCE;
    private final Behaviour behaviour;

    private final PotionEffect blind = new PotionEffect(PotionEffectType.BLINDNESS, 40, 0, false);

    DarkCircle() {
        this.behaviour = CircleSpell.newBuilder(CirclePlacement.RELATIVE)
                .setCircleRadius(4)
                .setEntityDamage(2)
                .setSpellEffectRadius(4F)
                .setEntityEffects(entity -> {
                    entity.setVelocity(entity.getVelocity().setY(0.5f));
                    entity.getLocation().createExplosion(0);
                    entity.addPotionEffect(blind, true);
                })
                .setMetersPerTick(2)
                .setCastSound(Sound.ENTITY_ENDER_DRAGON_FLAP)
                .setSpellRelativeEffects(loc -> {
                    World world = loc.getWorld();
                    world.spawnParticle(Particle.SMOKE_LARGE, loc, 10, 0.3, 0.3, 0.3, 0.1, null, true);
                    world.spawnParticle(Particle.ENCHANTMENT_TABLE, loc, 8, 0.3, 0.3, 0.3, 0.1, null, true);
                })
                .setImpactSpeed(1.1F)
                .setCircleHeight(1)
                .build();
    }

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }
}
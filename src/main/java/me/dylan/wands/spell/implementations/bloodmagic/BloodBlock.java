package me.dylan.wands.spell.implementations.bloodmagic;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.handler.Behaviour;
import me.dylan.wands.spell.handler.MovingBlockSpell;
import me.dylan.wands.spell.spelleffect.sound.CompoundSound;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;

public enum BloodBlock implements Castable {
    INSTANCE;
    private final Behaviour behaviour;

    BloodBlock() {
        this.behaviour = MovingBlockSpell.newBuilder(Material.REDSTONE_BLOCK)
                .setCastSound(Sound.ENTITY_FIREWORK_ROCKET_BLAST)
                .setBlockRelativeSounds(CompoundSound.chain()
                        .add(Sound.ENTITY_FIREWORK_ROCKET_BLAST, 1, 20)
                        .add(Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, 1, 30, 5)
                        .add(Sound.ENTITY_FIREWORK_ROCKET_TWINKLE_FAR, 1, 40)
                )
                .setHitEffects((loc -> {
                    loc.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 20, 1, 1, 1, 0.1, null, true);
                    loc.getWorld().spawnParticle(Particle.BLOCK_CRACK, loc, 15, 1, 1, 1, 0.15, Material.REDSTONE_BLOCK.createBlockData(), true);
                    loc.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, loc, 0, 0.0, 0.0, 0.0, 0.0, null, true);
                    loc.getWorld().playSound(loc, Sound.ENTITY_GENERIC_EXPLODE, SoundCategory.MASTER, 4.0F, 1.0F);
                }))
                .setSpellRelativeEffects(loc -> {
                    loc.getWorld().spawnParticle(Particle.BLOCK_CRACK, loc, 10, 0.5, 0.5, 0.5, 0.15, Material.REDSTONE_BLOCK.createBlockData(), true);
                    loc.getWorld().spawnParticle(Particle.SMOKE_LARGE, loc, 2, 0.2, 0.2, 0.2, 0.1, null, true);
                })
                .setSpellEffectRadius(4.5F)
                .setAffectedEntityDamage(7)
                .build();
    }

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }
}

package me.dylan.wands.spell.spells;

import me.dylan.wands.Main;
import me.dylan.wands.spell.SpellData;
import me.dylan.wands.spell.types.Behaviour;
import me.dylan.wands.spell.types.Phase;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class CorruptedLaunch implements SpellData {

    private final Behaviour behaviour;

    public CorruptedLaunch() {
        this.behaviour = Phase.newBuilder(Behaviour.Target.MULTI)
                .setSpellEffectRadius(2.8f)
                .setEntityDamage(7)
                .setEntityEffects(entity -> entity.setVelocity(new Vector(0, 1.2, 0)))
                .setSpellRelativeEffects((loc, world) -> {
                    world.spawnParticle(Particle.SMOKE_NORMAL, loc, 15, 0.5, 0.5, 0.5, 0.05, null, true);
                    world.spawnParticle(Particle.SPELL_MOB, loc, 10, 1, 1, 1, 1, null, true);
                    world.spawnParticle(Particle.SPELL_MOB_AMBIENT, loc, 30, 1, 1, 1, 1, null, true);
                    world.playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_BLAST, SoundCategory.MASTER, 4.0F, 1.0F);
                    Bukkit.getScheduler().runTaskLater(Main.getPlugin(), () ->
                            world.playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, SoundCategory.MASTER, 4.0F, 1.0F), 10L);
                })
                .setStagePassCondition(Entity::isOnGround)
                .setEffectsDuringPhase(entity -> {
                    World world = entity.getWorld();
                    Location loc = entity.getLocation();
                    world.spawnParticle(Particle.SMOKE_NORMAL, loc, 1, 0, 0, 0, 0.3, null, true);
                    world.spawnParticle(Particle.SPELL_MOB, loc, 10, 0.6, 0.6, 0.6, 1, null, true);
                    world.spawnParticle(Particle.SPELL_MOB_AMBIENT, loc, 3, 0.6, 0.6, 0.6, 1, null, true);
                })
                .setEffectDistance(30)
                .build();
    }

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }
}

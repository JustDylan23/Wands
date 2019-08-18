package me.dylan.wands.spell.spells;

import me.dylan.wands.Main;
import me.dylan.wands.spell.SpellData;
import me.dylan.wands.spell.types.Behaviour;
import me.dylan.wands.spell.types.Behaviour.Target;
import me.dylan.wands.spell.types.Phase;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

public class Launch implements SpellData {
    private final Behaviour behaviour;

    public Launch() {
        this.behaviour = Phase.newBuilder(Target.MULTI)
                .setSpellEffectRadius(2.8f)
                .setEntityDamage(7)
                .setEntityEffects(entity -> entity.setVelocity(new Vector(0, 1.2, 0)))
                .setSpellRelativeEffects((loc, world) -> {
                    world.spawnParticle(Particle.SPELL_WITCH, loc, 30, 0.6, 0.7, 0.6, 0.4, null, true);
                    world.spawnParticle(Particle.SMOKE_LARGE, loc, 20, 0.2, 0.2, 0.2, 0.2, null, true);
                    world.playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_BLAST, SoundCategory.MASTER, 4.0F, 1.0F);
                    Bukkit.getScheduler().runTaskLater(Main.getPlugin(), () ->
                            world.playSound(loc, Sound.ENTITY_FIREWORK_ROCKET_TWINKLE, SoundCategory.MASTER, 4.0F, 1.0F), 10L);
                })
                .setStagePassCondition(Entity::isOnGround)
                .setEffectsDuringPhase(entity -> {
                    World world = entity.getWorld();
                    Location location = entity.getLocation();
                    world.spawnParticle(Particle.SMOKE_LARGE, location, 1, 0, 0, 0, 0.3, null, true);
                    world.spawnParticle(Particle.PORTAL, location, 10, 0.4, 0.4, 0.4, 0, null, true);
                })
                .setEffectDistance(30)
                .build();
    }

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }
}

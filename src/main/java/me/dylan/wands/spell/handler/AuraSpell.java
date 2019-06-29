package me.dylan.wands.spell.handler;

import me.dylan.wands.Main;
import me.dylan.wands.util.EffectUtil;
import me.dylan.wands.util.Common;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.function.Consumer;

/**
 * AuraSpell is a type of Behaviour which is executed relative to the player.
 * The distance in which entities will be affected can vary. The player itself
 * can also be given effects upon executing the spell that has implemented this behaviour.
 *
 *  setEffectDuration is meant for setting the amount of ticks
 * {@link Builder#setEffectDuration(int)}
 */

public final class AuraSpell extends Behaviour {
    private static int instanceCount = 0;
    private final int effectDuration;
    private final String hasAura;
    private final Consumer<LivingEntity> playerEffects;

    private AuraSpell(Builder builder) {
        super(builder.baseMeta);
        instanceCount++;
        this.effectDuration = builder.effectDuration;
        this.hasAura = "HAS_AURA;ID#" + instanceCount;
        this.playerEffects = builder.playerEffects;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public boolean cast(Player player) {
        castEffects.accept(player.getLocation());
        if (player.hasMetadata(hasAura)) return false;
        player.setMetadata(hasAura, Common.METADATA_VALUE_TRUE);
        playerEffects.accept(player);
        new BukkitRunnable() {
            int count = 0;

            @Override
            public void run() {
                if (++count > effectDuration) {
                    cancel();
                    player.removeMetadata(hasAura, plugin);
                } else {
                    Location loc = player.getLocation();
                    visualEffects.accept(loc);
                    EffectUtil.getNearbyLivingEntities(player, loc, effectRadius)
                            .forEach(entity -> {
                                if (entityDamage != 0) entity.damage(entityDamage);
                                entityEffects.accept(entity);
                            });
                }
            }
        }.runTaskTimer(Main.getPlugin(), 1, 1);
        return true;
    }

    @Override
    public String toString() {
        return super.toString() + "Effect duration: " + effectDuration + " ticks";
    }


    public static final class Builder extends AbstractBuilder<Builder> {
        private int effectDuration;
        private Consumer<LivingEntity> playerEffects = Common.emptyConsumer();

        private Builder() {
        }

        @Override
        Builder self() {
            return this;
        }

        @Override
        public Behaviour build() {
            return new AuraSpell(this);
        }

        public Builder setEffectDuration(int ticks) {
            this.effectDuration = ticks;
            return this;
        }

        public Builder setPlayerEffects(Consumer<LivingEntity> playerEffects) {
            this.playerEffects = playerEffects;
            return this;
        }
    }
}

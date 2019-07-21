package me.dylan.wands.spell.handler;

import me.dylan.wands.Main;
import me.dylan.wands.spell.SpellEffectUtil;
import me.dylan.wands.util.Common;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;
import java.util.function.Consumer;

/**
 * AuraSpell is a type of Behaviour which is executed relative to the player.
 * The distance in which entities will be affected can vary. The player itself
 * can also be given effects upon executing the spell that has implemented this behaviour.
 * <p>
 * setEffectDuration is meant for setting the amount of ticks
 * {@link Builder#setEffectDuration(int)}
 */

public final class AuraSpell extends Behaviour {
    private final EffectFrequency effectFrequency;
    private final int effectDuration;
    private final String AuraUUID;
    private final Consumer<LivingEntity> playerEffects, reverseAuraEffects;

    private AuraSpell(Builder builder) {
        super(builder.baseMeta);
        this.effectFrequency = builder.effectFrequency;
        this.effectDuration = builder.effectDuration;
        this.AuraUUID = UUID.randomUUID().toString();
        this.playerEffects = builder.playerEffects;
        this.reverseAuraEffects = builder.reverseAuraEffects;

        addStringProperty("Aura duration", effectDuration, "ticks");
        addStringProperty("Effect quantity", effectFrequency);
    }

    public static Builder newBuilder(EffectFrequency effectFrequency) {
        return new Builder(effectFrequency);
    }

    @Override
    public boolean cast(Player player, String wandDisplayName) {
        if (player.hasMetadata(AuraUUID)) return false;
        player.setMetadata(AuraUUID, Common.METADATA_VALUE_TRUE);
        castSounds.play(player);
        playerEffects.accept(player);
        new BukkitRunnable() {
            int count = 0;
            boolean repeat = true;
            boolean hasAffected = false;

            @Override
            public void run() {
                if (++count > effectDuration) {
                    cancel();
                    player.removeMetadata(AuraUUID, plugin);
                    if (!hasAffected) {
                        reverseAuraEffects.accept(player);
                    }
                } else {
                    Location loc = player.getLocation();
                    spellRelativeEffects.accept(loc, loc.getWorld());
                    if (repeat) {
                        SpellEffectUtil.getNearbyLivingEntities(player, loc, spellEffectRadius)
                                .forEach(entity -> {
                                    if (effectFrequency == EffectFrequency.ONCE) {
                                        repeat = false;
                                    }
                                    push(entity, loc, player);
                                    SpellEffectUtil.damageEffect(player, entity, affectedEntityDamage, wandDisplayName);
                                    hasAffected = true;
                                    entityEffects.accept(entity);

                                });
                    }
                }
            }
        }.runTaskTimer(Main.getPlugin(), 1, 1);
        return true;
    }

    public enum EffectFrequency {
        ONCE,
        CONSTANT
    }

    public static final class Builder extends AbstractBuilder<Builder> {
        private final EffectFrequency effectFrequency;
        private Consumer<LivingEntity> reverseAuraEffects = Common.emptyConsumer();
        private Consumer<LivingEntity> playerEffects = Common.emptyConsumer();
        private int effectDuration;

        private Builder(EffectFrequency effectFrequency) {
            this.effectFrequency = effectFrequency;
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

        public Builder setReverseAuraEffects(Consumer<LivingEntity> playerEffects) {
            this.reverseAuraEffects = playerEffects;
            return this;
        }
    }
}

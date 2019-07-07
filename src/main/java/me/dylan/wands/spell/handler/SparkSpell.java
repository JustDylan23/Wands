package me.dylan.wands.spell.handler;

import me.dylan.wands.spell.SpellEffectUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public final class SparkSpell extends Behaviour {
    private final int effectDistance;

    private SparkSpell(Builder builder) {
        super(builder.baseMeta);
        this.effectDistance = builder.effectDistance;
        addStringProperty("Effect distance", effectDistance, "meters");
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public boolean cast(Player player) {
        Location loc = SpellEffectUtil.getSpellLocation(effectDistance, player);
        castSounds.play(player);
        spellRelativeEffects.accept(loc);
//        applyEntityEffects(loc, player);
        SpellEffectUtil.getNearbyLivingEntities(player, loc, spellEffectRadius)
                .forEach(entity -> {
                    if (affectedEntityDamage != 0) {
                        EntityDamageByEntityEvent event = new EntityDamageByEntityEvent(player, entity, DamageCause.CUSTOM, 10);
                        entity.setLastDamageCause(event);
                        entity.damage(10);
                        entity.setLastDamageCause(event);
                    }
                });
        return true;
    }

    public static final class Builder extends AbstractBuilder<Builder> {

        private int effectDistance;

        private Builder() {
        }

        @Override
        Builder self() {
            return this;
        }

        @Override
        public Behaviour build() {
            return new SparkSpell(this);
        }

        public Builder setEffectDistance(int effectDistance) {
            this.effectDistance = effectDistance;
            return this;
        }
    }
}

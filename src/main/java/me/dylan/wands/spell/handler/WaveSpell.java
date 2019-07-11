package me.dylan.wands.spell.handler;

import me.dylan.wands.spell.SpellEffectUtil;
import me.dylan.wands.util.Common;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.UUID;

public final class WaveSpell extends Behaviour {
    private final int effectDistance;
    private final int speed;
    private final String tagWaveSpell;

    private WaveSpell(Builder builder) {
        super(builder.baseMeta);
        this.effectDistance = builder.effectDistance;
        this.speed = builder.speed;
        this.tagWaveSpell = UUID.randomUUID().toString();

        addStringProperty("Effect distance", effectDistance, "meters");
        addStringProperty("Meters per tick", speed, "meters");
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    @Override
    public boolean cast(Player player, String wandDisplayName) {
        Vector direction = player.getLocation().getDirection().normalize();
        castSounds.play(player.getLocation().add(direction.clone().multiply(5)));
        Location origin = player.getEyeLocation();
        new BukkitRunnable() {
            int count = 0;

            @Override
            public void run() {
                for (int i = 0; i < speed; i++) {
                    count++;
                    if (count >= effectDistance) cancel();
                    Location loc = origin.add(direction).clone();
                    if (!loc.getBlock().isPassable()) {
                        cancel();
                        return;
                    }
                    spellRelativeEffects.accept(loc);
                    for (LivingEntity entity : SpellEffectUtil.getNearbyLivingEntities(player, loc, spellEffectRadius)) {
                        if (!entity.hasMetadata(tagWaveSpell)) {
                            entity.setMetadata(tagWaveSpell, Common.METADATA_VALUE_TRUE);
                            SpellEffectUtil.damageEffect(player, entity, affectedEntityDamage, wandDisplayName);
                            affectedEntityEffects.accept(entity);
                            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                                if (entity.isValid()) {
                                    entity.removeMetadata(tagWaveSpell, plugin);
                                }
                            }, effectDistance - count);
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 1, 1);
        return true;
    }

    public static final class Builder extends AbstractBuilder<Builder> {
        private int effectDistance;
        private int speed = 1;

        private Builder() {
        }

        @Override
        Builder self() {
            return this;
        }

        public Builder setEffectDistance(int effectDistance) {
            this.effectDistance = effectDistance;
            return this;
        }

        public Builder setMetersPerTick(int speed) {
            this.speed = Math.max(1, speed);
            return this;
        }

        public WaveSpell build() {
            return new WaveSpell(this);
        }
    }
}

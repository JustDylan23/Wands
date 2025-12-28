package me.dylan.wands.spell.spells.corrupt;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.accessories.sound.CompoundSound;
import me.dylan.wands.spell.spellbuilders.Behavior;
import me.dylan.wands.spell.spellbuilders.ShockWave;
import me.dylan.wands.spell.spells.AffinityType;
import me.dylan.wands.spell.util.SpellEffectUtil;
import org.bukkit.Sound;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class CorruptedShockWave implements Castable {
    @Override
    public AffinityType[] getAffinityTypes() {
        return new AffinityType[]{AffinityType.CORRUPTED_MAGIC};
    }

    @Override
    public Behavior createBehaviour() {
        return ShockWave.newBuilder()
                .setCastSound(CompoundSound.chain().add(Sound.ENTITY_WOLF_GROWL, 0.5F).add(Sound.ENTITY_WOLF_WHINE, 0.5F))
                .setWaveRadius(8)
                .setEntityDamage(8)
                .setPotionEffects(
                        new PotionEffect(PotionEffectType.WITHER, 60, 1, false),
                        new PotionEffect(PotionEffectType.SLOWNESS, 60, 2, false)
                )
                .setSpellRelativeEffects((loc, spellInfo) -> SpellEffectUtil.spawnEntityEffect(loc, 5, 0.2, 0.5, 0.2))
                .setExpansionDelay(3)
                .build();
    }
}

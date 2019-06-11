package me.dylan.wands.spell.implementations.icemagic;

import me.dylan.wands.spell.Castable;
import me.dylan.wands.spell.handler.Behaviour;
import me.dylan.wands.spell.handler.SparkSpell;
import org.bukkit.Sound;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public enum ThunderRage implements Castable {
    INSTANCE;
    private final Behaviour behaviour;
    private final PotionEffect wither = new PotionEffect(PotionEffectType.WITHER, 80, 1, false);
    private final PotionEffect slow = new PotionEffect(PotionEffectType.SLOW, 80, 3, false);

    ThunderRage() {
        this.behaviour = SparkSpell.newBuilder()
                .setCastEffects(loc -> loc.getWorld().playSound(loc, Sound.ITEM_TOTEM_USE, 4, 1))
                .setEntityDamage(4)
                .setEffectRadius(8)
                .setEffectDistance(0)
                .setEntityEffects(entity -> entity.getWorld().strikeLightning(entity.getLocation()))
                .build();
    }

    @Override
    public Behaviour getBehaviour() {
        return behaviour;
    }
}
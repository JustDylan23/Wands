package me.dylan.wands;

import me.dylan.wands.spell.BrowseParticle;
import me.dylan.wands.spell.ItemBuilder;
import me.dylan.wands.spell.SpellType;
import me.dylan.wands.spell.accessories.ItemTag;
import me.dylan.wands.spell.spells.AffinityType;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public enum PreSetItem {
    ASSASSINS_DAGGER(
            ItemBuilder.from(Material.MUSIC_DISC_MALL)
                    .named("&dAssassin's &8Dagger")
                    .hideFlags()
                    .tag(ItemTag.IS_DAGGER)
                    .blockWandRegistration()
                    .build()
    ),
    CURSED_BOW(
            ItemBuilder.from(Material.BOW)
                    .named("&cCursed Bow")
                    .unbreakable()
                    .hideFlags()
                    .enchant(Enchantment.INFINITY, 1, true)
                    .tag(ItemTag.IS_CURSED_BOW)
                    .blockWandRegistration()
                    .build()
    ),
    MORTAL_BLADE(
            ItemBuilder.from(Material.NETHERITE_SWORD)
                    .named("&0&k&l|| &cMortal Blade &0&k&l||")
                    .unbreakable()
                    .hideFlags()
                    .withoutAttackDamage()
                    .withSpells(
                            SpellType.MORTAL_CUT,
                            SpellType.ICHIMONJI,
                            SpellType.ONE_MIND,
                            SpellType.SPIRIT_THRUST,
                            SpellType.SPIRIT_FURY,
                            SpellType.SPIRAL_CLOUD_PASSAGE
                    )
                    .withCastAffinity(AffinityType.SWORD_ARTS)
                    .withSpellBrowseParticles(BrowseParticle.MORTAL_BLADE)
                    .build()
    ),
    BEWITCHED_WAND(
            ItemBuilder.from(Material.BLAZE_ROD)
                    .named("&5Bewitched Wand")
                    .glowing()
                    .withSpells(
                            SpellType.COMET,
                            SpellType.SPARK,
                            SpellType.CONFUSE,
                            SpellType.LAUNCH,
                            SpellType.ESCAPE,
                            SpellType.POISON_WAVE
                    )
                    .withCastAffinity(AffinityType.WITCH_MAGIC)
                    .withSpellBrowseParticles(BrowseParticle.WITCH)
                    .build()
    ),
    BLOOD_WAND(
            ItemBuilder.from(Material.NETHER_WART)
                    .named("&cBlood Magic")
                    .withSpells(
                            SpellType.BLOOD_WAVE,
                            SpellType.BLOOD_SPARK,
                            SpellType.BLOOD_EXPLODE,
                            SpellType.BLOOD_STUN,
                            SpellType.BLOOD_BLOCK
                    )
                    .withCastAffinity(AffinityType.BLOOD_MAGIC)
                    .withSpellBrowseParticles(BrowseParticle.PARTICLE_BLOOD)
                    .build()
    ),
    ICE_WAND(
            ItemBuilder.from(Material.BLAZE_ROD)
                    .named("&rIce Wand")
                    .withSpells(
                            SpellType.LIGHTNING_ARROW,
                            SpellType.THUNDER_STRIKE,
                            SpellType.THUNDER_STORM,
                            SpellType.THUNDER_RAGE,
                            SpellType.ICE_FREEZE,
                            SpellType.ICE_AURA,
                            SpellType.ZAP
                    )
                    .withCastAffinity(AffinityType.WEATHER_MAGIC)
                    .withSpellBrowseParticles(BrowseParticle.PARTICLE_ICE)
                    .build()
    ),
    DARK_WAND(
            ItemBuilder.from(Material.STICK)
                    .named("&8&lDark Wand")
                    .withSpells(
                            SpellType.DARK_PULSE,
                            SpellType.DARK_BLOCK,
                            SpellType.DARK_CIRCLE,
                            SpellType.DARK_PUSH,
                            SpellType.DARK_AURA,
                            SpellType.DARK_SPARK,
                            SpellType.SOUL_SEEKER
                    )
                    .withCastAffinity(AffinityType.DARK_MAGIC)
                    .withSpellBrowseParticles(BrowseParticle.PARTICLE_DARK)
                    .build()
    ),

    CELCRIUS_WAND(
            ItemBuilder.from(Material.BLAZE_POWDER)
                    .named("&6Celcrius Wand")
                    .glowing()
                    .withSpells(
                            SpellType.FIRE_COMET,
                            SpellType.FIRE_TWISTER,
                            SpellType.FLAME_WAVE,
                            SpellType.FLAME_SHOCK_WAVE,
                            SpellType.FLAME_THROWER,
                            SpellType.FIRE_SPARK
                    )
                    .withCastAffinity(AffinityType.FIRE_MAGIC)
                    .withSpellBrowseParticles(BrowseParticle.PARTICLE_FIRE)
                    .build()
    ),
    CORRUPTED_WAND(
            ItemBuilder.from(Material.STICK)
                    .named("&cCorrupted Wand")
                    .withSpells(
                            SpellType.CORRUPTED_RAIN,
                            SpellType.CORRUPTED_WAVE,
                            SpellType.CORRUPTED_SHOCK_WAVE,
                            SpellType.CORRUPTED_WOLFS,
                            SpellType.CORRUPTED_LAUNCH,
                            SpellType.CORRUPTED_SPARK,
                            SpellType.POISON_WAVE
                    )
                    .withCastAffinity(AffinityType.CORRUPTED_MAGIC)
                    .withSpellBrowseParticles(BrowseParticle.PARTICLE_CORRUPTED)
                    .build()
    ),
    MEPHI_WAND(
            ItemBuilder.from(Material.IRON_HOE)
                    .named("&2Mephi Wand")
                    .unbreakable()
                    .withSpells(
                            SpellType.MEPHI_AURA,
                            SpellType.MEPHI_AWAY,
                            SpellType.MEPHI_GRAB_WAVE,
                            SpellType.MEPHI_CHOKE,
                            SpellType.MEPHI_SPARK,
                            SpellType.POISON_WAVE
                    )
                    .withCastAffinity(AffinityType.GRAVITY_MAGIC)
                    .withSpellBrowseParticles(BrowseParticle.PARTICLE_MEPHI)
                    .build()
    );

    private final ItemStack itemStack;

    PreSetItem(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    @Override
    public @NotNull String toString() {
        return super.toString().toLowerCase();
    }
}
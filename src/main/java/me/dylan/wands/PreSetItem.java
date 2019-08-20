package me.dylan.wands;

import me.dylan.wands.customitems.AssassinDagger;
import me.dylan.wands.customitems.CursedBow;
import me.dylan.wands.spell.BrowseParticle;
import me.dylan.wands.spell.SpellManagementUtil;
import me.dylan.wands.spell.SpellType;
import me.dylan.wands.spell.WandBuilder;
import me.dylan.wands.util.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

@SuppressWarnings("unused")
public enum PreSetItem {
    ASSASSINS_DAGGER(ItemUtil.getItemStack(() -> {
        ItemStack itemStack = new ItemStack(Material.MUSIC_DISC_MALL);
        SpellManagementUtil.blockRegistration(itemStack);
        ItemUtil.setName(itemStack, "&dAssassin's &8Dagger");
        ItemUtil.setPersistentData(itemStack, AssassinDagger.ID_TAG, PersistentDataType.BYTE, (byte) 1);
        return itemStack;
    })),

    CURSED_BOW(ItemUtil.getItemStack(() -> {
        ItemStack itemStack = new ItemStack(Material.BOW);
        SpellManagementUtil.blockRegistration(itemStack);
        ItemUtil.setName(itemStack, "&cCursed Bow");
        ItemUtil.setItemMeta(itemStack, meta -> {
            meta.setUnbreakable(true);
            meta.addEnchant(Enchantment.ARROW_INFINITE, 1, true);
            meta.addItemFlags(ItemFlag.values());
        });
        ItemUtil.setPersistentData(itemStack, CursedBow.ID_TAG, PersistentDataType.BYTE, (byte) 1);
        return itemStack;
    })),

    MAGIC_WAND(
            WandBuilder.from(Material.BLAZE_ROD)
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
                    .withSpellBrowseParticles(BrowseParticle.DEFAULT)
                    .build()
    ),
    BLOOD_WAND(
            WandBuilder.from(Material.NETHER_WART)
                    .named("&cBlood Magic")
                    .withSpells(
                            SpellType.BLOOD_WAVE,
                            SpellType.BLOOD_SPARK,
                            SpellType.BLOOD_EXPLODE,
                            SpellType.BLOOD_STUN,
                            SpellType.BLOOD_BLOCK
                    )
                    .withSpellBrowseParticles(BrowseParticle.PARTICLE_BLOOD)
                    .build()
    ),

    ICE_WAND(
            WandBuilder.from(Material.BLAZE_ROD)
                    .named("&rIce Wand")
                    .withSpells(
                            SpellType.THUNDER_ARROW,
                            SpellType.THUNDER_STRIKE,
                            SpellType.THUNDER_STORM,
                            SpellType.THUNDER_RAGE,
                            SpellType.ICE_FREEZE,
                            SpellType.ICE_AURA
                    )
                    .withSpellBrowseParticles(BrowseParticle.PARTICLE_ICE)
                    .build()
    ),

    DARK_WAND(
            WandBuilder.from(Material.STICK)
                    .named("&8&lDark Wand")
                    .withSpells(
                            SpellType.DARK_PULSE,
                            SpellType.DARK_BLOCK,
                            SpellType.DARK_CIRCLE,
                            SpellType.DARK_PUSH,
                            SpellType.DARK_AURA,
                            SpellType.DARK_SPARK
                    )
                    .withSpellBrowseParticles(BrowseParticle.PARTICLE_DARK)
                    .build()
    ),

    CELCRIUS_WAND(
            WandBuilder.from(Material.BLAZE_POWDER)
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
                    .withSpellBrowseParticles(BrowseParticle.PARTICLE_FIRE)
                    .build()
    ),

    CORRUPTED_WAND(
            WandBuilder.from(Material.STICK)
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
                    .withSpellBrowseParticles(BrowseParticle.PARTICLE_CORRUPTED)
                    .build()
    ),
    MEPHI_WAND(
            WandBuilder.from(Material.IRON_HOE)
                    .named("&2Mephi Wand")
                    .withSpells(
                            SpellType.MEPHI_AURA,
                            SpellType.MEPHI_AWAY,
                            SpellType.MEPHI_GRAB_WAVE,
                            SpellType.MEPHI_CHOKE,
                            SpellType.MEPHI_SPARK,
                            SpellType.POISON_WAVE
                    )
                    .withSpellBrowseParticles(BrowseParticle.PARTICLE_MEPHI)
                    .build()
    ),
    MORTAL_BLADE(
            WandBuilder.from(Material.IRON_SWORD)
                    .named("&0&k&l|| &cMortal Blade &0&k&l||")
                    .withSpells(
                            SpellType.ONE_MIND,
                            SpellType.BLADE_CROSS,
                            SpellType.WHIRLWIND_SLASH,
                            SpellType.FLOATING_PASSAGE,
                            SpellType.SPIRAL_CLOUD_PASSAGE
                            )
                    .withSpellBrowseParticles(BrowseParticle.MORTAL_BLADE)
                    .build()
    );

    private static final String[] names = Arrays.stream(values()).map(Enum::toString).toArray(String[]::new);

    private final ItemStack itemStack;

    PreSetItem(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public static String[] getNames() {
        return names;
    }

    public static void openInventory(Player player) {
        Inventory inventory = Bukkit.createInventory(null, InventoryType.SHULKER_BOX, Main.PREFIX);
        for (PreSetItem preSetItem : PreSetItem.values()) {
            inventory.addItem(preSetItem.itemStack);
        }
        player.openInventory(inventory);
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    @NotNull
    @Override
    public String toString() {
        return super.toString().toLowerCase();
    }
}
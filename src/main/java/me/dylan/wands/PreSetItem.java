package me.dylan.wands;

import me.dylan.wands.customitems.AssassinDagger;
import me.dylan.wands.customitems.CursedBow;
import me.dylan.wands.spell.BrowseParticle;
import me.dylan.wands.spell.SpellInstance;
import me.dylan.wands.spell.SpellManagementUtil;
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
                            SpellInstance.COMET,
                            SpellInstance.SPARK,
                            SpellInstance.CONFUSE,
                            SpellInstance.LAUNCH,
                            SpellInstance.ESCAPE,
                            SpellInstance.POISON_WAVE
                    )
                    .withSpellBrowseParticles(BrowseParticle.DEFAULT)
                    .build()
    ),
    BLOOD_WAND(
            WandBuilder.from(Material.NETHER_WART)
                    .named("&cBlood Magic")
                    .withSpells(
                            SpellInstance.BLOOD_WAVE,
                            SpellInstance.BLOOD_SPARK,
                            SpellInstance.BLOOD_EXPLODE,
                            SpellInstance.BLOOD_STUN,
                            SpellInstance.BLOOD_BLOCK
                    )
                    .withSpellBrowseParticles(BrowseParticle.PARTICLE_BLOOD)
                    .build()
    ),

    ICE_WAND(
            WandBuilder.from(Material.BLAZE_ROD)
                    .named("&rIce Wand")
                    .withSpells(
                            SpellInstance.THUNDER_ARROW,
                            SpellInstance.THUNDER_STRIKE,
                            SpellInstance.THUNDER_STORM,
                            SpellInstance.THUNDER_RAGE,
                            SpellInstance.ICE_FREEZE,
                            SpellInstance.ICE_AURA
                    )
                    .withSpellBrowseParticles(BrowseParticle.PARTICLE_ICE)
                    .build()
    ),

    DARK_WAND(
            WandBuilder.from(Material.STICK)
                    .named("&8&lDark Wand")
                    .withSpells(
                            SpellInstance.DARK_PULSE,
                            SpellInstance.DARK_BLOCK,
                            SpellInstance.DARK_CIRCLE,
                            SpellInstance.DARK_PUSH,
                            SpellInstance.DARK_AURA,
                            SpellInstance.DARK_SPARK
                    )
                    .withSpellBrowseParticles(BrowseParticle.PARTICLE_DARK)
                    .build()
    ),

    CELCRIUS_WAND(
            WandBuilder.from(Material.BLAZE_POWDER)
                    .named("&6Celcrius Wand")
                    .glowing()
                    .withSpells(
                            SpellInstance.FIRE_COMET,
                            SpellInstance.FIRE_TWISTER,
                            SpellInstance.FLAME_WAVE,
                            SpellInstance.FLAME_SHOCK_WAVE,
                            SpellInstance.FLAME_THROWER,
                            SpellInstance.FIRE_SPARK
                    )
                    .withSpellBrowseParticles(BrowseParticle.PARTICLE_FIRE)
                    .build()
    ),

    CORRUPTED_WAND(
            WandBuilder.from(Material.STICK)
                    .named("&cCorrupted Wand")
                    .withSpells(
                            SpellInstance.CORRUPTED_RAIN,
                            SpellInstance.CORRUPTED_WAVE,
                            SpellInstance.CORRUPTED_SHOCK_WAVE,
                            SpellInstance.CORRUPTED_WOLFS,
                            SpellInstance.CORRUPTED_LAUNCH,
                            SpellInstance.CORRUPTED_SPARK,
                            SpellInstance.POISON_WAVE
                    )
                    .withSpellBrowseParticles(BrowseParticle.PARTICLE_CORRUPTED)
                    .build()
    ),
    MEPHI_WAND(
            WandBuilder.from(Material.IRON_HOE)
                    .named("&2Mephi Wand")
                    .withSpells(
                    SpellInstance.MEPHI_AURA,
                    SpellInstance.MEPHI_AWAY,
                    SpellInstance.MEPHI_GRAB_WAVE,
                    SpellInstance.MEPHI_CHOKE,
                    SpellInstance.MEPHI_SPARK,
                    SpellInstance.POISON_WAVE
            )
                    .withSpellBrowseParticles(BrowseParticle.PARTICLE_MEPHI)
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
        Inventory inventory = Bukkit.createInventory(null, InventoryType.CHEST, Main.PREFIX);
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
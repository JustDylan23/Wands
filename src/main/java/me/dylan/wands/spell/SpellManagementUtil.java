package me.dylan.wands.spell;

import me.dylan.wands.Main;
import me.dylan.wands.config.ConfigurableData;
import me.dylan.wands.spell.types.Behaviour;
import me.dylan.wands.util.ItemUtil;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;

public class SpellManagementUtil {
    private static final ConfigurableData data = Main.getPlugin().getConfigurableData();

    private static final String TAG_SPELL_INDEX = "SpellIndex";
    private static final String TAG_SPELLS_LIST = "Spells";
    private static final String TAG_VERIFIED = "IsWand";
    private static final String TAG_PARTICLE_SPELL_BROWSE = "SpellBrowseParticles";
    private static final String TAG_UNMODIFIABLE = "BlockModification";
    private static final String TAG_BLOCK_REGISTRATION = "BlockRegistering";

    private SpellManagementUtil() {
        throw new UnsupportedOperationException();
    }

    public static boolean canUse(Player player) {
        if (data.doesCastingRequirePermission()) {
            if (!player.hasPermission("wands.use")) {
                player.sendActionBar("§cinsufficient permissions");
                return false;
            }
        }
        return true;
    }

    public static void setAsWand(ItemStack itemStack) {
        if (canBeRegistered(itemStack)) {
            ItemUtil.setPersistentData(itemStack, TAG_VERIFIED, PersistentDataType.BYTE, (byte) 0);
        }
    }

    public static boolean isWand(ItemStack itemStack) {
        return ItemUtil.hasPersistentData(itemStack, TAG_VERIFIED, PersistentDataType.BYTE);
    }

    // todo implement
    @SuppressWarnings("unused")
    public static void undoWand(ItemStack itemStack) {
        ItemUtil.removePersistentData(itemStack, TAG_VERIFIED);
        ItemUtil.removePersistentData(itemStack, TAG_SPELL_INDEX);
        ItemUtil.removePersistentData(itemStack, TAG_SPELLS_LIST);
        ItemUtil.removePersistentData(itemStack, TAG_PARTICLE_SPELL_BROWSE);
    }

    public static void blockRegistration(ItemStack itemStack) {
        ItemUtil.setPersistentData(itemStack, TAG_BLOCK_REGISTRATION, PersistentDataType.BYTE, (byte) 1);
    }

    public static boolean canBeRegistered(ItemStack itemStack) {
        return !ItemUtil.hasPersistentData(itemStack, TAG_BLOCK_REGISTRATION, PersistentDataType.BYTE);
    }

    static void blockModification(ItemStack itemStack) {
        ItemUtil.setPersistentData(itemStack, TAG_UNMODIFIABLE, PersistentDataType.BYTE, (byte) 1);
    }

    static void setSpells(ItemStack itemStack, @NotNull SpellType... spells) {
        SpellCompound compound = new SpellCompound(itemStack);
        compound.add(spells);
        compound.apply(itemStack);
    }

    static void setSpellBrowseParticles(ItemStack itemStack, @NotNull BrowseParticle browseParticle) {
        ItemUtil.setPersistentData(itemStack, TAG_PARTICLE_SPELL_BROWSE, PersistentDataType.STRING, browseParticle.toString());
    }

    private static void setIndex(ItemStack itemStack, int index) {
        ItemUtil.setPersistentData(itemStack, TAG_SPELL_INDEX, PersistentDataType.INTEGER, index);
    }

    private static int getIndex(ItemStack itemStack) {
        return ItemUtil.getPersistentData(itemStack, TAG_SPELL_INDEX, PersistentDataType.INTEGER)
                .orElse(0);
    }

    private static Optional<BrowseParticle> getSpellBrowseParticle(ItemStack itemStack) {
        Optional<String> stringOptional = ItemUtil.getPersistentData(itemStack, TAG_PARTICLE_SPELL_BROWSE, PersistentDataType.STRING);
        if (stringOptional.isPresent()) {
            try {
                return Optional.of(BrowseParticle.valueOf(stringOptional.get()));
            } catch (IllegalArgumentException ignored) {
            }
        }
        return Optional.empty();
    }

    @Nullable
    private static SpellType getSelectedSpell(ItemStack itemStack) {
        SpellCompound compound = new SpellCompound(itemStack);
        if (compound.isEmpty()) return null;
        int index = getIndex(itemStack);
        if (index <= compound.size()) {
            return compound.get(index);
        } else {
            setIndex(itemStack, 0);
            return compound.get(0);
        }
    }

    static void nextSpell(Player player, ItemStack itemStack) {
        int index = getIndex(itemStack);
        List<SpellType> spells = new SpellCompound(itemStack).getSpells();
        int length = spells.size();
        if (length == 0) {
            player.sendActionBar(Main.PREFIX + "No spells are bound!");
            return;
        }
        length--;

        if (player.isSneaking()) {
            setIndex(itemStack, index >= 1 ? --index : length);
        } else {
            setIndex(itemStack, index < length ? ++index : 0);
        }

        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.MASTER, 0.5F, 0.5F);
        player.sendActionBar("§6Current spell: §7§l" + spells.get(index).spellData.getDisplayName());
        getSpellBrowseParticle(itemStack).ifPresent(particle -> particle.spawn(player.getLocation()));
    }

    static void showSelectedSpell(Player player, ItemStack itemStack) {
        SpellType spell = getSelectedSpell(itemStack);
        if (spell != null) {
            player.sendActionBar("§6Current spell: §7§l" + spell.spellData.getDisplayName());
        }
    }

    static void castSpell(@NotNull Player player, ItemStack itemStack) {
        CooldownManager cooldownManager = Main.getPlugin().getCooldownManager();
        if (cooldownManager.canCast(player)) {
            SpellType spell = getSelectedSpell(itemStack);
            if (spell != null) {
                Behaviour behaviour = spell.spellData.getBehaviour();
                if (behaviour != null) {
                    if (behaviour.cast(player, itemStack.getItemMeta().getDisplayName())) {
                        cooldownManager.updateLastUsed(player);
                    }
                } else {
                    player.sendActionBar("Not yet implemented!");
                }
            } else {
                player.sendActionBar(Main.PREFIX + "No spells are bound!");
            }
        }
    }
}

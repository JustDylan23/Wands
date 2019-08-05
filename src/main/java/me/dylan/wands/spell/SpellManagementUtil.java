package me.dylan.wands.spell;

import me.dylan.wands.Main;
import me.dylan.wands.config.ConfigurableData;
import me.dylan.wands.spell.types.Base;
import me.dylan.wands.util.ItemUtil;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;

//@SuppressWarnings("WeakerAccess")
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

    private static boolean canBeModified(ItemStack itemStack, Player player) {
        if (player.isOp() || ItemUtil.hasPersistentData(itemStack, TAG_UNMODIFIABLE, PersistentDataType.BYTE)) {
            return true;
        } else {
            player.sendMessage(Main.PREFIX + itemStack.getItemMeta().getDisplayName() + "§r couldn't be modified");
            return false;
        }
    }

    static void setSpells(ItemStack itemStack, @NotNull SpellInstance... spellInstances) {
        StringJoiner stringJoiner = new StringJoiner(", ");
        for (SpellInstance spellInstance : spellInstances) {
            stringJoiner.add(spellInstance.toString());
        }
        ItemUtil.setPersistentData(itemStack, TAG_SPELLS_LIST, PersistentDataType.STRING, stringJoiner.toString());
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
    private static String getSelectedSpell(ItemStack itemStack) {
        List<String> spells = SpellCompoundUtil.getSpells(itemStack);
        if (spells.isEmpty()) return null;
        int index = getIndex(itemStack);
        if (index <= spells.size()) {
            return spells.get(index);
        } else {
            setIndex(itemStack, 0);
            return spells.get(0);
        }
    }

    private static void removeCorruptedSpell(@NotNull Player player, ItemStack itemStack, String spell) {
        player.sendMessage(Main.PREFIX + "The spell that you tried to cast does not exist,\n" +
                "it was removed from your wand");
        SpellCompoundUtil.removeSpell(itemStack, spell, player, true);
    }

    static void nextSpell(Player player, ItemStack itemStack) {
        int index = getIndex(itemStack);
        List<String> spells = SpellCompoundUtil.getSpells(itemStack);
        int length = spells.size();
        if (length == 0) {
            player.sendActionBar(Main.PREFIX + "No spells are bound!");
            return;
        }
        length--;

        if (player.isSneaking()) {
            index = (index >= 1) ? --index : length;
        } else {
            index = (index < length) ? ++index : 0;
        }

        setIndex(itemStack, index);
        String spell = spells.get(index);

        // returns null when the spell stored on the item isn't a SpellType
        Castable castable = SpellInstance.getSpell(spell);
        if (castable != null) {
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.MASTER, 0.5F, 0.5F);
            player.sendActionBar("§6Current spell: §7§l" + castable.getDisplayName());
            getSpellBrowseParticle(itemStack).ifPresent(particle -> particle.spawn(player.getLocation()));
        } else {
            removeCorruptedSpell(player, itemStack, spell);
        }
    }

    static void showSelectedSpell(Player player, ItemStack itemStack) {
        String spell = getSelectedSpell(itemStack);
        if (spell != null) {
            Castable castable = SpellInstance.getSpell(spell);
            if (castable != null) {
                player.sendActionBar("§6Current spell: §7§l" + castable.getDisplayName());
            }
        }
    }

    static void castSpell(@NotNull Player player, ItemStack itemStack) {
        CooldownManager cooldownManager = CooldownManager.INSTANCE;
        if (cooldownManager.canCast(player)) {
            String spell = getSelectedSpell(itemStack);
            if (spell != null) {
                // returns null when the spell stored on the item isn't a SpellType
                Castable castable = SpellInstance.getSpell(spell);
                if (castable != null) {
                    Base baseType = castable.getBaseType();
                    if (baseType != null) {
                        if (baseType.cast(player, itemStack.getItemMeta().getDisplayName())) {
                            cooldownManager.updateLastUsed(player);
                        }
                    } else {
                        player.sendActionBar("No functionality found!");
                    }
                } else {
                    removeCorruptedSpell(player, itemStack, spell);
                }
            } else {
                player.sendActionBar(Main.PREFIX + "No spells are bound!");
            }
        }
    }

    public static class SpellCompoundUtil {
        public static List<String> getSpells(ItemStack itemStack) {
            return ItemUtil.getPersistentData(itemStack, TAG_SPELLS_LIST, PersistentDataType.STRING)
                    .filter(s -> !s.isEmpty())
                    .map(s -> new ArrayList<>(Arrays.asList(s.split(", "))))
                    .orElseGet(ArrayList::new);
        }

        public static boolean containsSpell(ItemStack itemStack, @NotNull String spell) {
            return getSpells(itemStack).contains(spell.toUpperCase());
        }

        public static boolean removeSpell(ItemStack itemStack, String spell, Player player, boolean override) {
            boolean b = override || canBeModified(itemStack, player);
            if (b) {
                spell = spell.toUpperCase();
                List<String> spells = getSpells(itemStack);
                if (spells.isEmpty()) return false;
                spells.remove(spell);
                if (!spells.isEmpty()) {
                    StringJoiner stringJoiner = new StringJoiner(", ");
                    spells.forEach(stringJoiner::add);
                    ItemUtil.setPersistentData(itemStack, TAG_SPELLS_LIST, PersistentDataType.STRING, stringJoiner.toString());
                } else {
                    clearSpells(itemStack, player, false);
                }
            }
            return b;
        }

        public static boolean addSpell(ItemStack itemStack, @NotNull SpellInstance spellInstance, Player player, boolean override) {
            boolean b = override || canBeModified(itemStack, player);
            if (b) {
                Optional<String> spells = ItemUtil.getPersistentData(itemStack, TAG_SPELLS_LIST, PersistentDataType.STRING);
                String rawList = spells
                        .map(s -> s + ", " + spellInstance)
                        .orElseGet(spellInstance::toString);
                ItemUtil.setPersistentData(itemStack, TAG_SPELLS_LIST, PersistentDataType.STRING, rawList);
            }
            return b;
        }

        public static boolean clearSpells(ItemStack itemStack, Player player, boolean override) {
            boolean b = override || canBeModified(itemStack, player);
            if (b) {
                ItemUtil.removePersistentData(itemStack, TAG_SPELLS_LIST);
            }
            return b;
        }

        public static boolean addAllSpells(ItemStack itemStack, Player player, boolean override) {
            boolean b = override || canBeModified(itemStack, player);
            if (b) {
                StringJoiner stringJoiner = new StringJoiner(", ");
                for (SpellInstance spellInstance : SpellInstance.values()) {
                    stringJoiner.add(spellInstance.toString());
                }
                ItemUtil.setPersistentData(itemStack, TAG_SPELLS_LIST, PersistentDataType.STRING, stringJoiner.toString());
            }
            return b;
        }
    }
}

package me.dylan.wands.spell;

import me.dylan.wands.Main;
import me.dylan.wands.spell.handler.Behaviour;
import me.dylan.wands.util.ItemUtil;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Contract;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

@SuppressWarnings("WeakerAccess")
public class SpellManagementUtil {

    private static final String TAG_SPELL_INDEX = "SpellIndex";
    private static final String TAG_SPELLS_LIST = "Spells";
    private static final String TAG_VERIFIED = "IsWand";
    private static final String TAG_PARTICLE_SPELL_BROWSE = "SpellBrowseParticles";

    @Contract(value = " -> fail", pure = true)
    private SpellManagementUtil() {
        throw new UnsupportedOperationException();
    }

    public static void setAsWand(@Nonnull ItemStack itemStack) {
        ItemUtil.setPersistentData(itemStack, TAG_VERIFIED, PersistentDataType.BYTE, (byte) 0);
    }

    public static void undoWand(@Nonnull ItemStack itemStack) {
        ItemUtil.removePersistentData(itemStack, TAG_VERIFIED);
        ItemUtil.removePersistentData(itemStack, TAG_SPELL_INDEX);
        ItemUtil.removePersistentData(itemStack, TAG_SPELLS_LIST);
        ItemUtil.removePersistentData(itemStack, TAG_PARTICLE_SPELL_BROWSE);

    }

    public static void setSpells(@Nonnull ItemStack itemStack, @Nonnull SpellType... spellTypes) {
        StringJoiner stringJoiner = new StringJoiner(", ");
        for (SpellType spellType : spellTypes) {
            stringJoiner.add(spellType.toString());
        }
        ItemUtil.setPersistentData(itemStack, TAG_SPELLS_LIST, PersistentDataType.STRING, stringJoiner.toString());
    }

    public static void setSpellBrowseParticles(@Nonnull ItemStack itemStack, @Nonnull BrowseParticle browseParticle) {
        ItemUtil.setPersistentData(itemStack, TAG_PARTICLE_SPELL_BROWSE, PersistentDataType.STRING, browseParticle.toString());
    }

    private static void setIndex(@Nonnull ItemStack itemStack, int index) {
        ItemUtil.setPersistentData(itemStack, TAG_SPELL_INDEX, PersistentDataType.INTEGER, index);
    }

    public static boolean isWand(@Nonnull ItemStack itemStack) {
        return ItemUtil.hasPersistentData(itemStack, TAG_VERIFIED, PersistentDataType.BYTE);
    }

    private static int getIndex(@Nonnull ItemStack itemStack) {
        return ItemUtil.getPersistentData(itemStack, TAG_SPELL_INDEX, PersistentDataType.INTEGER)
                .orElse(0);
    }

    public static Optional<BrowseParticle> getSpellBrowseParticle(@Nonnull ItemStack itemStack) {
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
    private static String getSelectedSpell(@Nonnull ItemStack itemStack) {
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

    private static void removeCorruptedSpell(@Nonnull Player player, @Nonnull ItemStack itemStack, @Nonnull String spell) {
        player.sendMessage(Main.PREFIX + "The spell that you tried to cast does not exist,\n" +
                "it was removed from your wand");
        SpellCompoundUtil.removeSpell(itemStack, spell);
    }

    static void nextSpell(@Nonnull Player player, @Nonnull ItemStack itemStack) {
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
        Castable castable = SpellType.getSpell(spell);
        if (castable != null) {
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.MASTER, 0.5F, 0.5F);
            player.sendActionBar("§6Current spell: §7§l" + castable.getDisplayName());
            getSpellBrowseParticle(itemStack).ifPresent(particle -> particle.spawn(player.getLocation()));
        } else {
            removeCorruptedSpell(player, itemStack, spell);
        }
    }

    static boolean castSpell(@Nonnull Player player, @Nonnull ItemStack itemStack) {
        String spell = getSelectedSpell(itemStack);
        if (spell != null) {
            // returns null when the spell stored on the item isn't a SpellType
            Castable castable = SpellType.getSpell(spell);
            if (castable != null) {
                Behaviour behaviour = castable.getBehaviour();
                if (behaviour != null) {
                    return behaviour.cast(player, itemStack.getItemMeta().getDisplayName());
                } else {
                    player.sendActionBar("No functionality found!");
                }
            } else {
                removeCorruptedSpell(player, itemStack, spell);
            }
        } else {
            player.sendActionBar(Main.PREFIX + "No spells are bound!");
        }
        return false;
    }

    public static class SpellCompoundUtil {
        public static List<String> getSpells(@Nonnull ItemStack itemStack) {
            return ItemUtil.getPersistentData(itemStack, TAG_SPELLS_LIST, PersistentDataType.STRING)
                    .filter(s -> !s.isEmpty())
                    .map(s -> new ArrayList<>(Arrays.asList(s.split(", "))))
                    .orElseGet(ArrayList::new);
        }

        public static boolean containsSpell(ItemStack itemStack, String spell) {
            return getSpells(itemStack).contains(spell.toUpperCase());
        }

        public static void removeSpell(ItemStack itemStack, String spell) {
            spell = spell.toUpperCase();
            List<String> spells = getSpells(itemStack);
            if (spells.isEmpty()) return;
            spells.remove(spell);
            if (!spells.isEmpty()) {
                StringJoiner stringJoiner = new StringJoiner(", ");
                spells.forEach(stringJoiner::add);
                ItemUtil.setPersistentData(itemStack, TAG_SPELLS_LIST, PersistentDataType.STRING, stringJoiner.toString());
            } else {
                clearSpells(itemStack);
            }
        }

        public static void addSpell(ItemStack itemStack, SpellType spellType) {
            Optional<String> spells = ItemUtil.getPersistentData(itemStack, TAG_SPELLS_LIST, PersistentDataType.STRING);
            String rawList = spells
                    .map(s -> s + ", " + spellType)
                    .orElseGet(spellType::toString);
            ItemUtil.setPersistentData(itemStack, TAG_SPELLS_LIST, PersistentDataType.STRING, rawList);
        }


        public static void clearSpells(ItemStack itemStack) {
            ItemUtil.removePersistentData(itemStack, TAG_SPELLS_LIST);
        }

        public static void addAllSpells(ItemStack itemStack) {
            StringJoiner stringJoiner = new StringJoiner(", ");
            for (SpellType spellType : SpellType.values()) {
                stringJoiner.add(spellType.toString());
            }
            ItemUtil.setPersistentData(itemStack, TAG_SPELLS_LIST, PersistentDataType.STRING, stringJoiner.toString());
        }
    }
}

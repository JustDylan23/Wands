package me.dylan.wands.spell;

import me.dylan.wands.Main;
import me.dylan.wands.spell.handler.Behaviour;
import me.dylan.wands.util.ItemUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.Contract;

import java.util.Optional;
import java.util.StringJoiner;

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

    public static void setAsWand(ItemStack itemStack) {
        ItemUtil.setPersistentData(itemStack, TAG_VERIFIED, PersistentDataType.BYTE, (byte) 0);
    }

    public static void setSpells(ItemStack itemStack, SpellType... spellTypes) {
        StringJoiner stringJoiner = new StringJoiner(";");
        for (SpellType spellType : spellTypes) {
            stringJoiner.add(spellType.toString());
        }
        ItemUtil.setPersistentData(itemStack, TAG_SPELLS_LIST, PersistentDataType.STRING, stringJoiner.toString());
    }

    public static void setSpellBrowseParticles(ItemStack itemStack, BrowseParticle browseParticle) {
        ItemUtil.setPersistentData(itemStack, TAG_PARTICLE_SPELL_BROWSE, PersistentDataType.STRING, browseParticle.toString());
    }

    private static void setIndex(ItemStack itemStack, int index) {
        ItemUtil.setPersistentData(itemStack, TAG_SPELL_INDEX, PersistentDataType.INTEGER, index);
    }

    public static boolean isWand(ItemStack itemStack) {
        return ItemUtil.hasPersistentData(itemStack, TAG_VERIFIED, PersistentDataType.BYTE);
    }

    private static int getIndex(ItemStack itemStack) {
        return ItemUtil.getPersistentData(itemStack, TAG_SPELL_INDEX, PersistentDataType.INTEGER)
                .orElse(0);
    }

    public static Optional<BrowseParticle> getSpellBrowseParticle(ItemStack itemStack) {
        Optional<String> stringOptional = ItemUtil.getPersistentData(itemStack, TAG_PARTICLE_SPELL_BROWSE, PersistentDataType.STRING);
        if (stringOptional.isPresent()) {
            try {
                return Optional.of(BrowseParticle.valueOf(stringOptional.get()));
            } catch (IllegalArgumentException e) {
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    private static Castable[] getSpells(ItemStack itemStack) {
        Optional<String> spells = ItemUtil.getPersistentData(itemStack, TAG_SPELLS_LIST, PersistentDataType.STRING);
        return spells.map(SpellType::getSpells).orElseGet(() -> new Castable[]{});
    }

    private static Optional<Castable> getSelectedSpell(ItemStack itemStack) {
        Castable[] castables = getSpells(itemStack);
        int index = getIndex(itemStack);
        if (index < castables.length) {
            return Optional.of(castables[index]);
        }
        setIndex(itemStack, 0);
        return Optional.empty();
    }

    static void nextSpell(Player player, ItemStack itemStack) {
        int index = getIndex(itemStack);
        Castable[] spells = getSpells(itemStack);
        int length = spells.length;
        length--;

        if (player.isSneaking()) {
            index = (index >= 1) ? --index : length;
        } else {
            index = (index < length) ? ++index : 0;
        }

        setIndex(itemStack, index);

        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, SoundCategory.MASTER, 0.5F, 0.5F);
        player.sendActionBar("§6Current spell: §7§l" + spells[index].getDisplayName());

        getSpellBrowseParticle(itemStack).ifPresent(particle -> particle.spawn(player.getLocation()));

    }

    static boolean castSpell(Player player, ItemStack itemStack) {
        Optional<Castable> spell = getSelectedSpell(itemStack);
        if (spell.isPresent()) {
            Behaviour behaviour = spell.get().getBehaviour();
            if (behaviour == null) {
                player.sendActionBar("No functionality found!");
                return false;
            }
            spell.get().getDisplayName();
            return behaviour.cast(player, itemStack.getItemMeta().getDisplayName());
        }
        player.sendActionBar(Main.PREFIX + "No spells are bound!");
        return false;
    }
}

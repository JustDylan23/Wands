package me.dylan.wands.spell;

import me.dylan.wands.spell.spellbuilders.Behavior;
import me.dylan.wands.spell.spells.AffinityType;
import me.dylan.wands.utils.Common;
import org.bukkit.Material;

public interface Castable {
    Behavior createBehaviour();

    AffinityType[] getAffinityTypes();

    default String getDisplayName() {
        return Common.pascalCaseToWords(getClass().getSimpleName());
    }

    default Material getMaterial() {
        return Material.MOJANG_BANNER_PATTERN;
    }

    default CastType getCastType() {
        return CastType.SPELL;
    }

    enum CastType {
        SPELL("Spell"),
        SWORD_SKILL("Skill");

        private final String displayName;

        CastType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }
}
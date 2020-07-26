package me.dylan.wands.spell.spells;

import me.dylan.wands.WandsPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public enum AffinityType {
    SWORD_ARTS(1, "Sword Arts"),
    BLOOD_MAGIC(2, "Blood Magic"),
    DARK_MAGIC(3, "Dark Magic"),
    FIRE_MAGIC(4, "Fire Magic"),
    GRAVITY_MAGIC(5, "Gravity Magic"),
    WEATHER_MAGIC(6, "Weather Magic"),
    WITCH_MAGIC(7, "Witch Magic"),
    CORRUPTED_MAGIC(8, "Corrupted Magic");

    private static final Map<Integer, AffinityType> AFFINITY_TYPE_MAP = new HashMap<>();
    public static final String PERSISTENT_DATA_KEY_SCROLL = "ScrollAffinityType";
    public static final String PERSISTENT_DATA_KEY_WAND = "WandAffinityType";

    static {
        for (AffinityType value : values()) {
            AffinityType overridden = AFFINITY_TYPE_MAP.get(value.id);
            if (overridden != null) {
                WandsPlugin.warn(overridden + " has been overridden by " + value);
            }
            AFFINITY_TYPE_MAP.put(value.id, value);
        }
    }

    private final int id;
    private final String displayName;

    AffinityType(int id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    @Nullable
    public static AffinityType getAffinityTypeById(int id) {
        return AFFINITY_TYPE_MAP.get(id);
    }

    public int getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }
}

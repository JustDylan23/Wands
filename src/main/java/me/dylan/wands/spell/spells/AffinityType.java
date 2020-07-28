package me.dylan.wands.spell.spells;

public enum AffinityType {
    SWORD_ARTS(1, "Sword Arts"),
    BLOOD_MAGIC(2, "Blood Magic"),
    DARK_MAGIC(3, "Dark Magic"),
    FIRE_MAGIC(4, "Fire Magic"),
    GRAVITY_MAGIC(5, "Gravity Magic"),
    WEATHER_MAGIC(6, "Weather Magic"),
    WITCH_MAGIC(7, "Witch Magic"),
    CORRUPTED_MAGIC(8, "Corrupted Magic");

    public static final String PERSISTENT_DATA_KEY_SCROLL = "ScrollAffinityType";
    public static final String PERSISTENT_DATA_KEY_WAND = "WandAffinityType";

    private final int id;
    private final String displayName;

    AffinityType(int id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public int getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }
}

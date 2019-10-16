package me.dylan.wands.spell.spellbuilders;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.StringJoiner;

public abstract class Behavior {
    private final List<String> props = new ArrayList<>();
    private int tweakedCooldown;

    protected Behavior() {
    }

    void addPropertyInfo(String key, Object value) {
        addPropertyInfo(key, value, "");
    }

    void addPropertyInfo(@NotNull String key, @NotNull Object value, String unit) {
        props.add("§6" + key.substring(0, 1).toUpperCase() + key.substring(1).toLowerCase() + ":§r " + value.toString().toLowerCase() + " " + unit);
    }

    public abstract boolean cast(@NotNull Player player, @NotNull String weaponName);

    public void setCooldown(int seconds) {
        this.tweakedCooldown = seconds;
    }

    public int getTweakedCooldown() {
        return tweakedCooldown;
    }

    @Override
    public final String toString() {
        StringJoiner stringJoiner = new StringJoiner("\n");
        Collections.sort(props);
        for (String prop : props) {
            stringJoiner.add(prop);
        }
        return stringJoiner.toString();
    }
}

package me.dylan.wands.config;

import me.dylan.wands.ListenerRegistry;
import me.dylan.wands.WandsPlugin;
import me.dylan.wands.spell.SpellType;
import me.dylan.wands.spell.types.Behavior;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

public class ConfigurableData {
    private static final String ALLOW_MAGIC_USE_KEY = "allow-magic-use";
    private static final String ALLOW_SELF_HARM = "allow-self-harm";
    private static final String MAGIC_COOLDOWN_TIME_KEY = "magic-cooldown-time";
    private static final String spellsRequirePermission = "casting-requires-permission";

    private final ListenerRegistry listenerRegistry;
    private boolean isMagicUseAllowed;
    private boolean isSelfHarmAllowed;
    private int magicCooldownTime;
    private boolean doesSpellCastingRequirePermission;

    public ConfigurableData(ListenerRegistry listenerRegistry) {
        this.listenerRegistry = listenerRegistry;
        mapConfig();
    }

    private void mapConfig() {
        this.isMagicUseAllowed = ConfigUtil.getBoolean(ALLOW_MAGIC_USE_KEY);
        this.isSelfHarmAllowed = ConfigUtil.getBoolean(ALLOW_SELF_HARM);
        this.magicCooldownTime = ConfigUtil.getInt(MAGIC_COOLDOWN_TIME_KEY);
        this.doesSpellCastingRequirePermission = ConfigUtil.getBoolean(spellsRequirePermission);

        FileConfiguration config = ConfigUtil.getConfig();
        for (SpellType spellType : SpellType.values()) {
            Behavior behavior = spellType.behavior;

            int cooldown = config.getInt(spellType.configKey + ".cooldown");
            int correctedCooldown = Math.max(0, Math.min(99, cooldown));
            if (correctedCooldown == cooldown) {
                behavior.setCooldown(cooldown);
            } else {
                WandsPlugin.log("at: " + spellType.configKey + ".cooldown");
                WandsPlugin.log("found: " + cooldown);
                WandsPlugin.log("to: " + correctedCooldown);
                tweakCooldown(spellType, correctedCooldown);
            }

            int damage = config.getInt(spellType.configKey + ".damage");
            int correctedDamage = Math.max(-behavior.entityDamage, Math.min(99, damage));
            if (damage == correctedDamage) {
                behavior.setDamage(damage);
            } else {
                WandsPlugin.log("at: " + spellType.configKey + ".damage");
                WandsPlugin.log("found: " + damage);
                WandsPlugin.log("to: " + correctedDamage);
                tweakDamage(spellType, correctedDamage);
            }

            behavior.setDamage(config.getInt(spellType.configKey + ".damage"));
        }
    }

    public void reload() {
        ConfigUtil.reloadConfig();
        mapConfig();
    }

    public void tweakCooldown(@NotNull SpellType spellType, int time) {
        ConfigUtil.set(spellType.configKey + ".cooldown", time);
        spellType.behavior.setCooldown(time);
    }

    public void tweakDamage(@NotNull SpellType spellType, int damage) {
        ConfigUtil.set(spellType.configKey + ".damage", damage);
        spellType.behavior.setDamage(damage);
    }

    public void allowMagicUse(boolean value) {
        if (this.isMagicUseAllowed != value) {
            this.isMagicUseAllowed = value;
            ConfigUtil.set(ALLOW_MAGIC_USE_KEY, value);
            if (value) {
                listenerRegistry.enableListeners();
            } else {
                listenerRegistry.disableListeners();
            }
        }
    }

    public boolean isMagicUseAllowed() {
        return isMagicUseAllowed;
    }

    public int getMagicCooldownTime() {
        return magicCooldownTime;
    }

    public void setMagicCooldownTime(int magicCooldownTime) {
        this.magicCooldownTime = magicCooldownTime;
        ConfigUtil.set(MAGIC_COOLDOWN_TIME_KEY, magicCooldownTime);
    }

    public void allowSelfHarm(boolean value) {
        this.isSelfHarmAllowed = value;
        ConfigUtil.set(ALLOW_SELF_HARM, value);
    }

    public boolean isSelfHarmAllowed() {
        return isSelfHarmAllowed;
    }

    public void requirePermissionForCasting(boolean value) {
        this.doesSpellCastingRequirePermission = value;
        ConfigUtil.set(spellsRequirePermission, value);
    }

    public boolean doesCastingRequirePermission() {
        return doesSpellCastingRequirePermission;
    }
}


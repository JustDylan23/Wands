package me.dylan.wands.config;

import me.dylan.wands.ListenerRegistry;
import me.dylan.wands.spell.SpellType;
import org.jetbrains.annotations.NotNull;

public class ConfigurableData {
    private static final String ALLOW_MAGIC_USE_KEY = "Allow Magic Use";
    private static final String ALLOW_SELF_HARM = "Allow Self Harm";
    private static final String MAGIC_COOLDOWN_TIME_KEY = "Magic Cooldown Time";
    private static final String spellsRequirePermission = "Casting Requires Permission";

    private final ListenerRegistry listenerRegistry;
    private boolean isMagicUseAllowed;
    private boolean isSelfHarmAllowed;
    private int magicCooldownTime;
    private boolean doesSpellCastingRequirePermission;

    public ConfigurableData(ListenerRegistry listenerRegistry) {
        this.listenerRegistry = listenerRegistry;
        mapConfig(true);
    }

    private void mapConfig(boolean write) {
        this.isMagicUseAllowed = ConfigUtil.getAndRenderBoolean(ALLOW_MAGIC_USE_KEY, write);
        this.isSelfHarmAllowed = ConfigUtil.getAndRenderBoolean(ALLOW_SELF_HARM, write);
        this.magicCooldownTime = ConfigUtil.getIntWithCorrectedRange(99, 0, MAGIC_COOLDOWN_TIME_KEY) * 1000;
        this.doesSpellCastingRequirePermission = ConfigUtil.getAndRenderBoolean(spellsRequirePermission, write);

        for (SpellType spellType : SpellType.values()) {
            int cooldown = ConfigUtil.getIntWithCorrectedRange(99, 0, spellType.configKey + ".cooldown");
            if (write) {
                ConfigUtil.set(spellType.configKey + ".cooldown", cooldown);
            }
            spellType.behavior.setCooldown(cooldown);
        }
    }

    public void reload() {
        ConfigUtil.reloadConfig();
        mapConfig(false);
    }

    public void tweakCooldown(@NotNull SpellType spellType, int time) {
        ConfigUtil.set(spellType.configKey + ".cooldown", time);
        spellType.behavior.setCooldown(time);
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
        this.magicCooldownTime = magicCooldownTime * 1000;
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


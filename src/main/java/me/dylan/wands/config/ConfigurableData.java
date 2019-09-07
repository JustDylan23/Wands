package me.dylan.wands.config;

import me.dylan.wands.ListenerRegistry;

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
        this.isMagicUseAllowed = ConfigUtil.getBoolean(ALLOW_MAGIC_USE_KEY);
        this.isSelfHarmAllowed = ConfigUtil.getBoolean(ALLOW_SELF_HARM);
        this.magicCooldownTime = ConfigUtil.getInt(MAGIC_COOLDOWN_TIME_KEY);
        this.doesSpellCastingRequirePermission = ConfigUtil.getBoolean(spellsRequirePermission);
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


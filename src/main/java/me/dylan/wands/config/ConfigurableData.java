package me.dylan.wands.config;

import me.dylan.wands.ListenerRegistry;
import me.dylan.wands.Main;

public class ConfigurableData {

    private static final String ALLOW_MAGIC_USE_KEY = "allow-magic-use";
    private static final String ALLOW_SELF_HARM = "allow-self-harm";
    private static final String MAGIC_COOLDOWN_TIME_KEY = "magic-cooldown-time";
    private static final String CASTING_REQUIRES_PERMISSION = "casting-requires-permission";
    private boolean isMagicUseAllowed;
    private boolean allowSelfHarm;
    private int magicCooldownTime;
    private boolean castringRequiresPermission;

    public ConfigurableData() {
        isMagicUseAllowed = ConfigUtil.getBoolean(ALLOW_MAGIC_USE_KEY);
        allowSelfHarm = ConfigUtil.getBoolean(ALLOW_SELF_HARM);
        magicCooldownTime = ConfigUtil.getInt(MAGIC_COOLDOWN_TIME_KEY);
        castringRequiresPermission = ConfigUtil.getBoolean(CASTING_REQUIRES_PERMISSION);
    }

    public void allowMagicUse(boolean value) {
        if (this.isMagicUseAllowed != value) {
            this.isMagicUseAllowed = value;
            ConfigUtil.set(ALLOW_MAGIC_USE_KEY, value);
            ListenerRegistry lg = Main.getPlugin().getListenerRegistry();
            if (value) {
                lg.enableListeners();
            } else {
                lg.disableListeners();
            }
        }
    }

    public boolean isMagicUseAllowed() {
        return isMagicUseAllowed;
    }

    public void setMagicCooldownTime(int magicCooldownTime) {
        this.magicCooldownTime = magicCooldownTime;
        ConfigUtil.set(MAGIC_COOLDOWN_TIME_KEY, magicCooldownTime);
    }

    public int getMagicCooldownTime() {
        return magicCooldownTime;
    }

    public void allowSelfHarm(boolean value) {
        this.allowSelfHarm = value;
        ConfigUtil.set(ALLOW_SELF_HARM, value);
    }

    public boolean isSelfHarmAllowed() {
        return allowSelfHarm;
    }

    public void requirePermissionForCasting(boolean value) {
        this.castringRequiresPermission = value;
        ConfigUtil.set(CASTING_REQUIRES_PERMISSION, value);
    }

    public boolean doesCastingRequirePermission() {
        return castringRequiresPermission;
    }
}


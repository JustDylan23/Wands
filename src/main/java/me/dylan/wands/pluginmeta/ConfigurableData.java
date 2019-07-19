package me.dylan.wands.pluginmeta;

import me.dylan.wands.Main;
import me.dylan.wands.util.ConfigUtil;

public class ConfigurableData {

    private static final String ALLOW_MAGIC_USE_KEY = "allow-magic-use";
    private static final String ALLOW_SELF_HARM = "allow-self-harm";
    private static final String MAGIC_COOLDOWN_TIME_KEY = "magic-cooldown-time";
    private boolean isMagicUseAllowed;
    private boolean allowSelfHarm;
    private int magicCooldownTime;

    public ConfigurableData() {
        isMagicUseAllowed = ConfigUtil.getBoolean(ALLOW_MAGIC_USE_KEY);
        allowSelfHarm = ConfigUtil.getBoolean(ALLOW_SELF_HARM);
        magicCooldownTime = ConfigUtil.getInt(MAGIC_COOLDOWN_TIME_KEY);
    }

    public int getMagicCooldownTime() {
        return magicCooldownTime;
    }

    public void setMagicCooldownTime(int magicCooldownTime) {
        this.magicCooldownTime = magicCooldownTime;
        ConfigUtil.setInt(MAGIC_COOLDOWN_TIME_KEY, magicCooldownTime);
    }

    public boolean isMagicUseAllowed() {
        return isMagicUseAllowed;
    }

    public boolean isSelfHarmAllowed() {
        return allowSelfHarm;
    }

    public void allowMagicUse(boolean value) {
        if (this.isMagicUseAllowed != value) {
            this.isMagicUseAllowed = value;
            ConfigUtil.setBoolean(ALLOW_MAGIC_USE_KEY, value);
            ListenerRegistry lg = Main.getPlugin().getListenerRegistry();
            if (value) {
                lg.enableListeners();
            } else {
                lg.disableListeners();
            }
        }
    }

    public void allowSelfHarm(boolean value) {
        this.allowSelfHarm = value;
        ConfigUtil.setBoolean(ALLOW_SELF_HARM, value);
    }
}


package me.dylan.wands.pluginmeta;

import me.dylan.wands.Main;
import me.dylan.wands.util.ConfigUtil;

public class ConfigurableData {

    private static final String ALLOW_MAGIC_USE_KEY = "allow-magic-use";
    private static final String MAGIC_COOLDOWN_TIME_KEY = "magic-cooldown-time";
    private boolean isMagicUseAllowed;
    private int magicCooldownTime;

    public ConfigurableData() {
        isMagicUseAllowed = ConfigUtil.getBoolean(ALLOW_MAGIC_USE_KEY);
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

    public void allowMagicUse(boolean value) {
        Main.log("new value: " + value);
        if (this.isMagicUseAllowed != value) {
            this.isMagicUseAllowed = value;
            ConfigUtil.setBoolean(ALLOW_MAGIC_USE_KEY, value);
            Main.log("value was different");
            ListenerRegistry lg = Main.getPlugin().getListenerRegistry();
            if (value) {
                Main.log("enabling listeners");
                lg.enableListeners();
            } else {
                Main.log("disabling listeners");
                lg.disableListeners();
            }
        }
    }
}


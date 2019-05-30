package me.dylan.wands.pluginmeta;

import me.dylan.wands.Wands;
import me.dylan.wands.util.ConfigUtil;

public class PluginData {

    private static final String ALLOW_MAGIC_USE_KEY = "allow-magic-use";
    private static final String MAGIC_COOLDOWN_TIME_KEY = "magic-cooldown-time";
    private boolean allowMagicUse;
    private int magicCooldownTime;

    public PluginData() {
        allowMagicUse = ConfigUtil.getBoolean(ALLOW_MAGIC_USE_KEY);
        magicCooldownTime = ConfigUtil.getInt(MAGIC_COOLDOWN_TIME_KEY);
    }

    public int getMagicCooldownTime() {
        return magicCooldownTime;
    }

    public void setMagicCooldownTime(int magicCooldownTime) {
        this.magicCooldownTime = magicCooldownTime;
        ConfigUtil.setInt(MAGIC_COOLDOWN_TIME_KEY, magicCooldownTime);
    }

    public void allowMagicUse(boolean allowMagicUse) {
        if (this.allowMagicUse != allowMagicUse) {
            this.allowMagicUse = allowMagicUse;
            ListenerRegistry lg = Wands.getPlugin().getListenerRegistry();
            if (allowMagicUse) {
                lg.enableListeners();
            } else {
                lg.disableListeners();
            }
        }
        ConfigUtil.setBoolean(ALLOW_MAGIC_USE_KEY, allowMagicUse);
    }
}


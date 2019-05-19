package me.dylan.wands.plugindata;

import me.dylan.wands.Wands;
import me.dylan.wands.utils.ConfigUtil;

public class PluginData {
    private boolean allowMagicUse;
    private final String ALLOW_MAGIC_USE_KEY = "allow-magic-use";

    private int magicCooldownTime;
    private final String MAGIC_COOLDOWN_TIME_KEY = "magic-cooldown-time";

    public PluginData() {
        allowMagicUse = ConfigUtil.getBoolean(ALLOW_MAGIC_USE_KEY);
        magicCooldownTime = ConfigUtil.getInt(MAGIC_COOLDOWN_TIME_KEY);
    }

    public void setMagicCooldownTime(int magicCooldownTime) {
        this.magicCooldownTime = magicCooldownTime;
        ConfigUtil.setInt(MAGIC_COOLDOWN_TIME_KEY, magicCooldownTime);
    }

    public int getMagicCooldownTime() {
        return magicCooldownTime;
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

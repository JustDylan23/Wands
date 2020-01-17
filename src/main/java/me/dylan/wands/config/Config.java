package me.dylan.wands.config;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

class Config {
    @SerializedName("spell_data")
    private final Map<Integer, SpellConfig> spellConfigMap = new HashMap<>();
    @SerializedName("is_magic_enabled")
    private boolean isMagicEnabled = true;
    @SerializedName("spell_cooldown")
    private int globalSpellCooldown = 0;
    @SerializedName("casting_requires_permission")
    private boolean castingRequiresPermission = true;
    @SerializedName("send_updating_notifications")
    private boolean sendUpdateNotifications = true;

    Map<Integer, SpellConfig> getSpellConfigMap() {
        return spellConfigMap;
    }

    boolean isMagicEnabled() {
        return isMagicEnabled;
    }

    void setMagicEnabled(boolean value) {
        isMagicEnabled = value;
    }

    int getGlobalSpellCooldown() {
        return globalSpellCooldown;
    }

    void setGlobalSpellCooldown(int seconds) {
        this.globalSpellCooldown = seconds;
    }

    boolean doesCastingRequirePermission() {
        return castingRequiresPermission;
    }

    void setCastingRequiresPermission(boolean value) {
        this.castingRequiresPermission = value;
    }

    boolean areNotificationsEnabled() {
        return sendUpdateNotifications;
    }

    void enableNotifications(boolean value) {
        this.sendUpdateNotifications = value;
    }

    static class SpellConfig {
        private int cooldown = 0;

        int getCooldown() {
            return cooldown;
        }

        void setCooldown(int seconds) {
            this.cooldown = seconds;
        }
    }
}

package me.dylan.wands.config;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

class Config {
    @SerializedName("is_magic_enabled")
    private boolean isMagicEnabled = true;

    @SerializedName("spell_cooldown")
    private int globalSpellCooldown = 0;

    @SerializedName("casting_requires_permission")
    private boolean castingRequiresPermission = true;

    @SerializedName("spell_data")
    private final Map<Integer, SpellConfig> spellConfigMap = new HashMap<>();

    boolean isMagicEnabled() {
        return isMagicEnabled;
    }

    void setMagicEnabled(boolean bool) {
        isMagicEnabled = bool;
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

    void setCastingRequiresPermission(boolean bool) {
        this.castingRequiresPermission = bool;
    }

    Map<Integer, SpellConfig> getSpellConfigMap() {
        return spellConfigMap;
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

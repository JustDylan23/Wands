package me.dylan.wands.config;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class Config {
    @SerializedName("is_magic_enabled")
    private boolean isMagicEnabled = true;

    @SerializedName("spell_cooldown")
    private int spellCooldown;

    @SerializedName("casting_requires_permission")
    private boolean castingRequiresPermission = true;

    @SerializedName("spell_data")
    private Map<Integer, SpellConfig> spellConfigMap = new HashMap<>();

    boolean isMagicEnabled() {
        return isMagicEnabled;
    }

    void setMagicEnabled(boolean magicEnabled) {
        isMagicEnabled = magicEnabled;
    }

    int getSpellCooldown() {
        return spellCooldown;
    }

    void setSpellCooldown(int spellCooldown) {
        this.spellCooldown = spellCooldown;
    }

    boolean doesCastingRequirePermission() {
        return castingRequiresPermission;
    }

    void setCastingRequiresPermission(boolean doesCastingRequirePermission) {
        this.castingRequiresPermission = doesCastingRequirePermission;
    }

    public Map<Integer, SpellConfig> getSpellConfigMap() {
        return spellConfigMap;
    }

    public void setSpellConfigMap(Map<Integer, SpellConfig> spellConfigMap) {
        this.spellConfigMap = spellConfigMap;
    }

    public static class SpellConfig {
        private int cooldown;

        int getCooldown() {
            return cooldown;
        }

        void setCooldown(int cooldown) {
            this.cooldown = cooldown;
        }
    }
}

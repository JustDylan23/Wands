package me.dylan.wands.config;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class Config implements JsonDeserializer<Config> {
    @SerializedName("is_magic_enabled")
    private boolean isMagicEnabled = true;

    @SerializedName("spell_cooldown")
    private int globalSpellCooldown = 0;

    @SerializedName("casting_requires_permission")
    private boolean castingRequiresPermission = true;

    @SerializedName("spell_data")
    private Map<Integer, SpellConfig> spellConfigMap = new HashMap<>();

    public boolean isMagicEnabled() {
        return isMagicEnabled;
    }

    public void setMagicEnabled(boolean bool) {
        isMagicEnabled = bool;
    }

    public int getGlobalSpellCooldown() {
        return globalSpellCooldown;
    }

    public void setGlobalSpellCooldown(int seconds) {
        this.globalSpellCooldown = seconds;
    }

    public boolean doesCastingRequirePermission() {
        return castingRequiresPermission;
    }

    public void setCastingRequiresPermission(boolean bool) {
        this.castingRequiresPermission = bool;
    }

    public Map<Integer, SpellConfig> getSpellConfigMap() {
        return spellConfigMap;
    }

    public void setSpellConfigMap(Map<Integer, SpellConfig> spellConfigMap) {
        this.spellConfigMap = spellConfigMap;
    }

    @Override
    public Config deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return null;
    }

    public static class SpellConfig {
        private int cooldown = 0;

        int getCooldown() {
            return cooldown;
        }

        void setCooldown(int seconds) {
            this.cooldown = seconds;
        }
    }
}

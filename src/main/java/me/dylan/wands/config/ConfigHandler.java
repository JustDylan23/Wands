package me.dylan.wands.config;

import com.google.gson.Gson;
import me.dylan.wands.ListenerRegistry;
import me.dylan.wands.WandsPlugin;
import me.dylan.wands.config.Config.SpellConfig;
import me.dylan.wands.spell.SpellType;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public final class ConfigHandler {
    private final Config config;
    private final ListenerRegistry listenerRegistry;

    private ConfigHandler(@NotNull Config config, ListenerRegistry listenerRegistry) {
        this.config = config;
        this.listenerRegistry = listenerRegistry;

        mapSpellConfigSection(config);
    }

    public static ConfigHandler load(File file, ListenerRegistry listenerRegistry) {
        if (file.exists()) {
            try (DataInputStream stream = new DataInputStream(new GZIPInputStream(new FileInputStream(file)))) {
                Config config = new Gson().fromJson(stream.readUTF(), Config.class);
                WandsPlugin.log("Loaded config");
                return new ConfigHandler(config, listenerRegistry);
            } catch (IOException e) {
                throw new IllegalStateException("Config is corrupted");
            }
        } else {
            WandsPlugin.log("No config found");
            return new ConfigHandler(new Config(), listenerRegistry);
        }
    }

    private void mapSpellConfigSection(@NotNull Config config) {
        config.getSpellConfigMap().forEach((id, spellConfig) -> {
            SpellType spellType = SpellType.getSpellById(id);
            if (spellType != null) {
                spellType.behavior.setCooldown(spellConfig.getCooldown());
            }
        });
    }

    public void save(File file) {
        if (!file.exists()) {
            WandsPlugin.log("No config to save to found, preparing directory");
            file.getParentFile().mkdirs();
        }
        try (DataOutputStream stream = new DataOutputStream(new GZIPOutputStream(new FileOutputStream(file)))) {
            stream.writeUTF(new Gson().toJson(this));
            WandsPlugin.log("Saved config");
        } catch (IOException e) {
            WandsPlugin.warn("Failed to save config");
        }
    }

    public void setSpellCooldown(@NotNull SpellType spellType, int time) {
        Map<Integer, SpellConfig> spellConfigMap = config.getSpellConfigMap();
        SpellConfig spellConfig = spellConfigMap.getOrDefault(spellType.id, new SpellConfig());
        spellConfig.setCooldown(time);
        spellConfigMap.putIfAbsent(spellType.id, spellConfig);
        spellType.behavior.setCooldown(time);
    }

    public boolean isMagicEnabled() {
        return config.isMagicEnabled();
    }

    public void enableMagic(boolean value) {
        if (config.isMagicEnabled() != value) {
            config.setMagicEnabled(value);
            if (value) {
                listenerRegistry.enableListeners();
            } else {
                listenerRegistry.disableListeners();
            }
        }
    }

    public int getGlobalSpellCooldown() {
        return config.getGlobalSpellCooldown();
    }

    public void setGlobalSpellCooldown(int seconds) {
        config.setGlobalSpellCooldown(seconds);
    }

    public boolean doesCastingRequirePermission() {
        return config.doesCastingRequirePermission();
    }

    public void requirePermissionForCasting(boolean bool) {
        config.setCastingRequiresPermission(bool);
    }
}


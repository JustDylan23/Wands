package me.dylan.wands.config;

import com.google.gson.Gson;
import me.dylan.wands.ListenerRegistry;
import me.dylan.wands.WandsPlugin;
import me.dylan.wands.config.Config.SpellConfig;
import me.dylan.wands.spell.SpellType;
import org.jetbrains.annotations.NotNull;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

public class ConfigHandler {
    private final Config config;
    private final ListenerRegistry listenerRegistry;

    public ConfigHandler(@NotNull Config config, ListenerRegistry listenerRegistry) {
        this.config = config;
        this.listenerRegistry = listenerRegistry;

        mapSpellConfigSection(config);
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
        try (DataOutputStream stream = new DataOutputStream(new GZIPOutputStream(new FileOutputStream(file)))) {
            stream.writeUTF(new Gson().toJson(config));
        } catch (IOException e) {
            WandsPlugin.warn("Could not save config");
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


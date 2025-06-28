package com.prismforge.boosters.manager;

import com.prismforge.boosters.Boosters;
import com.prismforge.boosters.data.BoosterType;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigManager {
    private final Boosters plugin;

    public ConfigManager(Boosters plugin) {
        this.plugin = plugin;
    }

    public void loadBoosters(BoosterManager manager) {
        FileConfiguration config = plugin.getConfig();
        if (!config.contains("boosters")) return;

        for (String key : config.getConfigurationSection("boosters").getKeys(false)) {
            String display = config.getString("boosters." + key + ".display");
            double strength = config.getDouble("boosters." + key + ".defaultStrength");
            int duration = config.getInt("boosters." + key + ".defaultDuration");

            BoosterType type = new BoosterType(key, display, strength, duration);
            manager.registerType(type);
        }
    }
}

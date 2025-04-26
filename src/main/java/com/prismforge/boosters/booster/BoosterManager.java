package com.prismforge.boosters.booster;

import com.prismforge.boosters.Boosters;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages all booster types loaded from config.
 */
public class BoosterManager {
    private final ActiveBoosterManager activeBoosterManager;

    private final Boosters plugin;
    private final Map<String, BoosterType> boosterTypes = new HashMap<>();


    public BoosterManager(Boosters plugin) {
        this.plugin = plugin;
        this.activeBoosterManager = new ActiveBoosterManager(plugin); // <--- Add this
        loadBoosters();
    }
    public ActiveBoosterManager getActiveBoosterManager() {
        return activeBoosterManager;
    }

    /**
     * Loads booster types from config.yml
     */
    public void loadBoosters() {
        boosterTypes.clear();
        ConfigurationSection section = plugin.getConfig().getConfigurationSection("boosters");
        if (section != null) {
            for (String key : section.getKeys(false)) {
                String displayName = section.getString(key + ".display-name");
                String activateCommand = section.getString(key + ".activate-command");
                List<String> tipCommands = section.getStringList(key + ".tip"); // NEW
                if (displayName != null && activateCommand != null) {
                    boosterTypes.put(key.toLowerCase(), new BoosterType(key, displayName, activateCommand, tipCommands));
                }
            }
        }
    }


    public BoosterType getBoosterType(String type) {
        return boosterTypes.get(type.toLowerCase());
    }

    public Map<String, BoosterType> getBoosterTypes() {
        return boosterTypes;
    }
}

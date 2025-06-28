package com.prismforge.boosters.manager;

import com.prismforge.boosters.Boosters;
import com.prismforge.boosters.data.Booster;
import com.prismforge.boosters.data.BoosterQueue;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class GlobalBoosterFile {

    private final Boosters plugin;

    public GlobalBoosterFile(Boosters plugin) {
        this.plugin = plugin;
    }

    public void save(Map<String, BoosterQueue> queues) {
        File file = new File(plugin.getDataFolder(), "boosters.yml");
        YamlConfiguration config = new YamlConfiguration();
        Map<String, List<Map<String, Object>>> boosterData = new HashMap<String, List<Map<String, Object>>>();

        for (Map.Entry<String, BoosterQueue> entry : queues.entrySet()) {
            String type = entry.getKey();
            BoosterQueue queue = entry.getValue();

            List<Map<String, Object>> boosters = new ArrayList<Map<String, Object>>();

            if (queue.getCurrent() != null) {
                boosters.add(boosterToMap(queue.getCurrent()));
            }

            for (Booster b : queue.getQueue()) {
                boosters.add(boosterToMap(b));
            }

            boosterData.put(type, boosters);
        }

        config.createSection("boosters", boosterData);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, List<Booster>> load() {
        File file = new File(plugin.getDataFolder(), "boosters.yml");
        Map<String, List<Booster>> loaded = new HashMap<String, List<Booster>>();

        if (!file.exists()) return loaded;

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        if (!config.contains("boosters")) return loaded;

        for (String type : config.getConfigurationSection("boosters").getKeys(false)) {
            List<Map<?, ?>> boosterList = config.getMapList("boosters." + type);
            List<Booster> boosters = new ArrayList<Booster>();

            for (Map<?, ?> map : boosterList) {
                double strength = Double.parseDouble(map.get("strength").toString());
                int remaining = Integer.parseInt(map.get("remaining").toString());
                boosters.add(new Booster(type, strength, remaining));
            }

            loaded.put(type, boosters);
        }

        return loaded;
    }

    private Map<String, Object> boosterToMap(Booster booster) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("strength", booster.getStrength());
        map.put("remaining", booster.getDurationSeconds());
        return map;
    }
}

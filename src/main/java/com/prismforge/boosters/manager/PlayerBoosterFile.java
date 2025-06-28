package com.prismforge.boosters.manager;

import com.prismforge.boosters.Boosters;
import com.prismforge.boosters.data.PlayerBooster;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class PlayerBoosterFile {

    private final Boosters plugin;

    public PlayerBoosterFile(Boosters plugin) {
        this.plugin = plugin;
    }

    public List<PlayerBooster> load(UUID uuid) {
        File file = getFile(uuid);
        List<PlayerBooster> boosters = new ArrayList<PlayerBooster>();

        if (!file.exists()) return boosters;

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        if (!config.contains("boosters")) return boosters;

        List<Map<?, ?>> list = config.getMapList("boosters");
        for (Map<?, ?> map : list) {
            String type = map.get("type").toString();
            double strength = Double.parseDouble(map.get("strength").toString());
            int duration = Integer.parseInt(map.get("duration").toString());
            boosters.add(new PlayerBooster(type, strength, duration));
        }

        return boosters;
    }

    public void save(UUID uuid, List<PlayerBooster> boosters) {
        File file = getFile(uuid);

        if (boosters.isEmpty()) {
            if (file.exists()) file.delete();
            return;
        }

        YamlConfiguration config = new YamlConfiguration();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        for (PlayerBooster booster : boosters) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("type", booster.getType());
            map.put("strength", booster.getStrength());
            map.put("duration", booster.getDuration());
            list.add(map);
        }

        config.set("boosters", list);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File getFile(UUID uuid) {
        File folder = new File(plugin.getDataFolder(), "players");
        if (!folder.exists()) folder.mkdirs();
        return new File(folder, uuid.toString() + ".yml");
    }
}

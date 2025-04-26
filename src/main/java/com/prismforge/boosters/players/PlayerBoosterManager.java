package com.prismforge.boosters.players;

import com.prismforge.boosters.Boosters;
import com.prismforge.boosters.booster.Booster;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Manages boosters stored per player.
 */
public class PlayerBoosterManager {

    private final Boosters plugin;
    private final File playerDataFolder;

    public PlayerBoosterManager(Boosters plugin) {
        this.plugin = plugin;
        this.playerDataFolder = new File(plugin.getDataFolder(), "players");
        if (!playerDataFolder.exists()) {
            playerDataFolder.mkdirs();
        }
    }

    /**
     * Load a player's boosters.
     */
    public List<Booster> getPlayerBoosters(UUID uuid) {
        File file = new File(playerDataFolder, uuid.toString() + ".yml");
        if (!file.exists()) return Collections.emptyList();

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        List<Booster> boosters = new ArrayList<>();

        if (config.contains("boosters")) {
            for (String type : config.getConfigurationSection("boosters").getKeys(false)) {
                for (String strengthKey : config.getConfigurationSection("boosters." + type).getKeys(false)) {
                    List<Map<?, ?>> boosterList = config.getMapList("boosters." + type + "." + strengthKey);
                    for (Map<?, ?> rawMap : boosterList) {
                        Map<String, Object> map = (Map<String, Object>) rawMap; // Safe cast here!

                        Object durationObj = map.getOrDefault("duration", 0);
                        Object amountObj = map.getOrDefault("amount", 0);

                        int duration = (durationObj instanceof Number) ? ((Number) durationObj).intValue() : 0;
                        int amount = (amountObj instanceof Number) ? ((Number) amountObj).intValue() : 0;

                        for (int i = 0; i < amount; i++) {
                            boosters.add(new Booster(type, Double.parseDouble(strengthKey), duration));
                        }
                    }
                }
            }
        }
        return boosters;
    }

    /**
     * Add a booster to a player's data.
     */
    public void addBooster(UUID uuid, Booster booster) {
        File file = new File(playerDataFolder, uuid.toString() + ".yml");
        YamlConfiguration config = file.exists() ? YamlConfiguration.loadConfiguration(file) : new YamlConfiguration();

        String path = "boosters." + booster.getType() + "." + booster.getStrength();
        List<Map<?, ?>> boosterList = config.getMapList(path);

        boolean found = false;
        for (Map<?, ?> rawMap : boosterList) {
            Map<String, Object> map = (Map<String, Object>) rawMap; // <-- CAST here!

            Object durationObj = map.getOrDefault("duration", 0);
            Object amountObj = map.getOrDefault("amount", 0);

            int duration = (durationObj instanceof Number) ? ((Number) durationObj).intValue() : 0;
            int amount = (amountObj instanceof Number) ? ((Number) amountObj).intValue() : 0;

            if (duration == booster.getDurationMinutes()) {
                map.put("amount", amount + 1); // ✅ Now this works safely
                found = true;
                break;
            }
        }

        if (!found) {
            Map<String, Object> map = new HashMap<>();
            map.put("duration", booster.getDurationMinutes());
            map.put("amount", 1);
            boosterList.add(map);
        }

        config.set(path, boosterList);

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Remove a booster from a player's data.
     */
    public void removeBooster(UUID uuid, Booster booster) {
        File file = new File(playerDataFolder, uuid.toString() + ".yml");
        if (!file.exists()) return;

        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        String path = "boosters." + booster.getType() + "." + booster.getStrength();
        List<Map<?, ?>> boosterList = config.getMapList(path);

        for (Iterator<Map<?, ?>> it = boosterList.iterator(); it.hasNext(); ) {
            Map<?, ?> rawMap = it.next();
            Map<String, Object> map = (Map<String, Object>) rawMap; // Cast

            Object durationObj = map.getOrDefault("duration", 0);
            Object amountObj = map.getOrDefault("amount", 0);

            int duration = (durationObj instanceof Number) ? ((Number) durationObj).intValue() : 0;
            int amount = (amountObj instanceof Number) ? ((Number) amountObj).intValue() : 0;

            if (duration == booster.getDurationMinutes()) {
                if (amount <= 1) {
                    it.remove();
                } else {
                    map.put("amount", amount - 1); // Now safe
                }
                break;
            }
        }


        if (boosterList.isEmpty()) {
            config.set(path, null);
        } else {
            config.set(path, boosterList);
        }

        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // If player has no boosters left, delete file
        if (config.getKeys(true).isEmpty()) {
            file.delete();
        }
    }
}

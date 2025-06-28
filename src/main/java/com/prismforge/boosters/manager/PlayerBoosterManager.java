package com.prismforge.boosters.manager;

import com.prismforge.boosters.Boosters;
import com.prismforge.boosters.data.PlayerBooster;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;

public class PlayerBoosterManager {

    private final Boosters plugin;
    private final Map<UUID, List<PlayerBooster>> playerBoosters = new HashMap<>();
    private final PlayerBoosterFile boosterFile;

    public PlayerBoosterManager(Boosters plugin) {
        this.plugin = plugin;
        this.boosterFile = new PlayerBoosterFile(plugin);

        // Auto-save every 10 min
        new BukkitRunnable() {
            @Override
            public void run() {
                saveAll();
            }
        }.runTaskTimerAsynchronously(plugin, 20L * 600, 20L * 600); // every 600 sec = 10 min
    }

    public List<PlayerBooster> getBoosters(UUID uuid) {
        return playerBoosters.getOrDefault(uuid, new ArrayList<>());
    }

    public void addBooster(UUID uuid, PlayerBooster booster) {
        playerBoosters.computeIfAbsent(uuid, k -> new ArrayList<>()).add(booster);
        save(uuid);
    }

    public void removeBooster(UUID uuid, PlayerBooster booster) {
        List<PlayerBooster> list = playerBoosters.get(uuid);
        if (list != null) {
            list.remove(booster);
            if (list.isEmpty()) {
                playerBoosters.remove(uuid);
                boosterFile.save(uuid, new ArrayList<>()); // deletes file
            }
        }
        save(uuid);
    }

    public void load(UUID uuid) {
        List<PlayerBooster> list = boosterFile.load(uuid);
        if (!list.isEmpty()) {
            playerBoosters.put(uuid, list);
        }
    }

    public void save(UUID uuid) {
        List<PlayerBooster> list = playerBoosters.getOrDefault(uuid, new ArrayList<>());
        boosterFile.save(uuid, list);
    }

    public void saveAll() {
        for (UUID uuid : playerBoosters.keySet()) {
            save(uuid);
        }
    }

    public void unload(UUID uuid) {
        save(uuid);
        playerBoosters.remove(uuid);
    }
}

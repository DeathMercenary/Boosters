package com.prismforge.boosters.manager;

import com.prismforge.boosters.Boosters;
import com.prismforge.boosters.data.Booster;
import com.prismforge.boosters.data.BoosterQueue;
import com.prismforge.boosters.data.BoosterType;

import java.util.*;

public class BoosterManager {
    private final Map<String, BoosterQueue> activeBoosters = new HashMap<>();
    private final Map<String, BoosterType> boosterTypes = new HashMap<>();
    private final Boosters plugin;
    public void registerType(BoosterType type) {
        boosterTypes.put(type.getName().toLowerCase(), type);
        activeBoosters.put(type.getName().toLowerCase(), new BoosterQueue(plugin, type.getName()));
    }

    public BoosterType getType(String name) {
        return boosterTypes.get(name.toLowerCase());
    }

    public void addBooster(String type, double strength, int duration) {
        BoosterQueue queue = activeBoosters.get(type.toLowerCase());
        if (queue != null) {
            queue.addBooster(new Booster(type, strength, duration));
        }
    }

    public void tick() {
        for (BoosterQueue queue : activeBoosters.values()) {
            queue.tick();
        }
    }
    private final GlobalBoosterFile globalFile;

    public BoosterManager(Boosters plugin) {
        this.globalFile = new GlobalBoosterFile(plugin);
        this.plugin = plugin;
        // registerType must be called by ConfigManager first!
    }

    public void saveGlobalBoosters() {
        globalFile.save(activeBoosters);
    }

    public void loadGlobalBoosters() {
        Map<String, List<Booster>> map = globalFile.load();
        for (Map.Entry<String, List<Booster>> entry : map.entrySet()) {
            String type = entry.getKey();
            BoosterQueue queue = activeBoosters.get(type.toLowerCase());
            if (queue == null) continue;

            List<Booster> list = entry.getValue();
            if (!list.isEmpty()) {
                queue.addBooster(list.get(0)); // current
                for (int i = 1; i < list.size(); i++) {
                    queue.addBooster(list.get(i));
                }
            }
        }
    }

    public Map<String, BoosterQueue> getActiveBoosters() {
        return activeBoosters;
    }
    public Collection<Booster> getAllActiveBoosters() {
        List<Booster> list = new ArrayList<Booster>();
        for (BoosterQueue queue : activeBoosters.values()) {
            if (queue.getCurrent() != null) {
                list.add(queue.getCurrent());
            }
        }
        return list;
    }

    public Booster getActiveBooster(String type) {
        BoosterQueue queue = activeBoosters.get(type.toLowerCase());
        return queue != null ? queue.getCurrent() : null;
    }
}

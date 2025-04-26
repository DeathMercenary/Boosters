package com.prismforge.boosters.booster;

import com.prismforge.boosters.Boosters;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Manages all currently active boosters.
 */
public class ActiveBoosterManager {

    private final Boosters plugin;
    private final List<ActiveBooster> activeBoosters = new ArrayList<>();
    private int rotatingBossbarIndex = 0;

    public ActiveBoosterManager(Boosters plugin) {
        this.plugin = plugin;
        startTicking();
    }

    /**
     * Adds a booster to the active list.
     */
    public void activateBooster(ActiveBooster booster) {
        activeBoosters.add(booster);
    }

    /**
     * Returns a copy of the current active boosters.
     */
    public List<ActiveBooster> getActiveBoosters() {
        return new ArrayList<>(activeBoosters);
    }

    /**
     * Check if a type is currently active.
     */
    public boolean isTypeActive(String type) {
        for (ActiveBooster booster : activeBoosters) {
            if (booster.getType().equalsIgnoreCase(type)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Tick boosters: remove expired ones and update bossbar.
     */
    private void startTicking() {
        new BukkitRunnable() {
            @Override
            public void run() {
                Iterator<ActiveBooster> iterator = activeBoosters.iterator();
                while (iterator.hasNext()) {
                    ActiveBooster booster = iterator.next();
                    if (booster.isExpired()) {
                        iterator.remove();
                    }
                }

                if (activeBoosters.isEmpty()) {
                    // No active boosters — hide bossbar
                    BossbarHandler.hideBossbarForAll();
                    return;
                }

                // Rotate through boosters every X seconds
                rotatingBossbarIndex++;
                if (rotatingBossbarIndex >= activeBoosters.size()) {
                    rotatingBossbarIndex = 0;
                }

                ActiveBooster booster = activeBoosters.get(rotatingBossbarIndex);
                BossbarHandler.updateBossbar(booster);
            }
        }.runTaskTimer(plugin, 20L, 20L * plugin.getConfig().getInt("settings.bossbar.interval-seconds", 5));
    }
}

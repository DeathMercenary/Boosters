package com.prismforge.boosters.data;

import com.prismforge.boosters.Boosters;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;

import java.util.LinkedList;
import java.util.Queue;

public class
BoosterQueue {

    private final Boosters plugin;
    private final String type;

    private Booster current;
    private final Queue<Booster> queue = new LinkedList<Booster>();

    private BukkitTask xpSyncTask;

    public BoosterQueue(Boosters plugin, String type) {
        this.plugin = plugin;
        this.type = type.toLowerCase();
    }

    public void addBooster(Booster booster) {
        if (current == null) {
            current = booster;
            onNewCurrent();
        } else if (booster.getStrength() == current.getStrength()) {
            current.extend(booster.getDurationSeconds());
        } else {
            queue.add(booster);
        }
    }

    public Queue<Booster> getQueue() {
        return queue;
    }

    public Booster getCurrent() {
        return current;
    }

    public void tick() {
        if (current != null) {
            current.tick();
            if (current.isExpired()) {
                current = queue.poll();
                onNewCurrent();
            }
        }
    }

    private void onNewCurrent() {
        // Stop any previous XP sync task
        if (xpSyncTask != null) {
            xpSyncTask.cancel();
            xpSyncTask = null;
        }

        if (current != null && type.equals("xp")) {
            // If next booster is XP: start new sync
            runXpSync(current.getStrength());

            xpSyncTask = Bukkit.getScheduler().runTaskTimer(plugin, new Runnable() {
                @Override
                public void run() {
                    runXpSync(current.getStrength());
                }
            }, 20L * 300, 20L * 300); // every 5 min
        } else if (type.equals("xp")) {
            // If booster expired OR next is not XP: reset to base 1
            runXpSync(1.0);
        }
    }


    private void runXpSync(double strength) {
        String cmd = "skills:setbooster " + strength;
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
    }

    public void cancelXpSync() {
        if (xpSyncTask != null) {
            xpSyncTask.cancel();
            xpSyncTask = null;
        }
    }
}

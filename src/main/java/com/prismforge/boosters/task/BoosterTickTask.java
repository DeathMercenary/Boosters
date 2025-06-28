package com.prismforge.boosters.task;

import com.prismforge.boosters.manager.BoosterManager;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

public class BoosterTickTask extends BukkitRunnable {

    private final BoosterManager boosterManager;

    public BoosterTickTask(BoosterManager boosterManager) {
        this.boosterManager = boosterManager;
    }

    @Override
    public void run() {
        boosterManager.tick();
    }
}
